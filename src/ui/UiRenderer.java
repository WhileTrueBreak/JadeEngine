package ui;

import org.joml.Vector2f;
import org.joml.Vector4f;

import engine.gfx.Sprite;
import engine.gfx.Texture;
import ui.component.UiComponent;

public class UiRenderer {
	
	private UiComponent parent;
	
	private Vector4f color;
	private Sprite sprite;
	
	private boolean isDirtyTexture;
	private boolean isDirtyVertex;
	
	public UiRenderer(Vector4f color) {
		this.sprite = new Sprite(null);
		this.color = color;
		this.isDirtyTexture = true;
		this.isDirtyVertex = true;
	}
	
	public UiRenderer(Sprite sprite) {
		this.sprite = sprite;
		this.color = new Vector4f(1, 1, 1, 1);
		this.isDirtyTexture = true;
		this.isDirtyVertex = true;
	}
	
	public UiRenderer(Sprite sprite, Vector4f color) {
		this.sprite = sprite;
		this.color = color;
		this.isDirtyTexture = true;
		this.isDirtyVertex = true;
	}
    
    public Vector4f getColor() {
    	return color;
    }

	public Texture getTexture() {
		return this.sprite.getTexture();
	}

	public Vector2f[] getTexCoords() {
		return this.sprite.getTexCoords();
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
		this.isDirtyTexture = true;
	}

	public void setColor(Vector4f color) {
		if(this.color.equals(color)) return;
		this.color = color;
		this.isDirtyVertex = true;
	}

	public boolean isDirtyVertex() {
		return this.isDirtyVertex;
	}

	public boolean isDirtyTexture() {
		return this.isDirtyTexture;
	}

	public void setCleanVertex() {
		this.isDirtyVertex = false;
	}

	public void setCleanTexture() {
		this.isDirtyTexture = false;
	}

	public UiComponent getParent() {
		return parent;
	}

	public void setParent(UiComponent parent) {
		this.parent = parent;
	}
}
















