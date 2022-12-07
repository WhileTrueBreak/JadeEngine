package game.entity.particles;

import java.awt.Color;
import java.awt.Graphics;

import game.Handler;
import utils.EventTimer;
import utils.Func;
import utils.Polygon;
import utils.Vector;

public class BlinkParticle extends Particle{
	
	private double velx, vely, fricCoef;
	private EventTimer decayTimer;
	private double decayTime;

	private double mass;
	
	public BlinkParticle(double x, double y, Handler handler) {
		super(x, y, handler);
		this.decayTimer = new EventTimer();
		this.decayTime = 5;
		this.width = 0.5;
		this.height = 0.5;
		Vector[] vertices = {new Vector(-width/2, -height/2), new Vector(width/2, -height/2), new Vector(width/2, height/2), new Vector(-width/2, height/2)};
		this.setHitbox(new Polygon(vertices));
		this.mass = 1;
		this.zIndex = -1;
	}
	
	public void init(double velx, double vely, double fricCoef, double decayTime) {
		this.velx = velx;
		this.vely = vely;
		this.fricCoef = fricCoef;
		this.decayTime = decayTime;
		decayTimer.reset();
	}

	@Override
	public void render(Graphics g) {
//		double op = Func.map(decayTimer.getTime(), decayTime/2, decayTime, 1, 0);
//		g.setColor(new Color(0f,0.7f,0.7f,(float) op));
//		g.fillOval((int)((this.x-this.width/2)*handler.getCamera().getScale()-handler.getCamera().getXoff()), 
//			(int)((this.y-this.height/2)*handler.getCamera().getScale()-handler.getCamera().getYoff()), 
//			(int)(this.width*handler.getCamera().getScale()), 
//			(int)(this.height*handler.getCamera().getScale()));
	}

	@Override
	public void update() {
		decayTimer.step(handler.getDT());
		double timeStep = handler.getDT();
		double b = -fricCoef/mass;
		x = -velx*mass/fricCoef*Math.exp(b*timeStep)+x+velx*mass/fricCoef;
		y = -vely*mass/fricCoef*Math.exp(b*timeStep)+y+vely*mass/fricCoef;
		velx = velx*Math.exp(b*timeStep);
		vely = vely*Math.exp(b*timeStep);
	}

	@Override
	public boolean isExpired() {
		return decayTimer.getTime() >= decayTime;
	}
	
}
