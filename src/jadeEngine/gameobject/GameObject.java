package jadeEngine.gameobject;

import jadeEngine.renderer.Transform;

public abstract class GameObject {

    protected String objName;
    protected SpriteRenderer spriteRenderer;
    public Transform transform;

    public GameObject(String name) {
        this.objName = name;
        this.transform = new Transform();
    }
    
    public GameObject(String name, Transform transform) {
        this.objName = name;
        this.transform = transform;
    }

    public void updateAll() {
    	update();
    	this.spriteRenderer.update();
    }
    
    public abstract void update();
    
    public void start() {
    	this.spriteRenderer.start();
    	
    }
    
    public String getObjName() {
    	return objName;
    }

	public SpriteRenderer getSpriteRenderer() {
		return spriteRenderer;
	}

	public void setSpriteRenderer(SpriteRenderer spriteRenderer) {
		this.spriteRenderer = spriteRenderer;
		this.spriteRenderer.gameObject = this;
	}
    
}
