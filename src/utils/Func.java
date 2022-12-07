package utils;

public class Func {
	public static double dist(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
	}
	public static double radians(double deg) {
		return deg*Math.PI/180;
	}
	public static double degrees(double rad) {
		return rad*180/Math.PI;
	}
	public static double map(double in, double in_min, double in_max, double min, double max) {
		if(in <= in_min) return min;
		if(in >= in_max) return max;
		return (in-in_min)/(in_max-in_min)*(max-min)+min;
	}
	public static double random(double min, double max) {
		double out = Math.random() * (max - min);
		return out + min;
	}
}
