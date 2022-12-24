package jadeEngine;

public class WindowHandler {
	
	private Window window;
	private double currentFps;
	
	public WindowHandler(Window window) {
		this.window = window;
	}

	public int getWidth() {
		return window.getWidth();
	}

	public int getHeight() {
		return window.getHeight();
	}
	
	public String getDir() {
		return System.getProperty("user.dir");
	}

	public double getCurrentFps() {
		return 1/window.getDT();
	}
	
	public double getDT() {
		return window.getDT();
	}
	
	public Window getWindow() { 
		return this.window;
	}
	
}
