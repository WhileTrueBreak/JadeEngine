package jadeEngine.gfx;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import jadeEngine.res.Assets;

public class Spritesheet {

	private Texture texture;
	private String texturePath;
	private List<Sprite>sprites;
	
	private int startIndexX, startIndexY;
	
	public Spritesheet(String texturePath, int spriteWidth, int spriteHeight, int numSprites, int spacing) {
		this.sprites = new ArrayList<Sprite>();
		this.texturePath = texturePath;
		this.texture = Assets.getTexture(texturePath);
		this.startIndexX = 0;
		this.startIndexY = 0;
		init(spriteWidth, spriteHeight, numSprites, spacing, 0, 0);
	}
	
	public Spritesheet(String texturePath, int spriteWidth, int spriteHeight, int numSprites, int spacing, int startIndexX, int startIndexY) {
		this.sprites = new ArrayList<Sprite>();
		this.texturePath = texturePath;
		this.texture = Assets.getTexture(texturePath);
		this.startIndexX = startIndexX;
		this.startIndexY = startIndexY;
		init(spriteWidth, spriteHeight, numSprites, spacing, startIndexX, startIndexY);
	}
	
	private void init(int spriteWidth, int spriteHeight, int numSprites, int spacing, int startIndexX, int startIndexY) {
		
		int currentX = 0 + (spriteWidth + spacing) * startIndexX;
		int currentY = texture.getHeight()-spriteHeight - (spriteHeight + spacing) * startIndexY;
		
		for(int i = 0;i < numSprites;i++) {
			float topY = (currentY + spriteHeight)/(float)texture.getHeight();
			float bottomY = currentY/(float)texture.getHeight();
			float leftX = currentX/(float)texture.getWidth();
			float rightX = (currentX + spriteWidth)/(float)texture.getWidth();
			
			Vector2f[] texCoords = {
					new Vector2f(rightX, topY),
					new Vector2f(rightX, bottomY),
					new Vector2f(leftX, bottomY),
					new Vector2f(leftX, topY)
			};
			Sprite sprite = new Sprite(texture, texCoords);
			sprites.add(sprite);

			currentX += spriteWidth + spacing;
			if(currentX < texture.getWidth()) continue;
			currentX = 0;
			currentY -= spriteHeight + spacing;
			if(currentY >= 0) continue;
			break;
		}
	}
	
	public Sprite getSprite(int index) {
		return this.sprites.get(index);
	}

	public Texture getTexture() {
		return texture;
	}

	public String getTexturePath() {
		return texturePath;
	}

	public int getStartIndexX() {
		return startIndexX;
	}

	public int getStartIndexY() {
		return startIndexY;
	}

}
