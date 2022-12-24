package game;

import game.world.World;
import jadeEngine.Program;
import jadeEngine.WindowHandler;
import jadeEngine.res.Assets;

public class Game extends Program{
	
	private World world;
	
	public Game() {}
	
	public void init() {
		Assets.init();
		world = new World(handler);
		world.init();
		world.start();
	}
	
	public void update() {
		world.update();
	}
	
	public World getWorld() {
		return world;
	}
	
}
