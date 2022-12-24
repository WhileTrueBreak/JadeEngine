package jadeEngine.res;

import java.util.HashMap;
import java.util.Map;

import jadeEngine.gfx.Shader;
import jadeEngine.gfx.Spritesheet;
import jadeEngine.gfx.Texture;

public class Assets {

	//texture constants
	public static final String T_COMPANION_CUBE = "/textures/testImage.jpg";
	public static final String T_MARIO = "/textures/testImage.png";
	public static final String T_BACKGROUND = "/textures/background.png";
	public static final String T_GREEN_ALPHA = "/textures/blendImage2.png";
	public static final String T_RED_ALPHA = "/textures/blendImage1.png";

	//spritesheet texture constants
	public static final String T_SPRSHT_MARIO = "/textures/spritesheet.png";

	//spritesheet constants
	public static final String SPRSHT_MARIO = "sht_mario";
	public static final String SPRSHT_GOOMBA = "sht_goomba";
	
	//shader path constants
	public static final String SHDR_FRAG_DEFAULT = "/shaders/defaultFragment.glsl";
	public static final String SHDR_VERT_DEFAULT = "/shaders/defaultVertex.glsl";
	public static final String SHDR_FRAG_GUI = "/shaders/guiFragment.glsl";
	public static final String SHDR_VERT_GUI = "/shaders/guiVertex.glsl";
	
	//shader constants
	public static final String SHDR_DEFAULT = "shdr_default";
	public static final String SHDR_GUI = "shdr_gui";
	
	public static Map<String, Shader> SHADERS = new HashMap<String, Shader>();
	public static Map<String, Texture> TEXTURES = new HashMap<String, Texture>();
	public static Map<String, Spritesheet> SPRITESHEETS = new HashMap<String, Spritesheet>();
	
	public static void init() {
		//preload all assets
		//textures
		loadTexture(Assets.T_MARIO);
		loadTexture(Assets.T_COMPANION_CUBE);
		loadTexture(Assets.T_BACKGROUND);
		loadTexture(Assets.T_GREEN_ALPHA);
		loadTexture(Assets.T_RED_ALPHA);
		//spritesheets
		loadTexture(Assets.T_SPRSHT_MARIO);
		loadSpritesheet(Assets.SPRSHT_MARIO, new Spritesheet(Assets.T_SPRSHT_MARIO, 16, 16, 14, 0, 0, 0));
		loadSpritesheet(Assets.SPRSHT_GOOMBA, new Spritesheet(Assets.T_SPRSHT_MARIO, 16, 16, 12, 0, 0, 1));
		//shaders
		loadShader(Assets.SHDR_DEFAULT, Assets.SHDR_VERT_DEFAULT, Assets.SHDR_FRAG_DEFAULT);
		loadShader(Assets.SHDR_GUI, Assets.SHDR_VERT_GUI, Assets.SHDR_FRAG_GUI);
		
	}
	
	public static void loadShader(String name, String vertexPath, String fragmentPath) {
		if(Assets.SHADERS.containsKey(name)) return;
		Assets.SHADERS.put(name, new Shader(vertexPath, fragmentPath));
	}
	
	public static void loadTexture(String texturePath) {
		if(Assets.TEXTURES.containsKey(texturePath)) return;
		Assets.TEXTURES.put(texturePath, new Texture(texturePath));
	}
	
	public static void loadSpritesheet(String name, Spritesheet spritesheet) {
		if(Assets.SPRITESHEETS.containsKey(name)) return;
		Assets.SPRITESHEETS.put(name, spritesheet);
	}
	
	public static Shader getShader(String name) {
		if(Assets.SHADERS.containsKey(name)) {
			return Assets.SHADERS.get(name);
		}
		assert false:"Shader: "+name+" does not exist or not initialised";
		return null;
	}
	
	public static Texture getTexture(String texturePath) {
		if(!Assets.TEXTURES.containsKey(texturePath))
			assert false:"Texture: "+texturePath+" does not exist or not initialised";
		return Assets.TEXTURES.get(texturePath);
	}
	
	public static Spritesheet getSpritesheet(String spritesheetPath) {
		if(!Assets.SPRITESHEETS.containsKey(spritesheetPath))
			assert false:"Spritesheet: "+spritesheetPath+" does not exist or not initialised";
		return Assets.SPRITESHEETS.getOrDefault(spritesheetPath, null);
	}
	
}
