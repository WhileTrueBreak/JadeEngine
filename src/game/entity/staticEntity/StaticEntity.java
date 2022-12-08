package game.entity.staticEntity;

import game.entity.Entity;
import jadeEngine.renderer.Transform;

public abstract class StaticEntity extends Entity{

	public StaticEntity(String name, Transform transform) {
		super(name, transform);
	}
	
}
