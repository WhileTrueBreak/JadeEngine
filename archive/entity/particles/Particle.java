package game.entity.particles;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import game.Handler;
import game.entity.Entity;
import utils.Logging;

public abstract class Particle extends Entity{
	
	///////////////////////////////////////////////////
	/////////////////static functions//////////////////
	///////////////////////////////////////////////////
	
	public static ArrayList<Class<? extends Particle>> PARTICLE_CLASS = new ArrayList<Class<? extends Particle>>();
	
	public static void initParticles() {
		//add particle
		PARTICLE_CLASS.add(BlinkParticle.class);
		for(Class<? extends Particle> particle:PARTICLE_CLASS) {
			Logging.debugPrint("Retrieved: "+particle.getName());
		}
	}
	
	public static Particle createParticle(double x, double y, Handler handler, int id) {
		if(id < 0 || id >= PARTICLE_CLASS.size()) {
			new Exception("ID "+id+" out of bounds").printStackTrace();
			return null;
		}
        try {
			Particle p = PARTICLE_CLASS.get(id).getDeclaredConstructor(Double.TYPE, Double.TYPE, Handler.class).newInstance(x, y, handler);
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
	
	protected int id = -1;
	
	public Particle(double x, double y, Handler handler) {
		super(x, y, handler);
		this.handler = handler;
		zIndex = 0;
	}
	
	public int getID() {
		if(id != -1) return this.id;
		return Particle.PARTICLE_CLASS.indexOf(this.getClass());
	}
	
	public void setID(int id) {
		if(id == -1) this.id = id;
	}
	
}
