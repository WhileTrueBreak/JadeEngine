package engine.gameobject;

import org.joml.Vector2f;
import org.joml.Vector4f;

import engine.gfx.Sprite;
import engine.gfx.Texture;
import engine.renderer.Transform;

public class SpriteRenderer{
	
    public GameObject gameObject = null;
	
	private Vector4f color;
	private Sprite sprite;
	
	private Transform lastTranform;
	private boolean isDirtyTexture;
	private boolean isDirtyVertex;
	
	public SpriteRenderer(Vector4f color) {
		this.sprite = new Sprite(null);
		this.color = color;
		this.isDirtyTexture = true;
		this.isDirtyVertex = true;
	}
	
	public SpriteRenderer(Sprite sprite) {
		this.sprite = sprite;
		this.color = new Vector4f(1, 1, 1, 1);
		this.isDirtyTexture = true;
		this.isDirtyVertex = true;
	}
	
	public SpriteRenderer(Sprite sprite, Vector4f color) {
		this.sprite = sprite;
		this.color = color;
		this.isDirtyTexture = true;
		this.isDirtyVertex = true;
	}

    public void start() {
    	this.lastTranform = this.gameObject.transform.copy();
    }

    public void update() {
    	if(!this.lastTranform.equals(this.gameObject.transform)) {
    		this.gameObject.transform.copy(this.lastTranform);
    		this.isDirtyVertex = true;
    	}
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
}
















