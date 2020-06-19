package networkFinal.net.packets;

import java.util.ArrayList;
import java.util.List;

import networkFinal.main.Player;
import networkFinal.net.GameClient;
import networkFinal.net.GameServer;

public class Packet02Move extends Packet {

	private String username;
	private double x, y;

	public Packet02Move(byte[] data) {
		super(02);
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];

		this.x = Double.parseDouble(dataArray[1]);
		this.y = Double.parseDouble(dataArray[2]);
	}

	public Packet02Move(String username, double x, double y) {
		super(02);
		this.username = username;
		this.x = x;
		this.y = y;
	}

	@Override
	public void writeData(GameClient client) {
		client.sendData(getData());

	}

	@Override
	public void writeData(GameServer server) {
		server.sendDataToAllClients(getData());
	}

	@Override
	public byte[] getData() {
		return ("02" + this.username + "," + this.x + "," + this.y).getBytes();
	}

	public String getUsername() {
		return username;
	}
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}

}
