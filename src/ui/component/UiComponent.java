package ui.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import engine.Window;
import engine.inputs.MouseListener;
import engine.renderer.Transform;
import ui.Callback;
import ui.UiRenderer;
import ui.constraint.UiConstraint;

public abstract class UiComponent {

	public static final int UI_X = 0;
	public static final int UI_Y = 1;
	public static final int UI_W = 2;
	public static final int UI_H = 3;
	public static final int UI_OFF_X = 4;
	public static final int UI_OFF_Y = 5;
	public static final int UI_OFF_W = 6;
	public static final int UI_OFF_H = 7;
	
	protected List<UiComponent>children;
	public UiComponent parent;
	
	protected Callback hoverCallback, dragCallback, clickCallBack;
	
	protected UiRenderer uiRenderer;
	public Transform transform;
	public Vector2f offset;

	protected boolean isDirtyVertexData;
	protected boolean isDirtyComponentList;
	
	protected UiConstraint[] constraints;
	
	public UiComponent() {
		this.children = new ArrayList<UiComponent>();
		this.parent = null;

		this.offset = new Vector2f(0, 0);
		this.transform = new Transform();
		this.constraints = new UiConstraint[4];
		
		this.hoverCallback = null;
		this.dragCallback = null;
		this.clickCallBack = null;

		this.uiRenderer = null;
		
		this.isDirtyVertexData = true;
		this.isDirtyComponentList = true;
	}
	
	public abstract void update();
	
	public void updateTransform() {
		float[] transforms = {0, 0, Window.getWidth(), Window.getHeight(), 0, 0, 0, 0};
		if(this.parent != null) {
			this.offset.x = this.parent.transform.getPosition().x + this.parent.offset.x;
			this.offset.y = this.parent.transform.getPosition().y + this.parent.offset.y;
			transforms[UiConstraint.P_X] = this.parent.transform.getPosition().x;
			transforms[UiConstraint.P_Y] = this.parent.transform.getPosition().y;
			transforms[UiConstraint.P_W] = this.parent.transform.getScale().x;
			transforms[UiConstraint.P_H] = this.parent.transform.getScale().y;
		}
		boolean[] isSet = {true, true, true, true, false, false, false, false};
		List<UiConstraint>todo = new ArrayList<UiConstraint>(Arrays.asList(this.constraints));
		while(todo.size() != 0) {
			UiConstraint current = null;
			for(UiConstraint c:todo) {
				assert c != null:"Missing dimension in constraints";
				if(!c.validTransform(isSet)) continue;
				current = c;
				break;
			}
			assert current != null:"ui constraints cant be solved";
			current.adjustTransform(transforms, isSet);
			todo.remove(current);
		}
		this.transform.setPosition(new Vector3f(transforms[UiConstraint.T_X], transforms[UiConstraint.T_Y], 0));
		this.transform.setScale(new Vector2f(transforms[UiConstraint.T_W], transforms[UiConstraint.T_H]));
		this.children.forEach(comp -> comp.updateTransform());
	}
	
	public void actions() {
		double mx = MouseListener.getMouseX(), my = MouseListener.getMouseY();
		my = Window.getHeight() - my;
		if(		mx >= this.transform.getPosition().x && 
				my >= this.transform.getPosition().y && 
				mx <= this.transform.getPosition().x+this.transform.getScale().x && 
				my <= this.transform.getPosition().y+this.transform.getScale().y) {	
			this.onHover(hoverCallback);
			if(MouseListener.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
				this.onClick(clickCallBack);
			}
		}else {
			onDefault();
		}
	}

	public abstract void onDefault();
	public abstract void onHover(Callback callback);
	public abstract void onClick(Callback callback);

	public void addChild(UiComponent child) {
		if(child.parent != null) return;
		if(this.children.contains(child)) return;
		this.setDirtyComponentList();
		this.children.add(child);
		child.parent = this;
		child.updateTransform();
	}
	
	public void addChild(UiComponent ...childs) {
		for(UiComponent child:childs) {
			addChild(child);
		}
	}
	
	public void removeChild(UiComponent child) {
		if(child.parent != this) return;
		if(!this.children.contains(child)) return;
		this.setDirtyComponentList();
		this.children.remove(child);
		child.parent = null;
	}

	public void setHoverCallback(Callback hoverCallback) {
		this.hoverCallback = hoverCallback;
	}

	public void setDragCallback(Callback dragCallback) {
		this.dragCallback = dragCallback;
	}

	public void setClickCallBack(Callback clickCallBack) {
		this.clickCallBack = clickCallBack;
	}

	public List<UiComponent> getChildren() {
		return children;
	}

	public UiRenderer getUiRenderer() {
		return uiRenderer;
	}

	public void setUiRenderer(UiRenderer uiRenderer) {
		this.uiRenderer = uiRenderer;
	}
	
	public boolean isDirtyVertexData() {
		return this.isDirtyVertexData;
	}
	
	public void setCleanVertexData() {
		this.isDirtyVertexData = false;
	}

	public boolean isDirtyComponentList() {
		return isDirtyComponentList;
	}

	public void setCleanComponentList() {
		this.isDirtyComponentList = false;
	}

	protected void setDirtyVertexData() {
		this.isDirtyVertexData = true;
		if(this.parent == null) return;
		this.parent.setDirtyVertexData();
	}

	public void setDirtyComponentList() {
		this.isDirtyComponentList = true;
		if(this.parent == null) return;
		this.parent.setDirtyComponentList();
	}
	
	public void setConstraint(UiConstraint constraint) {
		this.constraints[constraint.toChange - 4] = constraint;
	}
	
	public UiConstraint getConstraint(int index) {
		return this.constraints[index];
	}
	
}
