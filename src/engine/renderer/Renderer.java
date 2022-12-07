package engine.renderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import engine.gameobject.GameObject;
import engine.gameobject.SpriteRenderer;
import engine.gfx.Texture;

public class Renderer {

	private final int SORT_INC = 4;
	private final int SORT_BASE = (int)Math.pow(2, SORT_INC);

	private final int MAX_BATCH_SIZE = 1000;
	private List<RenderBatch> batches;
	
	private SortHeuristic sortHeuristic;

	public Renderer() {
		this.batches = new ArrayList<>();
		this.sortHeuristic = new SortHeuristic() {public float heuristic(GameObject obj) {return 0;}};
	}
	
	public Renderer(SortHeuristic sortHeuristic) {
		this.batches = new ArrayList<>();
		this.sortHeuristic = sortHeuristic;
	}

	public void render() {
		for (RenderBatch batch:batches) {
			batch.updateVertexData();
		}
		for (RenderBatch batch:batches) {
			batch.render();
		}
	}

	public void add(GameObject go) {
		SpriteRenderer spr = go.getSpriteRenderer();
		if (spr != null) {
			add(spr);
		}
	}

	public void remove(GameObject go) {
		SpriteRenderer spr = go.getSpriteRenderer();
		if (spr != null) {
			remove(spr);
		}
	}

	public void add(SpriteRenderer sprite) {		
		float lastMin = Float.POSITIVE_INFINITY;
		RenderBatch lastBatch = null;
		boolean needNewBatch = false;
		boolean added = false;
		boolean hasZIndex = false;
		float sprHeuristic = this.sortHeuristic.heuristic(sprite.gameObject);
		for(RenderBatch batch:batches) {
			if(batch.getzIndex()!=sprite.gameObject.transform.getZIndex()) continue;
			hasZIndex = true;
			//within last batch range
			if(lastBatch != null && sprHeuristic >= batch.getMaxY() && sprHeuristic <= lastBatch.getMaxY()) {
				if(addToBatch(sprite, lastBatch)) {
					added = true;
					break;
				}
			}
			//within current batch range
			if(sprHeuristic <= lastMin && sprHeuristic >= batch.getMinY()) {
				if(addToBatch(sprite, batch)) {
					added = true;
					break;
				}
			}
			//cant add to new or prev batch
			//if under current batch wait for next cycle
			if(sprHeuristic <= batch.getMinY()) {
				lastMin = batch.getMinY();
				lastBatch = batch;
				continue;
			}
			//if above current batch it cant add to either last or current so create new batch
			if(sprHeuristic >= batch.getMaxY()) {
				needNewBatch = true;
				break;
			}
			//if within current batch and cant add split batch at point
			if(sprHeuristic <= batch.getMaxY() && sprHeuristic >= batch.getMinY()) {
				RenderBatch splitBatch = batch.split(sprHeuristic);
				this.batches.add(splitBatch);
				//test if can add to either new batches
				if(addToBatch(sprite, batch)) {
					added = true;
					break;
				}
				if(addToBatch(sprite, splitBatch)) {
					added = true;
					break;
				}
				//if not create a new batch to add to
				needNewBatch = true;
				break;
			}
			lastMin = batch.getMinY();
			lastBatch = batch;
		}
		if(!added && !needNewBatch && hasZIndex && lastBatch != null) {
			//check if under last batch and try to add
			if(sprHeuristic <= lastBatch.getMinY() && lastBatch.getzIndex() == sprite.gameObject.transform.getZIndex()) {
				if(addToBatch(sprite, lastBatch)) {
					added = true;
				}else {
					needNewBatch = true;
				}
			}
		}
		if(needNewBatch || !hasZIndex) {
			RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.gameObject.transform.getZIndex(), this);
			newBatch.start();
			this.batches.add(newBatch);
			newBatch.addSprite(sprite);
			added = true;
		}
		
		assert added:sprite.gameObject.getObjName()+" was not added";
		
		sortMinMax();
		sortZIndex();
	}
	
	private boolean addToBatch(SpriteRenderer sprite, RenderBatch batch) {
		if (!batch.hasRoom()) return false;
		if(batch.getzIndex() != sprite.gameObject.transform.getZIndex()) return false;
		Texture texture = sprite.getTexture();
		if(texture != null && !batch.hasTexture(texture) && !batch.hasTextureRoom()) return false;
		batch.addSprite(sprite);
		return true;
	}

	public void remove(SpriteRenderer sprite) {
		for (RenderBatch batch : batches) {
			batch.removeSprite(sprite);
		}
		this.batches.removeIf(b -> b.getNumSprites() == 0);
	}
	
	public void print() {
		System.out.println("-----");
		for(RenderBatch batch:this.batches) {
			System.out.println(batch);
			batch.print();
		}
	}
	
	private void sortMinMax() {
		if(isMinMaxSorted()) return;
		//simple sort
		Collections.sort(this.batches);
	}

	private void sortZIndex() {
		//check if batches are sorted already
		if(isZIndexSorted()) return;
		//sort via radix sort
		int[] counts = new int[SORT_BASE];
		RenderBatch[] buffer = new RenderBatch[this.batches.size()];
		for(int i = 0;i < 4;i++){
			//get counts
			for(int j = 0;j < this.batches.size();j++){
				int digit = (this.batches.get(j).getzIndex()+33768)>>(SORT_INC*i)&(SORT_BASE-1);
				counts[digit]++;
			}
			//calc prefix sum
			for(int j = 1;j < SORT_BASE;j++){
				counts[j] += counts[j-1];
			}
			//
			for(int j = this.batches.size()-1;j >= 0;j--){
				int digit = (this.batches.get(j).getzIndex()+33768)>>(SORT_INC*i)&(SORT_BASE-1);
				buffer[--counts[digit]] = this.batches.get(j);
			}
			//swap buffer
			this.batches = new ArrayList<RenderBatch>(Arrays.asList(buffer));
			counts = new int[SORT_BASE];
		}
	}

	private boolean isMinMaxSorted() {
		return false;
	}
	
	private boolean isZIndexSorted() {
		int prev = -33768;
		for(RenderBatch batch:this.batches){
			if(batch.getzIndex() < prev) return false;
			prev = batch.getzIndex();
		}
		return true;
	}

	public SortHeuristic getSortHeuristic() {
		return sortHeuristic;
	}
	
}