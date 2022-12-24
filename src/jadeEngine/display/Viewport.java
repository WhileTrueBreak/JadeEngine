package jadeEngine.display;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import jadeEngine.WindowHandler;
import jadeEngine.gameobject.GameObject;

public class Viewport {

	private final static int DEFAULT_DIM_WIDTH = 1920;
	private final static int DEFAULT_DIM_HEIGHT = 1080;
	private final static float DEFAULT_DIM_RATIO = DEFAULT_DIM_WIDTH/(float)DEFAULT_DIM_HEIGHT;
	
	private float scale;
	private float windowWidth, windowHeight;
	
	private Matrix4f projMatrix, viewMatrix;
	public Vector2f position;
	public Vector2f offset;
	
	private WindowHandler handler;
	
	public Viewport(WindowHandler handler, Vector2f position) {
		this.handler = handler;
		
		this.position = position;
		this.offset = new Vector2f();
		this.projMatrix = new Matrix4f();
		this.viewMatrix = new Matrix4f();
		this.scale = 1;
		adjustProjection();
	}
	
	public void adjustProjection() {
		this.projMatrix.identity();
		float scale;
		float cRatio = this.handler.getWidth()/(float)this.handler.getHeight();
		if(cRatio < Viewport.DEFAULT_DIM_RATIO) {
			scale = Viewport.DEFAULT_DIM_WIDTH/(float)this.handler.getWidth();
			this.windowWidth = Viewport.DEFAULT_DIM_WIDTH;
			this.windowHeight = this.windowWidth/cRatio;
		} else {
			scale = Viewport.DEFAULT_DIM_HEIGHT/(float)this.handler.getHeight();
			this.windowHeight = Viewport.DEFAULT_DIM_HEIGHT;
			this.windowWidth = this.windowHeight*cRatio;
		}
		float newWidth = this.handler.getWidth()*scale;
		float newHeight = this.handler.getHeight()*scale;
		
		float yOffset = (this.windowHeight - DEFAULT_DIM_HEIGHT)/2;
		float xOffset = (this.windowWidth - DEFAULT_DIM_WIDTH)/2;

		this.projMatrix.ortho(-xOffset, newWidth - xOffset, -yOffset, newHeight - yOffset, 0, 100);
	}
	
	public Matrix4f getProjMatrix() {
		adjustProjection();
		return this.projMatrix;
	}

	public Matrix4f getViewMatrix() {
		Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
		Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
		this.viewMatrix.identity();
		this.viewMatrix = viewMatrix.lookAt(new Vector3f((int)position.x, (int)position.y, 20.0f), 
				cameraFront.add(new Vector3f((int)position.x, (int)position.y, 0.0f)), cameraUp);
		return this.viewMatrix;
	}
	
	public void focusOn(GameObject obj) {
		Vector2f objCenter = new Vector2f(obj.transform.getPosition().x+obj.transform.getScale().x/2, obj.transform.getPosition().y+obj.transform.getScale().y/2);
		this.position.x = objCenter.x-DEFAULT_DIM_WIDTH/2;
		this.position.y = objCenter.y-DEFAULT_DIM_HEIGHT/2;
	}
	
	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getWindowWidth() {
		return windowWidth;
	}

	public void setWindowWidth(float windowWidth) {
		this.windowWidth = windowWidth;
	}

	public float getWindowHeight() {
		return windowHeight;
	}

	public void setWindowHeight(float windowHeight) {
		this.windowHeight = windowHeight;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	
//	private Handler handler;
//	
//	//relative to top left (0,0)
//	private double xoff, yoff;
//	private double rot;
//	private double scale;
//	
//	private double PoFx, PoFy;
//	
//	public Viewport(Handler handler, double xoff, double yoff) {
//		this.handler = handler;
//		this.xoff = xoff;
//		this.yoff = yoff;
//		this.scale = 1;
//		this.rot = 0;
//	}
//	
//	public void focusOnEntity(Entity e, double spring) {
//		double cameraSpring = spring;
//		PoFx = (e.getX()+(float)e.getHitbox().getBoundingRect().getWidth()/2)*scale - handler.getWidth()/2;
//		PoFy = (e.getY()+(float)e.getHitbox().getBoundingRect().getHeight()/2)*scale - handler.getHeight()/2;
//		if(cameraSpring <= 0)
//			move((PoFx-xoff), (PoFy-yoff));
//		else {
//			move(((PoFx-xoff)/cameraSpring)/(float)handler.getCurrentFps(), 
//					((PoFy-yoff)/cameraSpring)/(float)handler.getCurrentFps());
//		}
//	}
//	
//	public void focusOnPoint(double x, double y, double spring) {
//		double cameraSpring = spring;
//		PoFx = x*scale - handler.getWidth()/2;
//		PoFy = y*scale - handler.getHeight()/2;
//		if(cameraSpring <= 0)
//			move((PoFx-xoff), (PoFy-yoff));
//		else
//			move(((PoFx-xoff)/cameraSpring)/(float)handler.getCurrentFps(), 
//					((PoFy-yoff)/cameraSpring)/(float)handler.getCurrentFps());
//	}
//	
//	public void move(double amtx, double amty) {
//		xoff += amtx;
//		yoff += amty;
//	}
//
//	public double getXoff() {
//		double dx = xoff-PoFx;
//		double dy = yoff-PoFy;
//		return dx*Math.cos(rot)-dy*Math.sin(rot)+PoFx;
//	}
//
//	public void setXoff(int xoff) {
//		this.xoff = xoff;
//	}
//
//	public double getYoff() {
//		double dx = xoff-PoFx;
//		double dy = yoff-PoFy;
//		return dx*Math.sin(rot)+dy*Math.cos(rot)+PoFy;
//	}
//
//	public void setYoff(int yoff) {
//		this.yoff = yoff;
//	}
//	
//	public double getScale() {
//		return scale;
//	}
//	
//	public void setScale(double scale) {
//		this.scale = scale;
//	}
//
//	public double getRot() {
//		return rot;
//	}
//
//	public void setRot(float rot) {
//		this.rot = rot;
//	}
	
}
