package utils;

public class EventTimer {
	
	private double time;
	
	public EventTimer() {
		time = 0;
	}
	
	public void reset() {
		time = 0;
	}
	
	public void step(double step) {
		time += step;
	}
	
	public double getTime() {
		return time;
	}
	
}
