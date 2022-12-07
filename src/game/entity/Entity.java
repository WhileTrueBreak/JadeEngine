package game.entity;

import engine.gameobject.GameObject;
import engine.renderer.Transform;

public abstract class Entity extends GameObject{

	public Entity(String name, Transform transform) {
		super(name, transform);
	}

}
