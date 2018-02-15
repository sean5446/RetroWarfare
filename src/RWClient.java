/*
 * Sean Maguire
 * see README
 * 
 */

import java.net.*;
import jcurses.system.*;

public class RWClient {

	public static void main(String[] args) throws Exception {
		try {
			// find my IP address
			InetAddress addr = InetAddress.getLocalHost();
			String hostAddress = addr.getHostAddress(); // addr.getHostName();

			// setup UDP socket
			MulticastSocket socket = new MulticastSocket(15446);
			InetAddress group = InetAddress.getByName("239.0.0.1");
			socket.joinGroup(group);

			// prompt for name
			System.out.print("Avatar [A-Z]: ");
			int av = System.in.read();
			if (!(av > 64 && av < 91)) {
				System.out.println("Invalid avatar. Must be a capital letter.");
				return;
			}
			String avatar = "" + (char) av;

			// start the listener
			RWClientListener recvThread = new RWClientListener(avatar);
			recvThread.start();

			// send name to server
			String dString = "rwcli " + avatar + " " + hostAddress;
			byte[] buf = new byte[256];
			buf = dString.getBytes();
			DatagramPacket spacket = new DatagramPacket(buf, buf.length, group, 15446);
			socket.send(spacket);
			
			// show status
			String status = "Requested '" + avatar + "'. Waiting for response...";
			//Toolkit.printString(status, 0, 0, new CharColor(CharColor.BLACK,CharColor.YELLOW));
			System.out.println(status);

			while (true) {
				InputChar c = Toolkit.readCharacter(); // get input from user
				int n = c.getCode();
				String action = "";

				if (n == 27 || n == 113 || n == 81) {	// quit (3)
					action = "q";
				} else if (n == 119) {	// up
					action = "u";
				} else if (n == 97) {	// left
					action = "l";
				} else if (n == 115) {	// down
					action = "d";
				} else if (n == 100) {	// right
					action = "r";
				} else if (n == 32) {	// shoot
					action = "s";
				}

				// send action
				if (!action.equals("")) {
					dString = "rwcla " + avatar + " " + action;
					buf = new byte[256];
					buf = dString.getBytes();
					spacket = new DatagramPacket(buf, buf.length, group, 15446);
					socket.send(spacket);
				}
				if (action.equals("q")) {	// quit!
					socket.leaveGroup(group);
					socket.close();
					Toolkit.clearScreen(new CharColor(CharColor.BLACK, CharColor.CYAN));
					Toolkit.shutdown();
					System.exit(0);
					break;
				}
				
				Thread.currentThread().sleep(100);
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			System.out.println(ex.getLocalizedMessage());
			ex.printStackTrace();
		}
	}

}
