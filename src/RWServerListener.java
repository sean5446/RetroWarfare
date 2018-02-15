/*
 * Sean Maguire
 * see README
 * 
 */

import java.io.*;
import java.net.*;

public class RWServerListener extends Thread {
	protected MulticastSocket socket = null;
	private RWMessage rwMessage = null;
	private RWGame rwGame = null;
 
	public RWServerListener(RWMessage message, RWGame game) throws IOException {
		super("RWServerListener");
		socket = new MulticastSocket(15446);
		rwMessage = message;
		rwGame = game;
	}

	public void run() {
		try {
			InetAddress group = InetAddress.getByName("239.0.0.1");
			socket.joinGroup(group);

			while (true) {
				byte[] buf = new byte[256];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				String received = new String(packet.getData());
				String avatar = "";
				
				if (received.startsWith("rwcl")) {
					System.out.println(received);
				}
				if (received.startsWith("rwcli ")) {
					avatar = received.split(" ")[1].trim();
					//String address = received.split(" ")[2].trim();
					
					//add new player
					if (rwGame.playerExists(avatar)) {
						System.out.println("Exists");
						rwMessage.put("rwsvd " + avatar);
						continue;
					} 
					if (rwGame.playerCount() > 9) {
						System.out.println("Full");
						rwMessage.put("rwsvf " + avatar);
						continue;
					}
					
					String msg = "rwsvm " + avatar + " " + rwGame.getMap();
					System.out.println(msg);
					
					rwMessage.put("rwsvm " + avatar + " " + rwGame.getMap()); 
					rwGame.addPlayer(avatar);
				}
				if (received.startsWith("rwcla ")) {
					avatar = received.split(" ")[1].trim();
					String action = received.split(" ")[2].trim();
					
					rwGame.updatePlayer(avatar, action);
					
					//String msg = "rwsvu " + avatar + " " + rwGame.getX() + " " + rwGame.getY();
					//System.out.println(new Date().toString() + " Buffered: update " + msg);
					//rwMessage.put(msg);
				}
			}
		} catch (Exception e) {
			socket.close();
			e.printStackTrace();
		}
	}
}
