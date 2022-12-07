package utils;

public class LinearParametricPoint {
	
	private Vector pos, vel;
	
	public LinearParametricPoint(Vector pos, Vector vel) {
		this.pos = pos;
		this.vel = vel;
	}
	
	public Vector getPos(float t) {
		return Vector.add(pos, Vector.mult(vel, t));
	}
	
	public Vector getVel() {
		return vel;
	}
	
}
