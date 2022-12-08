package game.entity;

import jadeEngine.gameobject.GameObject;
import jadeEngine.renderer.Transform;

public abstract class Entity extends GameObject{

	public Entity(String name, Transform transform) {
		super(name, transform);
	}

}
