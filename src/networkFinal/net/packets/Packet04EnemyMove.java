package networkFinal.net.packets;

import java.util.ArrayList;
import java.util.List;

import networkFinal.main.Player;
import networkFinal.net.GameClient;
import networkFinal.net.GameServer;

public class Packet04EnemyMove extends Packet {

	private double x, y;

	public Packet04EnemyMove(byte[] data) {
		super(04);
		String[] dataArray = readData(data).split(",");

		this.x = Double.parseDouble(dataArray[1]);
		this.y = Double.parseDouble(dataArray[2]);
	}

	public Packet04EnemyMove(double x, double y) {
		super(04);
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
		return ("04" + "," + this.x + "," + this.y).getBytes();
	}

	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}

}
