/*
 * Sean Maguire
 * see README
 * 
 */

public class RWServer {
	public static void main(String[] args) throws Exception {
		RWMessage rwMessage = new RWMessage();
		RWGame rwGame = new RWGame(rwMessage);
		RWServerSender sendThread = new RWServerSender(rwMessage);
		RWServerListener recvThread = new RWServerListener(rwMessage, rwGame);

		recvThread.start();
		sendThread.start();

		while (true) {
			long start = System.nanoTime();
			rwGame.moveBullets();
			String update = rwGame.toString();
			System.out.println("rwsrvu " + update
					+ " (" + (System.nanoTime() - start)/1000 + ")");
			rwMessage.put("rwsvu " + update);
			Thread.currentThread().sleep(100);
		}
	}
}
