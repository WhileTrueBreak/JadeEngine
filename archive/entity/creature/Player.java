package game.entity.creature;

import java.awt.Color;
import java.awt.Graphics;

import game.Handler;
import game.entity.particles.BlinkParticle;
import game.entity.particles.Particle;
import game.inputs.Binds;
import game.inputs.KeyListener;
import game.inputs.MouseListener;
import game.weapon.Weapon;
import utils.EventTimer;
import utils.Func;
import utils.Polygon;
import utils.Vector;

public class Player extends Creature{
	
	public static final double PLAYER_SPEED = 20;
	
	private double velx, vely;

	private EventTimer blinkTimer;
	
	public Player(double x, double y, Handler handler) {
		super(x, y, handler);
		this.health = 1;
		this.width = 2;
		this.height = 2;
		Vector[] vertices = {new Vector(0,0), new Vector(0,this.width), new Vector(this.height,this.width), new Vector(this.height,0)};
		this.setHitbox(new Polygon(vertices));
		this.weapon = Weapon.createWeapon(handler, 1);
		
		blinkTimer = new EventTimer();
	}

	@Override
	public void render(Graphics g) {
//		g.setColor(new Color(0, 255, 0));
//		g.fillRect((int)(this.x*handler.getCamera().getScale()-handler.getCamera().getXoff()), 
//				(int)(this.y*handler.getCamera().getScale()-handler.getCamera().getYoff()), 
//				(int)(this.width*handler.getCamera().getScale()), 
//				(int)(this.height*handler.getCamera().getScale()));
	}

	@Override
	public void update() {
		move();
		blinkTimer.step(handler.getDT());
	}
	
	private void move() {
		velx = 0;
		vely = 0;
		if(KeyListener.isKeyPressed(Binds.DOWN)) {
			vely++;
		}
		if(KeyListener.isKeyPressed(Binds.UP)) {
			vely--;
		}
		if(KeyListener.isKeyPressed(Binds.RIGHT)) {
			velx++;
		}
		if(KeyListener.isKeyPressed(Binds.LEFT)) {
			velx--;
		}
		double velMag = Math.sqrt(velx*velx+vely*vely);
		if(velMag != 0 && handler.getCurrentFps() != 0) {
			velx = velx/velMag/handler.getCurrentFps()*PLAYER_SPEED;
			vely = vely/velMag/handler.getCurrentFps()*PLAYER_SPEED;
		}
		x += velx;
		y += vely;
	}
	
	@Override
	public boolean isExpired() {
		return health <= 0;
	}
	
}
