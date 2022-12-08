package jadeEngine.inputs;

import org.lwjgl.glfw.GLFW;

public class MouseListener {
	private static MouseListener mouseListener;
	
	public static MouseListener get() {
		if(MouseListener.mouseListener == null) {
			MouseListener.mouseListener = new MouseListener();
		}
		return MouseListener.mouseListener;
	}
	
	private double scrollX, scrollY;
	private double mouseX, mouseY, mouseLastX, mouseLastY;
	private boolean mouseButtonPressed[] = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST+1];
	private boolean isDragging;
	
	private MouseListener() {
		this.scrollX = 0;
		this.scrollY = 0;
		this.mouseX = 0;
		this.mouseY = 0;
		this.mouseLastX = 0;
		this.mouseLastY = 0;
	}
	
	public static void mousePosCallback(long window, double xPos, double yPos) {
		get().mouseLastX = get().mouseX;
		get().mouseLastY = get().mouseY;
		
		get().mouseX = xPos;
		get().mouseY = yPos;
		
		for(int i = 0;i < get().mouseButtonPressed.length;i++) get().isDragging = get().isDragging || get().mouseButtonPressed[i];
	}
	
	public static void mouseButtonCallback(long window, int button, int action, int mods) {
		if(action == GLFW.GLFW_PRESS) {
			if(button < get().mouseButtonPressed.length) {
				get().mouseButtonPressed[button] = true;
			}
		}else if(action == GLFW.GLFW_RELEASE){
			if(button < get().mouseButtonPressed.length) {
				get().mouseButtonPressed[button] = false;
				get().isDragging = false;
			}
		}
	}
	
	public static void mouseScrollCallback(long window, double xoffset, double yoffset) {
		get().scrollX = xoffset;
		get().scrollY = yoffset;
	}
	
	public static void endFrame() {
		get().scrollX = 0;
		get().scrollY = 0;
		get().mouseLastX = get().mouseX;
		get().mouseLastY = get().mouseY;
	}

	public static double getMouseX() {
		return get().mouseX;
	}
	
	public static double getMouseY() {
		return get().mouseY;
	}
	
	public static double getMouseDX() {
		return get().mouseX-get().mouseLastX;
	}
	
	public static double getMouseDY() {
		return get().mouseY-get().mouseLastY;
	}
	
	public static double getScrollX() {
		return get().scrollX;
	}
	
	public static double getScrollY() {
		return get().scrollY;
	}
	
	public static boolean isDragging() {
		return get().isDragging;
	}
	
	public static boolean mouseButtonDown(int button) {
		if(button >= get().mouseButtonPressed.length) return false;
		return get().mouseButtonPressed[button];
	}
	
}














