package engine.renderer;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

import engine.gameobject.SpriteRenderer;
import engine.gfx.Shader;
import engine.gfx.Texture;
import game.Game;
import game.res.Assets;

public class RenderBatch implements Comparable<RenderBatch>{

	//Vertex
	//Pos 2		Color 4	
	private final int POS_SIZE = 3;
	private final int COLOR_SIZE = 4;
	private final int TEX_COORDS_SIZE = 2;
	private final int TEX_ID_SIZE = 1;

	private final int POS_OFFSET = 0;
	private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
	private final int TEX_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
	private final int TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * Float.BYTES;

	private final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE + TEX_COORDS_SIZE + TEX_ID_SIZE;
	private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

	private final int NUM_VERTICES = 4;
	private final int NUM_ELEMENTS = 6;

	private SpriteRenderer[] sprites;
	private int numSprites;
	private float[] vertices;
	private int[] indices;
	private int[] texSlots;

	private int vaoID, vboID, eboID;
	private int maxBatchSize;
	private Shader shader;

	private int maxTextures;
	private List<Texture>textures;

	private int zIndex;
	private float minY, maxY;

	private Renderer parent;
	private boolean rebufferData = true;

	public RenderBatch(int maxBatchSize, int zIndex, Renderer parent) {
		this.sprites = new SpriteRenderer[maxBatchSize];
		this.maxBatchSize = maxBatchSize;
		this.zIndex = zIndex;

		this.parent = parent;
		this.shader = Assets.getShader(Assets.SHDR_DEFAULT);

		this.vertices = new float[maxBatchSize * NUM_VERTICES * VERTEX_SIZE];

		this.numSprites = 0;

		IntBuffer binds = BufferUtils.createIntBuffer(1);
		GL30.glGetIntegerv(GL30.GL_MAX_TEXTURE_IMAGE_UNITS, binds);

		this.maxTextures = binds.get(0);
		this.textures = new ArrayList<Texture>();
		this.texSlots = new int[this.maxTextures];
		for(int i = 0;i < this.texSlots.length;i++) {
			this.texSlots[i] = i;
		}
	}

	public void start() {
		vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);

		vboID = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboID);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL30.GL_DYNAMIC_DRAW);

		eboID = GL30.glGenBuffers();
		this.indices = generateDefaultIndices();
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, eboID);
		GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, indices, GL30.GL_DYNAMIC_DRAW);

		GL30.glVertexAttribPointer(0, POS_SIZE, GL30.GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
		GL30.glEnableVertexAttribArray(0);

		GL30.glVertexAttribPointer(1, COLOR_SIZE, GL30.GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
		GL30.glEnableVertexAttribArray(1);

		GL30.glVertexAttribPointer(2, TEX_COORDS_SIZE, GL30.GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
		GL30.glEnableVertexAttribArray(2);

		GL30.glVertexAttribPointer(3, TEX_ID_SIZE, GL30.GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
		GL30.glEnableVertexAttribArray(3);
	}

	public void updateVertexData() {
		for(int i = 0;i < this.sprites.length;i++) {
			SpriteRenderer spr = this.sprites[i];
			if(spr == null) continue;
			if(spr.isDirtyTexture()) this.loadTextureData(i);
			if(spr.isDirtyVertex()) loadVertexProperties(i);
			spr.setCleanTexture();
			spr.setCleanVertex();
			this.rebufferData = true;
		}
	}

	public void render() {

		GL30.glBindVertexArray(this.vaoID);

		if(this.rebufferData) rebuffer();

		this.shader.use();
		this.shader.uploadIntArray("uTextures", this.texSlots);
		this.shader.uploadMat4f("uProj", Game.get().getWorld().getViewport().getProjMatrix());
		this.shader.uploadMat4f("uView", Game.get().getWorld().getViewport().getViewMatrix());

		for(int i = 0;i < this.textures.size();i++) {
			GL30.glActiveTexture(GL30.GL_TEXTURE0 + i);
			this.textures.get(i).bind();
		}

		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		GL30.glEnableVertexAttribArray(2);
		GL30.glEnableVertexAttribArray(3);

		GL30.glDrawElements(GL30.GL_TRIANGLES, this.NUM_ELEMENTS*this.numSprites, GL30.GL_UNSIGNED_INT, 0);

		GL30.glDisableVertexAttribArray(0);
		GL30.glDisableVertexAttribArray(1);
		GL30.glDisableVertexAttribArray(2);
		GL30.glDisableVertexAttribArray(3);

		GL30.glBindVertexArray(0);

		for(Texture tex:this.textures) {
			tex.unbind();
		}

		this.shader.detach();
	}

	public void rebuffer() {
		sortIndices();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboID);
		GL30.glBufferSubData(GL30.GL_ARRAY_BUFFER, 0, vertices);
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, eboID);
		GL30.glBufferSubData(GL30.GL_ELEMENT_ARRAY_BUFFER, 0, indices);
		rebufferData = false;
	}

	public void addSprite(SpriteRenderer spr) {
		//update min and max y
		float sprY = spr.gameObject.transform.getPosition().y;
		if(sprY > this.maxY || this.numSprites == 0) this.maxY = sprY;
		if(sprY < this.minY || this.numSprites == 0) this.minY = sprY;

		//get index
		int index = -1;
		for(int i = 0;i < this.sprites.length;i++) {
			if(this.sprites[i] != null) continue;
			index = i;
			this.sprites[index] = spr;
			break;
		}

		//get texture
		if(spr.getTexture() != null && !textures.contains(spr.getTexture())) {
			textures.add(spr.getTexture());
		}

		this.numSprites++;
		loadTextureData(index);
		loadVertexProperties(index);
		//increment num sprites and update has room
		this.rebufferData = true;
//				System.out.println("Added to "+this+": "+spr.gameObject.getName());
	}

	public void removeSprite(SpriteRenderer spr) {
		if(spr == null) return;
		//check for sprite
		int sprIndex = -1;
		boolean usesTexture = false;
		float newMinY = this.minY;
		float newMaxY = this.maxY;
		//loop through all srpite
		for(int i = 0;i < this.sprites.length;i++) {
			//continue if null
			if(this.sprites[i] == null) continue;
			//if not sprite update min and max and check if texture if used
			if(this.sprites[i] != spr) {
				float sprY = this.parent.getSortHeuristic().heuristic(this.sprites[i].gameObject);
				if(this.sprites[i].getTexture() == spr.getTexture() && spr.getTexture() != null) usesTexture = true;
				if(sprY > newMaxY) newMaxY = sprY;
				if(sprY < newMinY) newMinY = sprY;

				continue;
			}
			//update sprite index if sprite
			sprIndex = i;
		}
		//if sprite doesnt exist leave
		if(sprIndex == -1) return;
		//update vertex data (possibly not nessesary)
		//		int verticesIndex = sprIndex * NUM_VERTICES * VERTEX_SIZE;
		//		for(int i = 0;i < NUM_VERTICES * VERTEX_SIZE;i++) {
		//			this.vertices[verticesIndex+i] = 0;
		//		}
		//update min and max
		this.maxY = newMaxY;
		this.minY = newMinY;
		//update textures and sprites
		if(!usesTexture && spr.getTexture() != null) this.textures.remove(spr.getTexture());
		this.sprites[sprIndex] = null;
		//update batch
		//decrement num sprites and update has room
		this.numSprites--;
		//resort
		this.sortIndices();
		this.rebufferData = true;
		//		System.out.println("Removed from "+this+": "+spr.gameObject.getName());
	}

	public RenderBatch split(float splitVal) {
		//		System.out.println("Split batch");
		RenderBatch renderBatch = new RenderBatch(this.maxBatchSize, this.zIndex, this.parent);
		renderBatch.start();
		List<SpriteRenderer>toSplit = new ArrayList<SpriteRenderer>();
		for(int i = 0;i < this.sprites.length;i++) {
			if(this.sprites[i] == null) continue;
			if(this.parent.getSortHeuristic().heuristic(this.sprites[i].gameObject) < splitVal) {
				toSplit.add(this.sprites[i]);
			}
		}
		for(SpriteRenderer spr:toSplit) {
			this.removeSprite(spr);
			renderBatch.addSprite(spr);
		}
		this.rebufferData = true;
		return renderBatch;
	}
	
	private void loadTextureData(int index) {
		SpriteRenderer sprite = this.sprites[index];
		
		assert sprite != null:"Loading null sprite texture";
		
		int offset = index * NUM_VERTICES * VERTEX_SIZE;
		int texID = updateTexture(sprite);
		//if texID == -2 stop further calc
		if(texID == -2) return;
		for(int i = 0;i < NUM_VERTICES;i++) {
			this.vertices[offset + 9] = texID;
			offset += this.VERTEX_SIZE;
		}
	}

	private void loadVertexProperties(int index) {
		SpriteRenderer sprite = this.sprites[index];

		assert sprite != null:"Loading null sprite vertices";

		int offset = index * NUM_VERTICES * VERTEX_SIZE;

		Vector4f color = sprite.getColor();
		Vector2f[] texCoords = sprite.getTexCoords();

		float xAdd = 1;
		float yAdd = 1;
		for(int i = 0;i < NUM_VERTICES;i++) {
			if (i == 1) {
				yAdd = 0;
			} else if (i == 2) {
				xAdd = 0;
			} else if (i == 3) {
				yAdd = 1;
			}

			double offx = sprite.gameObject.transform.getScale().x * xAdd - sprite.gameObject.transform.getRotPoint().x;
			double offy = sprite.gameObject.transform.getScale().y * yAdd - sprite.gameObject.transform.getRotPoint().y;
			double rotx = offx*Math.cos(sprite.gameObject.transform.getRot()) - offy*Math.sin(sprite.gameObject.transform.getRot());
			double roty = offx*Math.sin(sprite.gameObject.transform.getRot()) + offy*Math.cos(sprite.gameObject.transform.getRot());

			//incrementing offset as we go
			//last increment for next loop
			this.vertices[offset++] = sprite.gameObject.transform.getPosition().x + (float)rotx + sprite.gameObject.transform.getRotPoint().x;
			this.vertices[offset++] = sprite.gameObject.transform.getPosition().y + (float)roty + sprite.gameObject.transform.getRotPoint().y;
			this.vertices[offset++] = 0;

			this.vertices[offset++] = color.x;
			this.vertices[offset++] = color.y;
			this.vertices[offset++] = color.z;
			this.vertices[offset++] = color.w;

			this.vertices[offset++] = texCoords[i].x;
			this.vertices[offset++] = texCoords[i].y;
			
			offset++;
			//texture id
//			this.vertices[offset++] = ;
		}
	}

	private int updateTexture(SpriteRenderer sprite) {
		//if no texture return -1
		if(sprite.getTexture() == null) return -1;
		//if texture is not in textures already than theres a problem
		if(this.textures.contains(sprite.getTexture())) return textures.indexOf(sprite.getTexture());
		//add texture if there is space
		if(this.hasTextureRoom()) {
			this.textures.add(sprite.getTexture());
			return textures.indexOf(sprite.getTexture());
		}
		//if there is no room there is a bigger problem
		//remove sprite from this batch and add to other batch in renderer
		this.removeSprite(sprite);
		this.parent.add(sprite);
		System.out.println("Readd to another batch");
		return -2;
	}

	private void sortIndices() {
		if(isIndicesSorted()) return;
		//insertion sort
		for(int i = this.NUM_ELEMENTS;i < indices.length;i+=this.NUM_ELEMENTS) {
			int index = indices[i]/this.NUM_VERTICES;
			if(this.sprites[index] == null) continue;
			float sprY = this.parent.getSortHeuristic().heuristic(this.sprites[index].gameObject);
			int j = i - this.NUM_ELEMENTS;
			int key0 = this.indices[i+0], key1 = this.indices[i+1], key2 = this.indices[i+2], key3 = this.indices[i+3], key4 = this.indices[i+4], key5 = this.indices[i+5];
			while(j >= 0) {
				//set prevY if null default to neg inf
				float prevY = Float.NEGATIVE_INFINITY;
				if(this.sprites[indices[j]/this.NUM_VERTICES] != null)
					prevY = this.parent.getSortHeuristic().heuristic(this.sprites[indices[j]/this.NUM_VERTICES].gameObject);
				//if smaller reached end and exit
				if(sprY <= prevY) break;
				this.indices[j+this.NUM_ELEMENTS+0] = this.indices[j+0];
				this.indices[j+this.NUM_ELEMENTS+1] = this.indices[j+1];
				this.indices[j+this.NUM_ELEMENTS+2] = this.indices[j+2];
				this.indices[j+this.NUM_ELEMENTS+3] = this.indices[j+3];
				this.indices[j+this.NUM_ELEMENTS+4] = this.indices[j+4];
				this.indices[j+this.NUM_ELEMENTS+5] = this.indices[j+5];
				j-=this.NUM_ELEMENTS;
			}
			this.indices[j+this.NUM_ELEMENTS+0] = key0;
			this.indices[j+this.NUM_ELEMENTS+1] = key1;
			this.indices[j+this.NUM_ELEMENTS+2] = key2;
			this.indices[j+this.NUM_ELEMENTS+3] = key3;
			this.indices[j+this.NUM_ELEMENTS+4] = key4;
			this.indices[j+this.NUM_ELEMENTS+5] = key5;
		}
	}

	private boolean isIndicesSorted() {
		//Check if sorted
		float prevMax = Float.POSITIVE_INFINITY;
		for(int i = 0;i < indices.length;i+=this.NUM_ELEMENTS) {
			int index = indices[i]/this.NUM_VERTICES;
			if(this.sprites[index] == null) {
				prevMax = Float.NEGATIVE_INFINITY;
				continue;
			}
			if(this.parent.getSortHeuristic().heuristic(this.sprites[index].gameObject) > prevMax) return false;
			prevMax = this.parent.getSortHeuristic().heuristic(this.sprites[index].gameObject);
		}
		return true;
	}

	private int[] generateDefaultIndices() {
		// 6 indices per quad (3 per triangle)
		int[] elements = new int[6 * maxBatchSize];
		for (int i=0; i < maxBatchSize; i++) {
			loadElementIndices(elements, i);
		}

		return elements;
	}

	private void loadElementIndices(int[] elements, int index) {
		int offsetArrayIndex = NUM_ELEMENTS * index;
		int offset = NUM_VERTICES * index;

		// Triangle 1
		elements[offsetArrayIndex++] = offset + 3;
		elements[offsetArrayIndex++] = offset + 2;
		elements[offsetArrayIndex++] = offset + 0;

		// Triangle 2
		elements[offsetArrayIndex++] = offset + 0;
		elements[offsetArrayIndex++] = offset + 2;
		elements[offsetArrayIndex] = offset + 1;
	}

	//Getters

	public int getNumSprites() {
		return this.numSprites;
	}

	public boolean hasRoom() {
		return this.numSprites < maxBatchSize;
	}

	public boolean hasTextureRoom() {
		return this.textures.size() < this.maxTextures;
	}

	public boolean hasTexture(Texture texure) {
		return this.textures.contains(texure);
	}

	public int getzIndex() {
		return zIndex;
	}

	public float getMinY() {
		return minY;
	}

	public float getMaxY() {
		return maxY;
	}

	@Override
	public int compareTo(RenderBatch o) {
		float oAvg = (o.getMaxY()+o.minY)/2;
		float tAvg = (this.maxY+this.minY)/2;
		if(oAvg > tAvg) return 1;
		if(oAvg < tAvg) return -1;
		return 0;
	}

	public void print() {
		this.sortIndices();
		for(Texture tex:this.textures) {
			System.out.print("|"+tex.getPath());
		}
		System.out.println();
		for(int i = 0;i < this.sprites.length;i++) {
			System.out.print("|"+(this.sprites[i]!=null?this.sprites[i].gameObject.getObjName():"_____"));
		}
		System.out.println();
		for(int i = 0;i < this.sprites.length;i++) {
			System.out.print("|"+(this.sprites[i]!=null?this.parent.getSortHeuristic().heuristic(this.sprites[i].gameObject):"_____"));
		}
		System.out.println();
//		for(int i = 0;i < this.sprites.length;i++) {
//			System.out.print("(");
//			for(int j = 0;j < this.NUM_VERTICES;j++) {
//				System.out.print("((|"+this.vertices[i*this.NUM_VERTICES*this.VERTEX_SIZE+j*this.VERTEX_SIZE+0]);
//				System.out.print("|"+this.vertices[i*this.NUM_VERTICES*this.VERTEX_SIZE+j*this.VERTEX_SIZE+1]);
//				System.out.print("|"+this.vertices[i*this.NUM_VERTICES*this.VERTEX_SIZE+j*this.VERTEX_SIZE+2]+")");
//				System.out.print("(|"+this.vertices[i*this.NUM_VERTICES*this.VERTEX_SIZE+j*this.VERTEX_SIZE+3]);
//				System.out.print("|"+this.vertices[i*this.NUM_VERTICES*this.VERTEX_SIZE+j*this.VERTEX_SIZE+4]);
//				System.out.print("|"+this.vertices[i*this.NUM_VERTICES*this.VERTEX_SIZE+j*this.VERTEX_SIZE+5]);
//				System.out.print("|"+this.vertices[i*this.NUM_VERTICES*this.VERTEX_SIZE+j*this.VERTEX_SIZE+6]+")");
//				System.out.print("(|"+this.vertices[i*this.NUM_VERTICES*this.VERTEX_SIZE+j*this.VERTEX_SIZE+7]);
//				System.out.print("|"+this.vertices[i*this.NUM_VERTICES*this.VERTEX_SIZE+j*this.VERTEX_SIZE+8]+")");
//				System.out.print("(|"+this.vertices[i*this.NUM_VERTICES*this.VERTEX_SIZE+j*this.VERTEX_SIZE+9]+"))");
//			}
//			System.out.print(")");
//		}
//		System.out.println();
		for(int i = 0;i < this.indices.length;i+=this.NUM_ELEMENTS) {
			System.out.print("(|"+this.indices[i+0]);
			System.out.print("|"+this.indices[i+1]);
			System.out.print("|"+this.indices[i+2]);
			System.out.print("|"+this.indices[i+3]);
			System.out.print("|"+this.indices[i+4]);
			System.out.print("|"+this.indices[i+5]+")");
		}
		System.out.println();
		for(int i = 0;i < this.indices.length;i+=this.NUM_ELEMENTS) {
			System.out.print("|"+(this.sprites[this.indices[i]/this.NUM_VERTICES]!=null?this.sprites[this.indices[i]/this.NUM_VERTICES].gameObject.getObjName():"_____"));
		}
		System.out.println();
		System.out.println("|"+this.numSprites);
	}

}










