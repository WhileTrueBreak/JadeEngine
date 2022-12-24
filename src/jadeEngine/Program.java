package jadeEngine;

public abstract class Program {
	
	//handler
	protected WindowHandler handler;
	
	public Program() {}
	
	public abstract void init();
	
	public abstract void update();
	
	public WindowHandler getWindowHandler() {
		return handler;
	}
	
	public void setWindowHandler(WindowHandler handler) {
		this.handler = handler;
	}
	
}
