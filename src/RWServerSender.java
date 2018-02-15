import java.io.*;
import java.net.*;
import java.util.*;

public class RWServerSender extends Thread {
	protected MulticastSocket socket = null;
	private RWMessage rwMessage = null;

	public RWServerSender(RWMessage message) throws IOException {
		super("MulticastRWServerSendThread");
		socket = new MulticastSocket(15446);
		rwMessage = message;
	}

	public void run() {
		try {
			InetAddress group = InetAddress.getByName("239.0.0.1");
			socket.joinGroup(group);
			
			while (true) {
				byte[] buf = new byte[256];
				String dString = rwMessage.take();
				buf = dString.getBytes();
				DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 15446);
				socket.send(packet);
			}
			
		} catch (Exception e) {
			socket.close();
			e.printStackTrace();
		}
	}

}
