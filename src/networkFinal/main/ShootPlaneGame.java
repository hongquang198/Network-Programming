package networkFinal.main;

import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

import com.joshuacrotts.standards.StandardDraw;
import com.joshuacrotts.standards.StandardGameObject;
import com.joshuacrotts.standards.StandardHandler;
import com.joshuacrotts.standards.StdOps;

import networkFinal.enemies.Hovercraft;
import networkFinal.net.packets.Packet00Login;
import networkFinal.net.packets.Packet01Disconnect;

public class ShootPlaneGame extends StandardGame {
	public static boolean isPlayer1Connected;
	public static boolean isPlayer2Connected;
	// Image reference:
	// http://wallpapercave.com/wp/jnmJ9pp.jpg

	private static final long serialVersionUID = -7923114031576573406L;

	// Inits the new handler to handle collisions
	public static StandardHandler gameHandler;

	public static ShootPlaneGame game;
	public WindowHandler windowHandler;
	// Inits the objects in the game
	static Player player;

	// Background init
	public static BufferedImage bg = null;

	// Etc instance variables
	public static int score = 0;

	public ShootPlaneGame(int w, int h) {
		super(w, h, "KAMIKAZE");
		isPlayer1Connected = false;
		isPlayer2Connected = false;
		this.consoleFPS = false;
		game = this;
		ShootPlaneGame.bg = StdOps.loadImage("Resources/bg.png");

		ShootPlaneGame.gameHandler = new GameHandler();

		windowHandler = new WindowHandler(this);
		this.player = new Player(300, 720, JOptionPane.showInputDialog("Please enter a username"), this, null, -1);

		Packet00Login loginPacket = new Packet00Login(player.getUsername());
		ShootPlaneGame.gameHandler.addEntity(this.player);

		this.StartGame(this);

		if (socketServer != null) {
			socketServer.addConnection(player, loginPacket);
		}

		loginPacket.writeData(socketClient);
		this.addListener(player);

	}

	public void tick() {
//		if(GenericSpaceShooter.gssh.size() < 20)
//			GenericSpaceShooter.gssh.addEntity(new GreenBat(StdOps.rand(0, 760), StdOps.rand(-200, -50)));
		StandardHandler.Handler(gameHandler);
		if (isPlayer1Connected == true && isPlayer2Connected == true) {
			ShootPlaneGame.score++;			
		}
		if (player.getHealth() <=0 ) {
//			this.StopGame();
////			gss.window.getFrame().dispatchEvent(new WindowEvent(gss.window.getFrame(), WindowEvent.WINDOW_CLOSING));
//			gss.window.getFrame().setVisible(false);

		}
	}

	public void render() {
		StandardDraw.image(ShootPlaneGame.bg, 0, 0);
		StandardDraw.Handler(gameHandler);

	}


	public static void main(String[] args) {
		new ShootPlaneGame(800, 800);
	}
}
