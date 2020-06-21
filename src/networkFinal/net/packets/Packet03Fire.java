package networkFinal.net.packets;

import java.util.ArrayList;
import java.util.List;

import networkFinal.main.Player;
import networkFinal.net.GameClient;
import networkFinal.net.GameServer;

public class Packet03Fire extends Packet {

	private String username;

	public Packet03Fire(byte[] data) {
		super(03);
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
	}

	public Packet03Fire(String username, double y) {
		super(03);
		this.username = username;
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
		return ("03" + this.username ).getBytes();
	}

	public String getUsername() {
		return this.username;
	}
	

}
