package game;

import jadeEngine.Window;

public class Launcher {

	public static void main(String[] args) {
		Window.get(600, 600, "Title", new Game()).run();
	}

}
