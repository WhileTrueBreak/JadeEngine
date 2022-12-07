package utils;

import java.awt.geom.Rectangle2D;

public class Polygon {
	
	private Vector[] vertices;
	private Rectangle2D boundingRect;
	
	private int vertexCount;

	public Polygon(Vector[] vertices) {
		this.vertices = vertices;
		this.vertexCount = vertices.length;
		double minx = Float.POSITIVE_INFINITY, miny = Float.POSITIVE_INFINITY;
		double maxx = Float.NEGATIVE_INFINITY, maxy = Float.NEGATIVE_INFINITY;
		for(Vector vertex:vertices) {
			double vx = vertex.getX();
			double vy = vertex.getY();
			if(minx>vx) {
				minx = vx;
			}if(maxx<vx) {
				maxx = vx;
			}if(miny>vy) {
				miny = vy;
			}if(maxy<vy) {
				maxy = vy;
			}
		}
		boundingRect = new Rectangle2D.Double(minx, miny, maxx-minx, maxy-miny);
	}

	public Vector[] getVertices() {
		return vertices;
	}
	
	public Vector[] copyVertices() {
		Vector[] copy = new Vector[vertices.length];
		for(int i = 0;i < vertices.length;i++) {
			copy[i] = new Vector(vertices[i].getX(), vertices[i].getY());
		}
		return copy;
	}
	
	public Rectangle2D getBoundingRect(){
		return boundingRect;
	}
	
	public int getVertexCount() {
		return vertexCount;
	}
	
}
