package game.world;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import game.entity.staticEntity.Rect;
import jadeEngine.WindowHandler;
import jadeEngine.display.Viewport;
import jadeEngine.gameobject.GameObject;
import jadeEngine.gameobject.SpriteRenderer;
import jadeEngine.gfx.Sprite;
import jadeEngine.renderer.Renderer;
import jadeEngine.renderer.SortHeuristic;
import jadeEngine.renderer.Transform;
import jadeEngine.res.Assets;
import jadeEngine.ui.UiLayer;
import jadeEngine.ui.component.UiBlock;
import jadeEngine.ui.component.UiComponent;
import jadeEngine.ui.constraint.UiConstraint;

public class World {
	
	private WindowHandler handler;
	private Viewport viewport;
	
	private Renderer renderer;
	
	private UiLayer uiLayer;
	
    protected List<GameObject> gameObjects = new ArrayList<GameObject>();

    private boolean isRunning = false;
    
	public World(WindowHandler handler) {
		this.handler = handler;
	}
	
	Vector3f dir = new Vector3f(100, 100, 0);
	GameObject go3;
    
    public void init() {
    	this.viewport = new Viewport(this.handler, new Vector2f());
    	this.renderer = new Renderer(this.viewport, new SortHeuristic() {
			public float heuristic(GameObject obj) {return obj.transform.getPosition().y;}
		});
    	this.uiLayer = new UiLayer(this.handler);
    	

    	go3 = new Rect("r1", new Transform(new Vector3f(0, 0, 0), new Vector2f(100, 100)));
    	go3.setSpriteRenderer(new SpriteRenderer(new Sprite(Assets.getTexture(Assets.T_GREEN_ALPHA))));
    	this.addGameObject(go3);
    	GameObject go1 = new Rect("r1", new Transform(new Vector3f(1820, 0, 0), new Vector2f(100, 100)));
    	go1.setSpriteRenderer(new SpriteRenderer(new Sprite(Assets.getTexture(Assets.T_GREEN_ALPHA))));
    	this.addGameObject(go1);
    	GameObject go2 = new Rect("r1", new Transform(new Vector3f(0, 980, 0), new Vector2f(100, 100)));
    	go2.setSpriteRenderer(new SpriteRenderer(new Sprite(Assets.getTexture(Assets.T_GREEN_ALPHA))));
    	this.addGameObject(go2);
    	GameObject go4 = new Rect("r1", new Transform(new Vector3f(1820, 980, 0), new Vector2f(100, 100)));
    	go4.setSpriteRenderer(new SpriteRenderer(new Sprite(Assets.getTexture(Assets.T_GREEN_ALPHA))));
    	this.addGameObject(go4);
    	GameObject go5 = new Rect("r1", new Transform(new Vector3f(0, 0, 0), new Vector2f(1920, 1080)));
    	go5.setSpriteRenderer(new SpriteRenderer(new Sprite(Assets.getTexture(Assets.T_RED_ALPHA))));
    	this.addGameObject(go5);
    	
    	UiComponent br = new UiBlock(new Vector4f(1, 1, 1, 1));
    	br.setConstraint(UiConstraint.CENTER_X);
    	br.setConstraint(UiConstraint.CENTER_Y);
    	br.setConstraint(UiConstraint.ABSOLUTE(UiConstraint.T_W, 2));
    	br.setConstraint(UiConstraint.HEIGHT_ASPECT(1.0f));
    	
    	uiLayer.getMaster().addChild(br);
    }
    
	public void update() {
		Vector3f pos = go3.transform.getPosition();
		pos.add(new Vector3f(dir.x*(float)this.handler.getDT(), dir.y*(float)this.handler.getDT(), 0));
		
		this.viewport.focusOn(go3);
		this.gameObjects.forEach(go -> go.updateAll());
        this.uiLayer.update();
        this.renderer.render();
        this.uiLayer.render();
	}
    
    public void start() {
		this.gameObjects.forEach(go -> {go.start();this.renderer.add(go);});
        isRunning = true;
    }

    public void addGameObject(GameObject go) {
        gameObjects.add(go);
        if (!isRunning) return;
        go.start();
        this.renderer.add(go);
    }
	
	public Viewport getViewport() {
		return viewport;
	}
	
}
