package networkFinal.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.net.InetAddress;

import javax.swing.JOptionPane;

import com.joshuacrotts.standards.StandardDraw;
import com.joshuacrotts.standards.StandardGameObject;
import com.joshuacrotts.standards.StandardID;
import com.joshuacrotts.standards.StdOps;

import networkFinal.net.packets.Packet01Disconnect;
import networkFinal.net.packets.Packet02Move;
import networkFinal.net.packets.Packet03Fire;

public class Player extends StandardGameObject implements KeyListener {

	protected GenericSpaceShooter gss;
	public static int bulletId = 0;
	private short interval = 100;
	public String username;
	public InetAddress ipAddress;
	public int port;

	public Player(double x, double y, String username, GenericSpaceShooter gss, InetAddress ipAddress, int port) {
		super(x, y, StandardID.Player);

		this.gss = gss;
		this.ipAddress = ipAddress;
		this.port = port;
		this.username = username;
		this.currentSprite = StdOps.loadImage("Resources/player.png");

		this.width = this.currentSprite.getWidth();
		this.height = this.currentSprite.getHeight();

		this.health = 50;

	}
	
	public Player(double x, double y, String username, GenericSpaceShooter gss) {
		super(x, y, StandardID.Player);

		this.gss = gss;

		this.currentSprite = StdOps.loadImage("Resources/player.png");

		this.width = this.currentSprite.getWidth();
		this.height = this.currentSprite.getHeight();

		this.health = 50;
		this.username = username;
	}

	public void tick() {

		if (this.health <= 0) {
//			GenericSpaceShooter.gssh.removeEntity(this);

//			JOptionPane.showMessageDialog(null, "You died, your score was: " + GenericSpaceShooter.score);
			
//			System.exit(0);
		}

		this.x += this.velX;
		this.y -= this.velY;

		if (velX != 0 || velY != 0) {
			Packet02Move packet = new Packet02Move(this.getUsername(), this.x, this.y);
			packet.writeData(GenericSpaceShooter.gss.socketClient);
		}
		this.checkCoordinates();

		this.fireBulletCheck();

	}

	public void render(Graphics2D g2) {

		g2.drawImage(this.currentSprite, (int) x, (int) y, null);
		
		StandardDraw.text("Life: ", 20, 50, "", 40f, Color.YELLOW);
		StandardDraw.text("Score: " + GenericSpaceShooter.score, 20, 90, "", 40f, Color.YELLOW);
		StandardDraw.text("Level: " + (GenericSpaceShooter.score/1000 + 1), 20, 140, "", 40f, Color.YELLOW);

	}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_A:
			this.velX = -5.0;
			break;
		case KeyEvent.VK_D:
			this.velX = 5.0;
			break;
		case KeyEvent.VK_W:
			this.velY = 5;
			break;
		case KeyEvent.VK_S:
			this.velY = -5;
			break;
		case KeyEvent.VK_SPACE:
			this.fireBullet();
			break;
		}
	}

	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_A:
			this.velX = 0;
			break;
		case KeyEvent.VK_D:
			this.velX = 0;
			break;
		case KeyEvent.VK_W:
			this.velY = 0;
			break;
		case KeyEvent.VK_S:
			this.velY = 0;
			break;


		}
	}

	public void keyTyped(KeyEvent e) {

	}

	public void checkCoordinates() {
		if (this.x <= 0) {
			this.x = 0;
		}

		if (this.x >= this.gss.width() - this.width) {
			this.x = this.gss.width() - this.width;

		}
	}

	private void fireBullet() {
		if (this.interval < 20) {
			return;
		} else {
			this.interval = 0;
			Bullet bullet = new Bullet((this.x + this.width / 2), this.y, -20, this.getId(), this.getUsername());
			GenericSpaceShooter.gssh.addEntity(bullet);
			bulletId++;
			Packet03Fire packet = new Packet03Fire(this.username, bullet.getX(), bullet.getY());
			packet.writeData(GenericSpaceShooter.gss.socketClient);

		}
	}

	private void fireBulletCheck() {
		this.interval++;

		if (this.interval > 20) {
			this.interval = 20;
		}
	}
	
	public String getUsername() {
		return this.username;
	}
	
}
