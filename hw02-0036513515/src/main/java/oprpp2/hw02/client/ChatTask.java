package oprpp2.hw02.client;

/**
 * Razred predstavlja zadatak dodavanja nove poruke u prozor.
 * 
 * @author mskrabic
 *
 */
public class ChatTask implements Runnable {
	
	/**
	 * Prozor za chat.
	 */
	private ChatWindow window;
	
	/**
	 * Korisničko ime pošiljatelja.
	 */
	private String sender;
	
	/**
	 * Tekst poruke.
	 */
	private String messageText;
	
	/**
	 * Konstruktor.
	 * @param window prozor za chat.
	 * @param sender korisničko ime pošiljatelja.
	 * @param messageText tekst poruke.
	 */
	public ChatTask(ChatWindow window, String sender, String messageText) {
		this.window = window;
		this.sender = sender;
		this.messageText = messageText;
	}

	@Override
	public void run() {
		window.appendMessage(sender, messageText);
	}
	
}