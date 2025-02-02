package networkFinal.enemies;

import java.awt.Graphics2D;

import com.joshuacrotts.standards.StdOps;

import networkFinal.main.ShootPlaneGame;

public class Hovercraft extends Enemy{

	public Hovercraft(double x, double y){
		super(x, y);
		
		this.currentSprite = StdOps.loadImage("Resources/hovercraft.png");
		
		this.width = this.currentSprite.getWidth();
		this.height = this.currentSprite.getHeight();
		
		this.health = 40;
		
		this.velY = ShootPlaneGame.score/1000 + 1;
		
		
	}
	
	public void tick(){
		
		if(this.health <= 0 || this.y >= 900){
			ShootPlaneGame.gameHandler.removeEntity(this);
			return;
		}
		
		if( ShootPlaneGame.score%1000 == 0) {
			this.velY*= 2;
//			this.velY *= speed;
		}
		this.x += this.velX;
		this.y += this.velY;
		
//		this.fireBulletCheck();
//		this.fireBullet();
		
	}
	
	public void render(Graphics2D g2){
		g2.drawImage(this.currentSprite, (int) x, (int) y, null);
	}
	
	public void fireBullet(){
//		if(this.interval < 150){
//			return;
//		}else{
//			this.interval = 0;
//			GenericSpaceShooter.gssh.addEntity(new Bullet((this.x + this.width / 2), this.y, 0, 20, this.getId(), "GreenBat"));
//		}
	}
	
	private void fireBulletCheck(){
		this.interval += StdOps.rand(0, 2);
		
		if(this.interval > 150){
			this.interval = 150;
		}
	}
}
