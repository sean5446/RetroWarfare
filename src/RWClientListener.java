/*
 * Sean Maguire
 * see README
 * 
 */

import java.io.*;
import java.net.*;
import jcurses.system.*;

public class RWClientListener extends Thread {
	protected MulticastSocket socket = null;
	private String avatar;
	private String map;

	public RWClientListener(String avatar) throws IOException {
		super("RWClientListener");
		socket = new MulticastSocket(15446);
		this.avatar = avatar;
	}

	public void run() {
		try {
			InetAddress group = InetAddress.getByName("239.0.0.1");
			socket.joinGroup(group);
			boolean accepted = false;

			while (true) {
				byte[] buf = new byte[256];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet); // get response
				String received = new String(packet.getData());

				if (received.startsWith("rwsvd " + avatar)) {
					System.out.println("\nID '" + avatar + "' is in use. Try another avatar.\n\r");
					System.exit(0);
				}

				if (received.startsWith("rwsvf " + avatar)) {
					System.out.println("\nServer is full. Try again later.\n\r");
					System.exit(0);
				}

				if (received.startsWith("rwsvm " + avatar)) {
					map = received.substring(8, received.length()).trim();
					Toolkit.clearScreen(new CharColor(CharColor.BLACK, CharColor.CYAN));
					String status = "Map received. Match will begin in 3 seconds...\n\r";
					Toolkit.printString(status, 0, 0, new CharColor(
							CharColor.BLACK, CharColor.GREEN));
					sleep(3000);
					accepted = true;
				}

				if (received.startsWith("rwsvu ") && accepted) {
					parseUpdate(received.substring(6, received.length()));
				}
			}
		} catch (Exception e) {
			socket.close();
			e.printStackTrace();
		}
	}

	private void parseMap() {
		String maphorz = map.split("~")[0];
		String mapvert = map.split("~")[1];

		// BORDER
		Toolkit.drawBorder(0, 1, 79, 23, new CharColor(CharColor.BLACK,
				CharColor.WHITE));

		String[] smaphorz = maphorz.split("`");
		for (int i = 0; i < smaphorz.length; i++) {
			int startx = Integer.parseInt(smaphorz[i].split(" ")[0].trim());
			int starty = Integer.parseInt(smaphorz[i].split(" ")[1].trim());
			int length = Integer.parseInt(smaphorz[i].split(" ")[2].trim());
			Toolkit.drawHorizontalLine(startx, starty, length, new CharColor(
					CharColor.BLACK, CharColor.WHITE));
		}

		String[] smapvert = mapvert.split("`");
		for (int i = 0; i < smapvert.length; i++) {
			int startx = Integer.parseInt(smapvert[i].split(" ")[0].trim());
			int starty = Integer.parseInt(smapvert[i].split(" ")[1].trim());
			int length = Integer.parseInt(smapvert[i].split(" ")[2].trim());
			Toolkit.drawVerticalLine(startx, starty, length, new CharColor(
					CharColor.BLACK, CharColor.WHITE));
		}
	}

	private void parseUpdate(String update) {
		try {

			String players = update.split("~")[0];
			String bullets = update.split("~")[1];

			// CLEAR SCREEN
			Toolkit.clearScreen(new CharColor(CharColor.BLACK, CharColor.CYAN));

			parseMap(); // parse and print the map
			String status = "Retro Warfare: ";

			// players
			String[] splayers = players.split("`");
			for (int i = 0; i < splayers.length; i++) {
				String avatar = splayers[i].split(" ")[0].trim();
				int posx = Integer.parseInt(splayers[i].split(" ")[1].trim());
				int posy = Integer.parseInt(splayers[i].split(" ")[2].trim());
				int score = Integer.parseInt(splayers[i].split(" ")[3].trim());
				status += avatar + "(" + score + ") ";  // build status
				if (this.avatar.equals(avatar)) {
					Toolkit.printString(avatar, posx, posy, new CharColor(
							CharColor.BLACK, CharColor.CYAN));
				} else {
					Toolkit.printString(avatar, posx, posy, new CharColor(
							CharColor.BLACK, CharColor.RED));
				}
			}
			
			// print scores
			Toolkit.printString(status, 0, 0, new CharColor(CharColor.BLACK, CharColor.GREEN));

			// bullets
			if (bullets.contains("`")) {
				String[] sbullets = bullets.split("`");
				for (int i = 0; i < sbullets.length; i++) {
					if (sbullets[i].contains(" ")) {
						int posx = Integer.parseInt(sbullets[i].split(" ")[0].trim());
						int posy = Integer.parseInt(sbullets[i].split(" ")[1].trim());
						Toolkit.printString(".", posx, posy, new
								CharColor(CharColor.BLACK,CharColor.WHITE));
					}
				}
			}

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}

}
