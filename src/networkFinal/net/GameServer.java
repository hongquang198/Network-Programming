package networkFinal.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import com.joshuacrotts.standards.StandardHandler;
import com.joshuacrotts.standards.StdOps;

import networkFinal.enemies.GreenBat;
import networkFinal.main.Bullet;
import networkFinal.main.GenericSpaceShooter;
import networkFinal.main.Player;
import networkFinal.net.packets.Packet;
import networkFinal.net.packets.Packet.PacketTypes;
import networkFinal.net.packets.Packet00Login;
import networkFinal.net.packets.Packet01Disconnect;
import networkFinal.net.packets.Packet02Move;
import networkFinal.net.packets.Packet03Fire;
import networkFinal.net.packets.Packet04EnemyMove;

public class GameServer extends Thread {

	private DatagramSocket socket;
	private GenericSpaceShooter game;
	private List<Player> connectedPlayers = new ArrayList<Player>();
	private List<GreenBat> enemies = new ArrayList<GreenBat>();
	Player player;

	public GameServer(GenericSpaceShooter game) {
		this.game = game;
		try {
			this.socket = new DatagramSocket(1331);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			this.generateEnemies();
			this.sendEnemies(player);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}

			this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
//			System.out.println("Size is " + connectedPlayers.size());
			// String message = new String(packet.getData());
//			System.out.println(
//					"CLIENT [" + packet.getAddress().getHostAddress() + ":" + packet.getPort() + "]> " + message);
//			if (message.trim().equalsIgnoreCase("ping")) {
//				sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
//			}
		}
	}

	private void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
		Packet packet = null;
		switch (type) {
		default:
		case INVALID:
			break;
		case LOGIN:
			packet = new Packet00Login(data);
			System.out.println("[" + address.getHostAddress() + ":" + port + "] "
					+ ((Packet00Login) packet).getUsername() + " has connected ..");
			player = new Player(300, 720, ((Packet00Login) packet).getUsername(), game, address, port);
			this.addConnection(player, (Packet00Login) packet);
			break;
		case DISCONNECT:
			packet = new Packet01Disconnect(data);
			System.out.println("[" + address.getHostAddress() + ":" + port + "] "
					+ ((Packet01Disconnect) packet).getUsername() + " has left the world..");

			this.removeConnection((Packet01Disconnect) packet);
			break;
		case MOVE:
			packet = new Packet02Move(data);
			System.out.println(((Packet02Move) packet).getUsername() + "has moved to " + ((Packet02Move) packet).getX()
					+ ", " + ((Packet02Move) packet).getY());
			this.handleMove(((Packet02Move) packet));
			break;
		case FIRE:
			packet = new Packet03Fire(data);
			System.out.println(((Packet03Fire) packet).getUsername() + "has fired.");
			this.handleFire(((Packet03Fire) packet));

			break;
		}
	}

	public void addConnection(Player player, Packet00Login packet) {
		boolean alreadyConnected = false;
		for (Player p : this.connectedPlayers) {
			if (player.getUsername().equalsIgnoreCase(p.getUsername())) {
				if (p.ipAddress == null) {
					p.ipAddress = player.ipAddress;
				}
				if (p.port == -1) {
					p.port = player.port;
				}
				alreadyConnected = true;
			} else {
				sendData(packet.getData(), p.ipAddress, p.port);

				packet = new Packet00Login(p.getUsername());
				sendData(packet.getData(), player.ipAddress, player.port);
			}
		}
		if (!alreadyConnected) {
			this.connectedPlayers.add(player);
//			game.addListener(player);
		}
	}

	public void generateEnemies() {
			if (GenericSpaceShooter.gssh.size() < 20) {
				GreenBat greenBat = new GreenBat(StdOps.rand(0, 760), StdOps.rand(-200, -50));
				enemies.add(greenBat);
		}
	}

	public void sendEnemies(Player player) {
		boolean alreadyConnected = false;
		for (Player p : this.connectedPlayers) {
			if (player.getUsername().equalsIgnoreCase(p.getUsername())) {
				alreadyConnected = true;
			} else {
				for (GreenBat enemy : enemies) {
					Packet04EnemyMove packet = new Packet04EnemyMove(enemy.getX(), enemy.getY());
					sendDataToAllClients(packet.getData());
				}
			}
		}
	}

	public void removeConnection(Packet01Disconnect packet) {
		this.connectedPlayers.remove(getPlayerIndex(packet.getUsername()));
		packet.writeData(this);
	}

	public Player getPlayer(String username) {
		for (Player player : this.connectedPlayers) {
			if (player.getUsername().equalsIgnoreCase(username)) {
				return player;
			}
		}
		return null;
	}

	public int getPlayerIndex(String username) {
		int index = 0;
		for (Player player : this.connectedPlayers) {
			if (player.getUsername().equalsIgnoreCase(username)) {
				break;
			}
			index++;
		}
		return index;
	}

	public void sendData(byte[] data, InetAddress ipAddress, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendDataToAllClients(byte[] data) {
		for (Player p : connectedPlayers) {
			sendData(data, p.ipAddress, p.port);
		}
	}

	private void handleMove(Packet02Move packet) {
		if (getPlayer(packet.getUsername()) != null) {
			int index = getPlayerIndex(packet.getUsername());
			this.connectedPlayers.get(index).x = packet.getX();
			this.connectedPlayers.get(index).y = packet.getY();
			packet.writeData(this);
		}
	}

	private void handleFire(Packet03Fire packet) {
		if (getPlayer(packet.getUsername()) != null) {
			int index = getPlayerIndex(packet.getUsername());
			double playerPositionX = this.connectedPlayers.get(index).x;
			double playerPositionY = this.connectedPlayers.get(index).y;
			packet.writeData(this);
		}
	}

}
