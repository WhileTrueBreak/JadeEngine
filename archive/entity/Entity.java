package game.entity;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import game.Handler;
import utils.Polygon;

public abstract class Entity {
	protected Handler handler;
	
	protected double x, y;
	protected double width, height;
	protected Polygon hitbox;
	protected Rectangle2D drawbox;
	protected int zIndex;
	
	public Entity(double x, double y, Handler handler) {
		this.handler = handler;
		this.x = x;
		this.y = y;
		this.zIndex = 0;
	}

	public abstract void render(Graphics g);
	public abstract void update();

	public double getWidth() {
		return width;
	}
	
	public double getHeight() {
		return height;
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public Polygon getHitbox() {
		return hitbox;
	}
	
	public void setHitbox(Polygon hitbox) {
		this.hitbox = hitbox;
		if(drawbox == null) {
			this.drawbox = this.hitbox.getBoundingRect();
		}
	}
	
	public Rectangle2D getDrawbox() {
		return drawbox;
	}

	public void setDrawbox(Rectangle2D drawbox) {
		this.drawbox = drawbox;
	}

	public boolean isExpired() {
		return false;
	}

	public int getZIndex() {
		return zIndex;
	}

	public void setZIndex(int zIndex) {
		if(zIndex < -999 || zIndex > 999) {
			new Exception("zIndex "+zIndex+" out of bounds").printStackTrace();
			return;
		}
		this.zIndex = zIndex;
	}
	
}
