package jadeEngine.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jadeEngine.Window;
import jadeEngine.WindowHandler;
import jadeEngine.ui.component.UiComponent;
import jadeEngine.ui.component.UiWrapper;
import jadeEngine.ui.constraint.UiConstraint;

public class UiLayer {
	
	/*
	 * IDEAS
	 * 
	 * create an list of components to compare to
	 * when using renderer only use array but compare to list when setting indices
	 * 
	 * easy to remove and add accordingly
	 * still have to maintain order by shuffling the list and arrays
	 * master list with all comps and split to renderers if there is updates
	 * updated renderer with new list
	 * only need to change if sprite doesnt exist otherwise update indices
	 * 
	 * only when master list is changed split to renderers
	 * how to maintain master list?
	 * 
	 * when appending child to parent set flag for change
	 * propagate change to master comp
	 * check if flag set for master
	 * no need for flags if parent exists
	 */
	
	private List<UiComponent>masterList;
	private List<UiBatchRenderer>batchRenderers;
	private UiComponent master;
	
	private boolean hasMasterListChanged;
	
	private WindowHandler handler;
	
	public UiLayer(WindowHandler handler) {
		this.handler = handler;
		
		this.batchRenderers = new ArrayList<UiBatchRenderer>();
		this.masterList = new ArrayList<UiComponent>();
		
		this.master = new UiWrapper();
		this.master.setHandler(handler);
		this.master.setConstraint(UiConstraint.ABSOLUTE(UiConstraint.T_X, 0));
		this.master.setConstraint(UiConstraint.ABSOLUTE(UiConstraint.T_Y, 0));
		this.master.setConstraint(UiConstraint.RELATIVE(UiConstraint.T_W, 1f, UiConstraint.P_W));
		this.master.setConstraint(UiConstraint.RELATIVE(UiConstraint.T_H, 1f, UiConstraint.P_H));
		this.master.updateTransform();
		
		this.updateMasterList();
		this.updateRenderers();
	}
	
	private void updateMasterList() {
		//create list of ordered uicomponents
		masterList = new ArrayList<UiComponent>();
		List<UiComponent>queue = new ArrayList<UiComponent>(Arrays.asList(master));
		while(queue.size() != 0) {
			masterList.add(queue.get(0));
			queue.addAll(queue.get(0).getChildren());
			queue.remove(0);
		}
		this.master.setCleanComponentList();
		this.hasMasterListChanged = true;
	}
	
	public void updateRenderers() {
		this.batchRenderers = new ArrayList<UiBatchRenderer>();
		UiBatchRenderer batchRenderer = new UiBatchRenderer(this.handler, this.masterList.size());
		this.batchRenderers.add(batchRenderer);
		batchRenderer.start();
		for(int i = 0;i < this.masterList.size();i++) {
			if(this.masterList.get(i).getUiRenderer() == null) continue;
			if(!batchRenderer.hasRoom()) {
				if(		!batchRenderer.hasTextureRoom() && 
						!batchRenderer.hasTexture(this.masterList.get(i).getUiRenderer().getTexture()) && 
						this.masterList.get(i).getUiRenderer().getTexture() != null) {
					batchRenderer = new UiBatchRenderer(this.handler, this.masterList.size());
					this.batchRenderers.add(batchRenderer);
					batchRenderer.start();
				}
			}
			batchRenderer.addUiRenderer(this.masterList.get(i).getUiRenderer());
		}
		this.hasMasterListChanged = false;
	}
	
	public void update() {
		if(Window.hasResized()) this.master.updateTransform();
		if(this.master.isDirtyComponentList()) this.updateMasterList();
		if(this.hasMasterListChanged) this.updateRenderers();
		for(UiComponent component:masterList) {
			component.actions();
			component.update();
		}
	}
	
	public void render() {
		batchRenderers.forEach(br -> br.render());
	}

	public UiComponent getMaster() {
		return master;
	}
	
}
