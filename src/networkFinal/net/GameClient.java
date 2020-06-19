package networkFinal.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import networkFinal.main.GenericSpaceShooter;
import networkFinal.main.GenericSpaceShooterHandler;
import networkFinal.main.Player;
import networkFinal.net.packets.Packet;
import networkFinal.net.packets.Packet.PacketTypes;
import networkFinal.net.packets.Packet00Login;
import networkFinal.net.packets.Packet01Disconnect;
import networkFinal.net.packets.Packet02Move;

public class GameClient extends Thread {
	private InetAddress ipAddress;
	private DatagramSocket socket;
	private GenericSpaceShooter gss;

	public GameClient(GenericSpaceShooter gss, String ipAddress) {
		this.gss = gss;
		try {
			this.socket = new DatagramSocket();
			this.ipAddress = InetAddress.getByName(ipAddress);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());

//			String message = new String(packet.getData());
//			System.out.println("SERVER >" + message);	
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
					+ ((Packet00Login) packet).getUsername() + " has joined the game ..");
			Player player = new Player(300, 720, ((Packet00Login) packet).getUsername(), gss, address, port);
			GenericSpaceShooter.gssh.addEntity(player);
//			game.addListener(player);
			break;
		case DISCONNECT:
			packet = new Packet01Disconnect(data);
			System.out.println("[" + address.getHostAddress() + ":" + port + "] "
					+ ((Packet01Disconnect) packet).getUsername() + " has left  the world ..");
			GenericSpaceShooterHandler.gssh.removePlayer(((Packet01Disconnect) packet).getUsername());
			break;
		case MOVE:
			packet = new Packet02Move(data);
			handleMove(((Packet02Move)packet));
		}
		
	}

	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleMove(Packet02Move packet) {
		GenericSpaceShooterHandler.gssh.movePlayer(packet.getUsername(), packet.getX(), packet.getY());
	}
}
