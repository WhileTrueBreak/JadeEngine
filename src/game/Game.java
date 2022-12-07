package game;

import game.res.Assets;
import game.world.World;

public class Game {
	
	private static Game game;
	
	public static Game get() {
		if(Game.game == null) {
			Game.game = new Game();
		}
		return Game.game;
	}
	
	//handler
	private Handler handler;
	//world 
	private World world;
	
	public Game() {}
	
	public void init() {
		Assets.init();
		
		handler = new Handler();
		world = new World(handler);
		world.init();
		world.start();
	}
	
	public void update() {
		world.update();
	}
	
	public Handler getHandler() {
		return handler;
	}
	
	public World getWorld() {
		return world;
	}
	
}
