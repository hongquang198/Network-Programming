package networkFinal.net.packets;

import java.util.ArrayList;
import java.util.List;

import networkFinal.main.Player;
import networkFinal.net.GameClient;
import networkFinal.net.GameServer;

public class Packet03Fire extends Packet {

	private String username;
	private double x, y;

	public Packet03Fire(byte[] data) {
		super(03);
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];

		this.x = Double.parseDouble(dataArray[1]);
		this.y = Double.parseDouble(dataArray[2]);
	}

	public Packet03Fire(String username, double x, double y) {
		super(03);
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
		return ("03" + this.username + "," + this.x + "," + this.y).getBytes();
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
