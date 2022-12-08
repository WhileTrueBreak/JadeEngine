package jadeEngine.ui.component;

import org.joml.Vector4f;

import jadeEngine.gfx.Sprite;
import jadeEngine.gfx.Texture;
import jadeEngine.ui.Callback;
import jadeEngine.ui.UiRenderer;

public class UiBlock extends UiComponent{
	
	public UiBlock(Vector4f color) {
		this.uiRenderer = new UiRenderer(color);
		this.uiRenderer.setParent(this);
	}
	
	public UiBlock(Texture texture) {
		this.uiRenderer = new UiRenderer(new Sprite(texture));
		this.uiRenderer.setParent(this);
	}
	
	public UiBlock(Texture texture, Vector4f color) {
		this.uiRenderer = new UiRenderer(new Sprite(texture), color);
		this.uiRenderer.setParent(this);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDefault() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHover(Callback callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(Callback callback) {
		// TODO Auto-generated method stub
		
	}
	
}
