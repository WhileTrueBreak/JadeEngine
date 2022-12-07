package game.entity;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;

import game.Handler;
import game.entity.creature.Creature;
import game.entity.particles.Particle;
import game.entity.projectile.Projectile;

public class EntityManager {
	
	protected Handler handler;

	protected ArrayList<Entity>entities;
	protected ArrayList<Creature>creatures;
	protected ArrayList<Particle>particles;
	protected ArrayList<Projectile>projectiles;
	protected ArrayList<Entity> toRemoveEntities;
	protected ArrayList<Entity> toAddEntities;
	
	public EntityManager(Handler handler) {
		this.handler = handler;
		entities = new ArrayList<Entity>();
		creatures = new ArrayList<Creature>();
		particles = new ArrayList<Particle>();
		projectiles = new ArrayList<Projectile>();
		toRemoveEntities = new ArrayList<Entity>();
		toAddEntities = new ArrayList<Entity>();
	}
	
	public void render(Graphics g) {
//		sortZIndex();
//		Rectangle2D screen = new Rectangle2D.Double(
//				handler.getCamera().getXoff()/handler.getCamera().getScale(), 
//				handler.getCamera().getYoff()/handler.getCamera().getScale(), 
//				handler.getWidth()/handler.getCamera().getScale(), 
//				handler.getHeight()/handler.getCamera().getScale());
//		for(Entity entity:entities) {
//			if(entity.getDrawbox() == null) continue;
//			Rectangle2D posdrawbox = new Rectangle2D.Double(
//					entity.getDrawbox().getX()+entity.getX(), 
//					entity.getDrawbox().getY()+entity.getY(), 
//					entity.getDrawbox().getWidth(), 
//					entity.getDrawbox().getHeight());
//			if(!posdrawbox.intersects(screen)) continue;
//			entity.render(g);
//		}
	}
	
	public void update() {
		entities.removeAll(toRemoveEntities);
		entities.addAll(toAddEntities);
		toRemoveEntities.clear();
		toAddEntities.clear();
		for(Entity entity:entities) {
			entity.update();
			if(entity.isExpired()) {
				removeEntity(entity);
			}
		}
	}
	
	private void sortZIndex() {
//		long startTime = System.nanoTime();
		int indexLen = 3;
		int base = 64;
		//base 10 radix sort
		Entity[] buffer = new Entity[entities.size()];
		int[] counts = new int[2*base-1];
		
		for(int i = 0;i < indexLen;i++) {
			int place = (int) Math.pow(base, i);
			//get counts
			for(Entity e:entities) {
				int digit = e.getZIndex()/place%base+(base-1);
				counts[digit]++;
			}
			//get prefix sum
			for(int j = 1;j < 2*base-1;j++) {
				counts[j] += counts[j-1];
			}
			//reconstruct array
			for(int j = entities.size()-1;j >= 0;j--) {
				int digit = entities.get(j).getZIndex()/place%base+(base-1);
				buffer[--counts[digit]] = entities.get(j);
			}
			//swap buffer for array
			entities = new ArrayList<>(Arrays.asList(buffer));
			counts = new int[2*base-1];
		}
//		Logging.debugPrint((System.nanoTime()-startTime)/1000000+"ms\t|Entities: "+entities.size());
	}

	public ArrayList<Entity> getEntities(){
		return this.entities;
	}

	public ArrayList<Creature> getCreatures(){
		return this.creatures;
	}

	public ArrayList<Particle> getParticles(){
		return this.particles;
	}

	public ArrayList<Projectile> getProjectiles(){
		return this.projectiles;
	}

	public void addEntity(Entity entity) {
		if(entity == null) return;
		if(!(entity instanceof Entity)) return;
		this.toAddEntities.add(entity);
		if(entity instanceof Creature) this.creatures.add((Creature)entity);
		else if(entity instanceof Projectile) this.projectiles.add((Projectile)entity);
		else if(entity instanceof Particle) this.particles.add((Particle)entity);
	}
	
	public void removeEntity(Entity entity) {
		if(entity == null) return;
		if(!(entity instanceof Entity)) return;
		this.toRemoveEntities.add(entity);
		if(entity instanceof Creature) this.creatures.remove((Creature)entity);
		else if(entity instanceof Projectile) this.projectiles.remove((Projectile)entity);
		else if(entity instanceof Particle) this.particles.remove((Particle)entity);
	}
	
}
