
public class RWMessage {
	// Message sent from producer to consumer.
	private String message;

	// True if consumer should wait for producer to send message,
	// false if producer should wait for consumer to retrieve message.
	private boolean empty = true;

	// Object to use to synchronize against so as to not "leak" the
	// "this" monitor
	private Object lock = new Object();

	public String take() {
		synchronized (lock) {
			// Wait until message is available.
			while (empty) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
				}
			}
			empty = true; // Toggle status.
			lock.notifyAll(); // Notify producer that status has changed.
			return message;
		}
	}

	public void put(String message) {
		synchronized (lock) {
			// Wait until message has been retrieved.
			while (!empty) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
				}
			}
			empty = false; // Toggle status.
			this.message = message; // Store message.
			lock.notifyAll(); // Notify consumer that status has changed.
		}
	}
}
