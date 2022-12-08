package game;

import game.world.World;
import jadeEngine.Window;
import jadeEngine.display.Viewport;

public class Handler {
	
	private Viewport viewport;
	private World world;
	private double currentFps;
	
	public Handler() {
	}

	public int getWidth() {
		return Window.getWidth();
	}

	public int getHeight() {
		return Window.getHeight();
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public Viewport getViewport() {
		return viewport;
	}
	
	public void setViewport(Viewport viewport) {
		this.viewport = viewport;
	}
	
	public String getDir() {
		return System.getProperty("user.dir");
	}

	public double getCurrentFps() {
		return 1/Window.get().getDT();
	}
	
	public double getDT() {
		return 1/currentFps;
	}
	
}
