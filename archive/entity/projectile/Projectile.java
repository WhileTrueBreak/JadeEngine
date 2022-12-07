package game.entity.projectile;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import game.Handler;
import game.entity.Entity;
import utils.Logging;
import utils.Polygon;

public abstract class Projectile extends Entity{
	
	///////////////////////////////////////////////////
	/////////////////static functions//////////////////
	///////////////////////////////////////////////////
	
	public static ArrayList<Class<? extends Projectile>> PROJECTILE_CLASS = new ArrayList<Class<? extends Projectile>>();
	
	public static void initProjectiles() {
		//add projectiles
		PROJECTILE_CLASS.add(Bullet.class);
		for(Class<? extends Projectile> projectile:PROJECTILE_CLASS) {
			Logging.debugPrint("Retrieved: "+projectile.getName());
		}
	}
	
	public static Projectile createProjectile(double x, double y, Entity origin, Handler handler, int id) {
		if(id < 0 || id >= PROJECTILE_CLASS.size()) {
			new Exception("ID "+id+" out of bounds").printStackTrace();
			return null;
		}
        try {
			Projectile p = PROJECTILE_CLASS.get(id).getDeclaredConstructor(Double.TYPE, Double.TYPE, Entity.class, Handler.class).newInstance(x, y, origin, handler);
			p.setID(id);
			return p;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
        return null;
    }
	
	///////////////////////////////////////////////////
	///////////////////Abstract Part///////////////////
	///////////////////////////////////////////////////
	
	protected Entity origin;
	
	protected int id = -1;
	
	protected Polygon hitbox;
	
	public Projectile(double x, double y, Entity origin,  Handler handler) {
		super(x, y, handler);
		this.origin = origin;
		this.zIndex = 0;
	}
	
	public Entity getOrigin() {
		return origin;
	}
	
	public void setOrigin(Entity origin) {
		this.origin = origin;
	}

	public int getID() {
		if(id != -1) return this.id;
		return Projectile.PROJECTILE_CLASS.indexOf(this.getClass());
	}
	
	public void setID(int id) {
		if(id == -1) this.id = id;
	}
	
}
