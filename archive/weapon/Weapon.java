package game.weapon;

import java.awt.Graphics;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import game.Handler;
import utils.Logging;

public abstract class Weapon {
	
	///////////////////////////////////////////////////
	/////////////////static functions//////////////////
	///////////////////////////////////////////////////
	
	public static ArrayList<Class<? extends Weapon>> WEAPONS_CLASS = new ArrayList<Class<? extends Weapon>>();
	
	public static void initWeapons() {
		WEAPONS_CLASS.add(Sword.class);
		WEAPONS_CLASS.add(Pistol.class);
		for(Class<? extends Weapon> weapon:WEAPONS_CLASS) {
			Logging.debugPrint("Retrieved: "+weapon.getName());
		}
	}
	
	public static Weapon createWeapon(Handler handler, int id) {
		if(id < 0 || id >= WEAPONS_CLASS.size()) {
			new Exception("ID "+id+" out of bounds").printStackTrace();
			return null;
		}
        try {
			Weapon w = WEAPONS_CLASS.get(id).getDeclaredConstructor(Handler.class).newInstance(handler);
			w.setID(id);
			return w;
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
	
	protected Handler handler;
	protected int id = -1;
	
	public Weapon(Handler handler) {
		this.handler = handler;
	}
	
	public abstract void render(Graphics g);
	public abstract void update();
	
	public abstract boolean isReady();
	
	public int getID() {
		if(id != -1) return this.id;
		return Weapon.WEAPONS_CLASS.indexOf(this.getClass());
	}
	
	public void setID(int id) {
		if(id == -1) this.id = id;
	}
	
	public Weapon dupe() {
		try {
			return this.getClass().getDeclaredConstructor(Handler.class).newInstance(handler);
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
	
}