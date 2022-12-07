package game.entity.staticEntity;

import engine.renderer.Transform;
import game.entity.Entity;

public abstract class StaticEntity extends Entity{

	public StaticEntity(String name, Transform transform) {
		super(name, transform);
	}
	
}
