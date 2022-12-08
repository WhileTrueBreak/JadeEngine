package jadeEngine.gfx;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.lwjgl.opengl.GL20;

import utils.Logging;

public class ShaderLoader {

	private static String getResourceFileAsString(String path) throws IOException {
		URL url = ShaderLoader.class.getResource(path);
		Path p;
		try {
			p = Paths.get(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
			assert false : "";
			return "";
		}
		byte[] encoded = Files.readAllBytes(Paths.get(p.toString()));
		Logging.debugPrint("Loaded \t'" + path + "'");
		return new String(encoded, StandardCharsets.UTF_8);
	}
	
	public static int loadVertexShader(String path) {
		String src = "";
		try {
			src = getResourceFileAsString(path);
		} catch (IOException e) {
			e.printStackTrace();
			assert false : "";
		}
		int vertexID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		GL20.glShaderSource(vertexID, src);
		GL20.glCompileShader(vertexID);
		
		//check for errors
		int success = GL20.glGetShaderi(vertexID, GL20.GL_COMPILE_STATUS);
		if(success == GL20.GL_FALSE) {
			int len = GL20.glGetShaderi(vertexID, GL20.GL_INFO_LOG_LENGTH);
			System.err.println("ERROR: "+path+"\n\tVertex Shader failed to compile.");
			System.err.println(GL20.glGetShaderInfoLog(vertexID, len));
			assert false : "";
		}
		Logging.debugPrint("Complied '" + path + "'");
		return vertexID;
	}
	
	public static int loadFragmentShader(String path) {
		String src = "";
		try {
			src = getResourceFileAsString(path);
		} catch (IOException e) {
			e.printStackTrace();
			assert false : "";
		}
		int fragmentID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		GL20.glShaderSource(fragmentID, src);
		GL20.glCompileShader(fragmentID);
		
		//check for errors
		int success = GL20.glGetShaderi(fragmentID, GL20.GL_COMPILE_STATUS);
		if(success == GL20.GL_FALSE) {
			int len = GL20.glGetShaderi(fragmentID, GL20.GL_INFO_LOG_LENGTH);
			System.err.println("ERROR: "+path+"\n\tFragment Shader failed to compile.");
			System.err.println(GL20.glGetShaderInfoLog(fragmentID, len));
			assert false : "";
		}
		Logging.debugPrint("Complied '" + path + "'");
		return fragmentID;
	}
	
	public static int createShaderProgram(String vertexPath, String fragmentPath) {
		int shaderProgram = GL20.glCreateProgram();
		GL20.glAttachShader(shaderProgram, loadVertexShader(vertexPath));
		GL20.glAttachShader(shaderProgram, loadFragmentShader(fragmentPath));
		GL20.glLinkProgram(shaderProgram);
		
		//check for errors
		
		int success = GL20.glGetProgrami(shaderProgram, GL20.GL_LINK_STATUS);
		if(success == GL20.GL_FALSE) {
			int len = GL20.glGetProgrami(shaderProgram, GL20.GL_INFO_LOG_LENGTH);
			System.err.println("ERROR: "+vertexPath+" | "+fragmentPath+"\n\tLinking shaders failed.");
			System.err.println(GL20.glGetProgramInfoLog(shaderProgram, len));
			assert false : "";
		}
		Logging.debugPrint("Linked \t'" + vertexPath + "' & '" + fragmentPath + "'");
		return shaderProgram;
	}
	
	public static int createShaderProgram(int vertexID, int fragementID) {
		int shaderProgram = GL20.glCreateProgram();
		GL20.glAttachShader(shaderProgram, vertexID);
		GL20.glAttachShader(shaderProgram, fragementID);
		GL20.glLinkProgram(shaderProgram);
		
		//check for errors
		
		int success = GL20.glGetProgrami(shaderProgram, GL20.GL_LINK_STATUS);
		if(success == GL20.GL_FALSE) {
			int len = GL20.glGetProgrami(shaderProgram, GL20.GL_INFO_LOG_LENGTH);
			System.err.println("ERROR: "+vertexID+" | "+fragementID+"\n\tLinking shaders failed.");
			System.err.println(GL20.glGetProgramInfoLog(shaderProgram, len));
			assert false : "";
		}
		Logging.debugPrint("Linked "+vertexID+" & "+fragementID);
		return shaderProgram;
	}
	
}
