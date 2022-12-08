package jadeEngine.gfx;

import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Paths;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;

import utils.Logging;

public class Texture {
	
	private int width, height;

	private String path;
	private int texID;
	
	public Texture(String path) {
		try {
			this.path = Paths.get(Texture.class.getResource(path).toURI()).toFile().getAbsolutePath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			assert false:"Failed to read path '"+path+"'";
		} catch (Exception e) {
			assert false:"Failed to read path '"+path+"'";
		}
		
		texID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

		IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
		IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
		IntBuffer channels = BufferUtils.createIntBuffer(1);
		
		STBImage.stbi_set_flip_vertically_on_load(true);
		ByteBuffer image = STBImage.stbi_load(this.path, widthBuffer, heightBuffer, channels, 0);
		STBImage.stbi_set_flip_vertically_on_load(false);

		if(image != null) {
			this.width = widthBuffer.get(0);
			this.height = heightBuffer.get(0);
			if(channels.get(0) == 3) {
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, widthBuffer.get(0), heightBuffer.get(0), 0, 
						GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, image);
			}else if(channels.get(0) == 4){
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, widthBuffer.get(0), heightBuffer.get(0), 0, 
						GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
			}else {
				assert false:"Unknown number of channels: "+channels.get(0)+" in "+path;
			}
			Logging.debugPrint("Loaded \t'" + path + "'");
		} else {
			assert false : "Could not load texture " + this.path;
		}
		
		STBImage.stbi_image_free(image);
	}

	public void bind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
	}
	
	public void unbind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getPath() {
		return path;
	}
	
}
