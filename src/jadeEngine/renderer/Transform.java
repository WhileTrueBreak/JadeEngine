package jadeEngine.renderer;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Transform {

	private Vector3f position;
	private Vector2f scale;
	private Vector2f rotPoint;
	private double rot;

	public Transform() {
		init(new Vector3f(), new Vector2f(), new Vector2f(), 0);
	}
	
	public Transform(Vector3f position) {
		init(position, new Vector2f(), new Vector2f(), 0);
	}
	
	public Transform(Vector3f position, Vector2f scale) {
		init(position, scale, new Vector2f(), 0);
	}
	
	public Transform(Vector3f position, Vector2f scale, Vector2f rotPoint, double rot) {
		init(position, scale, rotPoint, rot);
	}
	
	public void init(Vector3f position, Vector2f scale, Vector2f rotPoint, double rot) {
		this.position = position;
		this.scale = scale;
		this.rotPoint = rotPoint;
		this.rot = rot;
	}
	
	public Transform copy() {
		return new Transform(new Vector3f(this.position.x, this.position.y, this.position.x), new Vector2f(this.scale.x, this.scale.y), new Vector2f(this.rotPoint.x, this.rotPoint.y), this.rot);
	}
	
	public void copy(Transform to) {
		to.position.set(this.position);
		to.scale.set(this.scale);
		to.rotPoint.set(this.rotPoint);
		to.rot = this.rot;
	}
	
	public boolean equals(Object o) {
		if(o == null) return false;
		if(!(o instanceof Transform)) return false;
		Transform t = (Transform) o;
		return t.position.equals(this.position)&&t.scale.equals(this.scale)&&t.rotPoint.equals(this.rotPoint)&&t.rot==this.rot;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector2f getScale() {
		return scale;
	}

	public void setScale(Vector2f scale) {
		this.scale = scale;
	}

	public Vector2f getRotPoint() {
		return rotPoint;
	}

	public void setRotPoint(Vector2f rotPoint) {
		this.rotPoint = rotPoint;
	}

	public double getRot() {
		return rot;
	}

	public void setRot(double rot) {
		this.rot = rot;
	}
	
	//ranges from -32767 to 32768
	public int getZIndex() {
		int index = (int)Math.floor(this.position.z);
		if(index < -32767 || index > 32768) {
			assert false:"zIndex out of bounds (-32767 to 32768)";
			return 0;
		}
		return index;
	}
	
}
