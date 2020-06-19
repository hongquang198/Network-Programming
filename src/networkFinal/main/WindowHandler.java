package networkFinal.main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import networkFinal.net.packets.Packet01Disconnect;

public class WindowHandler implements WindowListener {
	
	private final GenericSpaceShooter gss;
	
	public WindowHandler(GenericSpaceShooter gss) {
		this.gss = gss;
		this.gss.window.getFrame().addWindowListener(this);
	}
	
	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		Packet01Disconnect packet = new Packet01Disconnect(this.gss.player.getUsername());
		packet.writeData(this.gss.socketClient);
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

}
