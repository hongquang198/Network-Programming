package networkFinal.net.packets;

import java.util.ArrayList;
import java.util.List;

import networkFinal.main.Player;
import networkFinal.net.GameClient;
import networkFinal.net.GameServer;

public class Packet00Login extends Packet {
	
	private String username;
	
	public Packet00Login(byte[] data) {
		super(00);
		this.username = readData(data);
	}
	
	public Packet00Login(String username) {
		super(00);
		this.username = username;
	}
	
	@Override
	public void writeData(GameClient client) {
		client.sendData(getData());
		System.out.println("???");

	}

	@Override
	public void writeData(GameServer server) {
		server.sendDataToAllClients(getData());
	}
	
	@Override
	public byte[] getData() {
		return ("00" + this.username).getBytes();
	}
	
	public String getUsername() {
		return username;
	}
	
}
