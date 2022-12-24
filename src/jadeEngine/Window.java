package jadeEngine;

import java.nio.IntBuffer;

import org.joml.Vector2i;
import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import jadeEngine.inputs.KeyListener;
import jadeEngine.inputs.MouseListener;
import utils.Logging;

public class Window {
	
	private static Window window = null; 
	
	public static Window get(int width, int height, String title, Program program) {
		if(Window.window == null) {
			Window.window = new Window(width, height, title, program);
		}
		return Window.window;
	}
	
	public static Window get() {
		if(Window.window == null) {
			return null;
		}
		return Window.window;
	}
	
	public static boolean hasResized() {
		return Window.window.resized;
	}
	
	public static Vector2i getWindowDim() {
		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetWindowSize(get().glfwWindow, w, h);
		return new Vector2i(w.get(0), h.get(0));
	}
	
	private int width, height;
	private String title;
	private long glfwWindow;
	
	private Program program;
	private WindowHandler handler;
	
	private double dt;
	
	private boolean resized = false;
	
	private Window(int width, int height, String title, Program program) {
		this.width = width;
		this.height = height;
		this.title = title;
		this.program = program;
		this.handler = new WindowHandler(this);
		this.program.setWindowHandler(this.handler);
	}
	
	public void run() {
		Logging.debugPrint("Running LWJGL " + Version.getVersion() + ".");
		
		init();
		loop();
		
		//free memory at end
		Callbacks.glfwFreeCallbacks(glfwWindow);
		GLFW.glfwDestroyWindow(glfwWindow);
		
		//terminate GLFW and free the error callback
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
	}
	
	public void init() {
		//error callback
		GLFWErrorCallback.createPrint(System.err).set();
		
		//init GLFW
		if(!GLFW.glfwInit()) {
			throw new IllegalStateException("Unable to init GLFW.");
		}
		
		//configure GLFW
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
		
		//create window
		glfwWindow = GLFW.glfwCreateWindow(this.width, this.height, this.title, MemoryUtil.NULL, MemoryUtil.NULL);
		if(glfwWindow == MemoryUtil.NULL) {
			throw new IllegalStateException("Failed to create GLFW window.");
		}
		
		//set callbacks
		GLFW.glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
		GLFW.glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
		GLFW.glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
		GLFW.glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
		
		//make the OpenGL the context current
		GLFW.glfwMakeContextCurrent(glfwWindow);
		//vsync
		GLFW.glfwSwapInterval(0);

		//show window
		GLFW.glfwShowWindow(glfwWindow);
		
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();
		
		GL11.glViewport(0, 0, width, height);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		//create game wrapper
		this.handler = new WindowHandler(this);
		this.program.setWindowHandler(handler);
		this.program.init();
	}
	
	public void loop() {
		double nextSec = 0;
		double avgTime = 3;
		int framesElpsed = 0;
		double beginTime = GLFW.glfwGetTime();
		double endTime = GLFW.glfwGetTime();
		while(!GLFW.glfwWindowShouldClose(glfwWindow)) {
			updateDim();
			//poll events
			GLFW.glfwPollEvents();
			
			GL11.glClearColor(0, 0, 0, 1);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

			this.program.update();
			MouseListener.endFrame();

			GLFW.glfwSwapBuffers(glfwWindow);
			
			endTime = GLFW.glfwGetTime();
			framesElpsed++;
			this.dt = endTime - beginTime;
			nextSec += endTime - beginTime;
			beginTime = endTime;
			
			// only run next part every second
			if(nextSec < avgTime) continue;
				
			System.out.println("FPS: "+(int)(framesElpsed/nextSec));
			nextSec-=avgTime;
			framesElpsed = 0;
		}
	}
	
	public void updateDim() {
		this.resized = false;
		Vector2i dim = getWindowDim();
		
		//check if resize if required
		if(this.width == dim.x && this.height == dim.y) return;
		
		GL11.glViewport(0, 0, dim.x, dim.y);
		this.width = dim.x;
		this.height = dim.y;
		this.resized = true;
	}
	
	public double getDT() {
		return this.dt;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}
	
}
