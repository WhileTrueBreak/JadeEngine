package game.world;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

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
    
    public void init() {
    	this.viewport = new Viewport(this.handler, new Vector2f());
    	this.renderer = new Renderer(this.viewport, new SortHeuristic() {
			public float heuristic(GameObject obj) {return obj.transform.getPosition().y;}
		});
    	this.uiLayer = new UiLayer(this.handler);
    	

    	GameObject go3 = new Rect("r1", new Transform(new Vector3f(0, 0, 0), new Vector2f(100, 100)));
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
    	
//    	UiComponent br = new UiBlock(new Vector4f(1, 1, 1, 1));
//    	br.setConstraint(UiConstraint.CENTER_X);
//    	br.setConstraint(UiConstraint.CENTER_Y);
//    	br.setConstraint(new RelativeConstraint(UiConstraint.T_W, 0.5f, UiConstraint.P_W));
//    	br.setConstraint(UiConstraint.HEIGHT_ASPECT(1.0f));
//    	
//    	UiComponent t = new UiBlock(new Vector4f(1, 0, 1, 1));
//    	t.setConstraint(UiConstraint.CENTER_X);
//    	t.setConstraint(new CompoundConstraint(UiConstraint.ALIGN_TOP, new AbsoluteConstraint(UiConstraint.T_Y, -10)));
//    	t.setConstraint(new RelativeConstraint(UiConstraint.T_W, 0.25f, UiConstraint.P_W));
//    	t.setConstraint(new RelativeConstraint(UiConstraint.T_H, 0.25f, UiConstraint.P_H));
//    	
//    	UiComponent b = new UiBlock(new Vector4f(1, 1, 0, 1));
//    	b.setConstraint(UiConstraint.CENTER_X);
//    	b.setConstraint(new CompoundConstraint(UiConstraint.ALIGN_BOTTOM, new AbsoluteConstraint(UiConstraint.T_Y, 10)));
//    	b.setConstraint(new RelativeConstraint(UiConstraint.T_W, 0.25f, UiConstraint.P_W));
//    	b.setConstraint(new RelativeConstraint(UiConstraint.T_H, 0.25f, UiConstraint.P_H));
//
//    	UiComponent l = new UiBlock(new Vector4f(0, 1, 1, 1));
//    	l.setConstraint(new CompoundConstraint(UiConstraint.ALIGN_LEFT, new AbsoluteConstraint(UiConstraint.T_X, 10)));
//    	l.setConstraint(UiConstraint.CENTER_Y);
//    	l.setConstraint(new RelativeConstraint(UiConstraint.T_W, 0.25f, UiConstraint.P_W));
//    	l.setConstraint(new RelativeConstraint(UiConstraint.T_H, 0.25f, UiConstraint.P_H));
//    	
//    	UiComponent r = new UiBlock(new Vector4f(1, 0, 0, 1));
//    	r.setConstraint(new CompoundConstraint(UiConstraint.ALIGN_RIGHT, new AbsoluteConstraint(UiConstraint.T_X, -10)));
//    	r.setConstraint(UiConstraint.CENTER_Y);
//    	r.setConstraint(new RelativeConstraint(UiConstraint.T_W, 0.25f, UiConstraint.P_W));
//    	r.setConstraint(new RelativeConstraint(UiConstraint.T_H, 0.25f, UiConstraint.P_H));
//    	
//    	br.addChild(t, b, l, r);
//    	uiLayer.getMaster().addChild(br);
    }
    
	public void update() {
//		this.viewport.focusOn(this.gameObjects.get(0));
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
