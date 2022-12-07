package game.entity.creature;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import game.Handler;
import game.entity.Entity;
import game.weapon.Weapon;
import utils.Logging;

public abstract class Creature extends Entity{

	///////////////////////////////////////////////////
	/////////////////static functions//////////////////
	///////////////////////////////////////////////////
	
	public static int PLAYER_ID = 0;
	
	public static ArrayList<Class<? extends Creature>> CREATURE_CLASS = new ArrayList<Class<? extends Creature>>();
	
	public static void initCreatures() {
		//add particle
		CREATURE_CLASS.add(Player.class);
		for(Class<? extends Creature> creature:CREATURE_CLASS) {
			Logging.debugPrint("Retrieved: "+creature.getName());
		}
	}
	
	public static Creature createCreature(double x, double y, Handler handler, int id) {
		if(id < 0 || id >= CREATURE_CLASS.size()) {
			new Exception("ID "+id+" out of bounds").printStackTrace();
			return null;
		}
        try {
			Creature c = CREATURE_CLASS.get(id).getDeclaredConstructor(Double.TYPE, Double.TYPE, Handler.class).newInstance(x, y, handler);
			c.setID(id);
			return c;
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
	
	protected Weapon weapon;
	protected int team;
	protected int health;
	
	protected int id = -1;
	
	public Creature(double x, double y, Handler handler) {
		super(x, y, handler);
	}
	
	public int getHealth() {
		return health;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	public int getTeam() {
		return team;
	}
	
	public void setTeam(int team) {
		this.team = team;
	}
	
	public int getID() {
		if(id != -1) return this.id;
		return Creature.CREATURE_CLASS.indexOf(this.getClass());
	}
	
	public void setID(int id) {
		if(id == -1) this.id = id;
	}

}
