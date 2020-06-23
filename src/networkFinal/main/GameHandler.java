package networkFinal.main;

import java.awt.Graphics2D;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.joshuacrotts.standards.StandardGameObject;
import com.joshuacrotts.standards.StandardHandler;
import com.joshuacrotts.standards.StandardID;

public class GameHandler extends StandardHandler{
	
	public static GameHandler gameHandler;
	
	public GameHandler(){
		gameHandler = this;
		this.entities = new ArrayList<StandardGameObject>();
	}
	
	@Override
	public void tick(){
		for(int i = 0; i < this.entities.size(); i++){
			
			//Player to Bullet collision
			if(this.entities.get(i).getId() == StandardID.Player){
				
				for(int j = 0; j < this.entities.size(); j++){
					
					
					if(this.entities.get(j).getId() == StandardID.Obstacle &&
							this.entities.get(j).getBounds().intersects(this.entities.get(i).getBounds())){
						
							this.entities.get(i).health -= 50;
							this.entities.remove(j);
							j--;
						
					}
					
				}
				
			}
			
			//Player bullet to Enemy
			if(this.entities.get(i).getId() == StandardID.Weapon){
				
				for(int j = 0; j < this.entities.size(); j++){
					
					if(this.entities.get(j).getId() == StandardID.Enemy &&
							this.entities.get(j).getBounds().intersects(this.entities.get(i).getBounds())){
						
						this.entities.get(j).health -= 20;
//						GenericSpaceShooter.score += 1000;??
						
					}
					
				}
				
			}
			
			// Player to enemy collision
			if(this.entities.get(i).getId() == StandardID.Player){
				Player player = (Player) this.entities.get(i);
				
				for(int j = 0; j < this.entities.size(); j++){
					
					
					if(this.entities.get(j).getId() == StandardID.Enemy &&
							this.entities.get(j).getBounds().intersects(this.entities.get(i).getBounds())){
						
							this.entities.get(i).health -= 0;
							this.entities.remove(j);
							j--;
							if (player.getUsername().equalsIgnoreCase(ShootPlaneGame.player.getUsername())) {
								JOptionPane.showMessageDialog(null, "You died, your score was: " + ShootPlaneGame.score);
								ShootPlaneGame.game.window.getFrame().dispatchEvent(new WindowEvent(ShootPlaneGame.game.window.getFrame(), WindowEvent.WINDOW_CLOSING));
								
							}

						
					}
					
				}
				
			}

			this.entities.get(i).tick();
		}
	}
	
	@Override
	public void render(Graphics2D g2){
		for(int i = 0; i < this.entities.size(); i++){
			this.entities.get(i).render(g2);
		}
	}
	
	public void removePlayer(String username) {
		int index = 0;
		for (StandardGameObject e : gameHandler.entities) {
			if (e instanceof Player && ((Player) e).getUsername().equals(username)) {
				break;
			}
			index++;
		}
		gameHandler.entities.remove(index);
	}
	
	private Player getPlayer(String username) {
		for (StandardGameObject e : ShootPlaneGame.gameHandler.entities) {
			if (e instanceof Player && ((Player)e).getUsername().equals(username)) {
				return (Player)e;
			}
		}
		return null;
	}

	
	private int getPlayerIndex (String username) {
		int index = 0;
		for (StandardGameObject e : ShootPlaneGame.gameHandler.entities) {
			if (e instanceof Player && ((Player)e).getUsername().equals(username)) {
				break;
			}
			index++;
		}
		return index;
	}
	

	
	public void movePlayer(String username, double x, double y) {
		int index = getPlayerIndex(username);
		GameHandler.gameHandler.entities.get(index).x = x;
		GameHandler.gameHandler.entities.get(index).y = y;
	}
	
	public void fireBullet(String username) {
		Player player = getPlayer(username);
		int playerIndex = getPlayerIndex(username);
		double playerPositionX = GameHandler.gameHandler.entities.get(playerIndex).x;
		double playerPositionY = GameHandler.gameHandler.entities.get(playerIndex).y;
		double playerWidth = GameHandler.gameHandler.entities.get(playerIndex).width;
		GameHandler.gameHandler.entities.add(new Bullet((playerPositionX + playerWidth / 2), playerPositionY, -20, player.getId(), player.getUsername()));
	}
}
