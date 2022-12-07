package engine.inputs;

import org.lwjgl.glfw.GLFW;

public class KeyListener {
	
	private static KeyListener keyListener;
	
	public static KeyListener get() {
		if(KeyListener.keyListener == null) {
			KeyListener.keyListener = new KeyListener();
		}
		return KeyListener.keyListener;
	}
	
	private boolean keyPressed[] = new boolean[GLFW.GLFW_KEY_LAST+1];
	
	private KeyListener() {
		
	}
	
	public static void keyCallback(long window, int key, int scancode, int action, int mods) {
		if(key < 0 || key >= get().keyPressed.length) {
			System.err.println("Unknown key "+key+" pressed");
			return;
		}
		if(action == GLFW.GLFW_PRESS) {
			get().keyPressed[key] = true;
		}else if(action == GLFW.GLFW_RELEASE) {
			get().keyPressed[key] = false;
		}
	}
	
	public static boolean isKeyPressed(int key) {
		return get().keyPressed[key];
	}
	
}