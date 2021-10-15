package oprpp2.hw02.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.SwingUtilities;

import oprpp2.hw02.message.AckMessage;
import oprpp2.hw02.message.ByeMessage;
import oprpp2.hw02.message.HelloMessage;
import oprpp2.hw02.message.InMessage;
import oprpp2.hw02.message.Message;
import oprpp2.hw02.message.OutMessage;

/**
 * Glavni razred za pokretanje klijentske strane aplikacije.
 
 * @author mskrabic
 */
public class Main {
	/**
	 * Najveći broj retransmisija prije prekidanja veze.
	 */
	public static final int RETRANSMISSIONS = 10;
	
	/**
	 * Vrijeme koje se čeka na potvrdu prije retransmisije (u milisekundama).
	 */
	public static final int TIMEOUT = 5000;
	
	/**
	 * Najveća veličina polja sadržanog u dolaznim paketima.
	 */
	public static final int MAX_SIZE = 4000;
	
	/**
	 * Broj HELLO poruke.
	 */
	private static final long HELLO_NUMBER = 0;
	
	/**
	 * Broj zadnje pristigle poruke.
	 */
	private static long inMessageNumber = 1;
	
	/**
	 * User ID klijenta.
	 */
	private static long UID;
	
	/**
	 * Priključna točka preko koje klijent komunicira sa poslužiteljem.
	 */
	private static DatagramSocket socket = null;
	
	/**
	 * Zastavica za prekid rada klijenta.
	 */
	private static AtomicBoolean exit = new AtomicBoolean(false);
	
	/**
	 * Adresa poslužitelja.
	 */
	private static InetAddress adr = null;
	
	/**
	 * Port na kojem poslužitelj sluša.
	 */
	private static int port = 0;
	
	/**
	 * GUI aplikacije - prozor za chat.
	 */
	private static ChatWindow window = null;
	
	/**
	 * Pomoćna dretva za slanje odlaznih poruka.
	 */
	private static SenderThread sender;

	/**
	 * Glavna metoda za pokretanje klijentskog programa.
	 * 
	 * @param args Prima 3 argumenta: IP adresu i port servera te klijentovo korisničko ime.
	 */
	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.println("Trebam 3 argumenta: IP adresu i port servera te Vaše korisničko ime!");
			return;
		}

		adr = null;
		try {
			adr = InetAddress.getByName(args[0]);
		} catch (UnknownHostException e) {
			System.out.println("Ne može se razriješiti: " + args[0]);
		}

		port = Integer.parseInt(args[1]);
		String username = args[2].replaceAll("\"", "");

		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			System.out.println("Ne mogu otvoriti pristupnu točku!");
			return;
		}
		
		Random r = new Random();
		long key = r.nextLong();
		HelloMessage hello = new HelloMessage(HELLO_NUMBER, username, key);
		
		AckMessage ackMessage = sendMessage(hello);
		
		if (ackMessage != null) {
			UID = ackMessage.getUID();
			System.out.println("Veza uspostavljena. Dobili smo UID: " + UID);
			openChatWindow(username);
			chat();			
		}
	
		System.out.println("Veza prekinuta.");
		socket.close();
		return;		
	}
	
	/**
	 * Pomoćna metoda za otvaranje prozora za chat.
	 * 
	 * @param username korisničko ime klijenta.
	 */
	private static void openChatWindow(String username) {
		SwingUtilities.invokeLater(() -> {
			window = new ChatWindow(username);
			window.setVisible(true);
		});
		
	}

	/**
	 * Metoda koja se izvodi od uspostave veze do prekida komunikacije.
	 * Klijent u njoj obrađuje pristigle poruke te gura pristigle poruke i potvrde u redove pomoćne dretve.
	 */
	private static void chat() {
		sender = new SenderThread(socket, adr, port);
		sender.start();
		while (!exit.get()) {		
			byte[] buf = new byte[MAX_SIZE];
			DatagramPacket inPacket = new DatagramPacket(buf, buf.length);
			try {
				socket.receive(inPacket);
			} catch (IOException e) {
				continue;
			}
			AckMessage ackMessage;
			switch(inPacket.getData()[0]) {
			case Message.INMSG:
				InMessage inMessage = InMessage.fromData(inPacket.getData());
				System.out.println("Šaljem potvrdu za primljeni INMSG(" + inMessage.getMessageNumber() + ", " + inMessage.getSender() + ", " + inMessage.getMessageText() + ")");
				ackMessage = new AckMessage(inMessage.getMessageNumber(), UID);
				sendAck(ackMessage);
				if (inMessage.getMessageNumber() >= inMessageNumber) {
					SwingUtilities.invokeLater(new ChatTask(window, inMessage.getSender(), inMessage.getMessageText()));	
					inMessageNumber++;					
				}
				break;
			case Message.ACK:
				ackMessage = AckMessage.fromData(inPacket.getData());
				sender.putAckMessage(ackMessage);
				break;
			}
		}
		exit();
	}
	
	/**
	 * Pomoćna metoda koja šalje poruku i vraća potvrdu, ako je dobije.
	 * @param m poruka koja se želi poslati.
	 * @return {@link AckMessage} potvrda za poslanu poruku, <code>null</code> u slučaju pogreške.
	 */
	private static AckMessage sendMessage(Message m) {
		int counter = 0;
		DatagramPacket p = m.toPacket();
		p.setAddress(adr);
		p.setPort(port);
		while(counter < RETRANSMISSIONS) {
			counter++;			
			try {
				socket.send(p);
			} catch (IOException e) {
				System.out.println("Greška pri slanju poruke. Prekidam vezu...");
				break;
			}
			byte[] buf = new byte[AckMessage.ACK_SIZE];
			DatagramPacket ack = new DatagramPacket(buf, buf.length);
			try {
				socket.setSoTimeout(TIMEOUT);
			} catch (SocketException e) {
				System.out.println("Greška pri postavljanju timeouta. Prekidam vezu...");
				break;
			}
			try {
				socket.receive(ack);
			} catch (SocketTimeoutException e) {
				continue;
			} catch (IOException e) {
				System.out.println("I/O pogreška pri primanju ACK paketa. Prekidam vezu...");
				break;
			}
			AckMessage ackMessage = AckMessage.fromData(ack.getData());
			if (ackMessage.getMessageNumber() != m.getMessageNumber()) {
				System.out.println("Primljena kriva potvrda. Prekidam vezu...");
				break;
			}
			return ackMessage;
		}
		return null;
		
	}
	
	/**
	 * Pomoćna metoda za slanje potvrde poslužitelju.
	 * @param ackMessage potvrda koja se želi poslati.
	 */
	private static void sendAck(AckMessage ackMessage) {
		DatagramPacket p = ackMessage.toPacket();
		p.setAddress(adr);
		p.setPort(port);

		try {
			socket.send(p);
		} catch (IOException e) {
			System.out.println("Greška pri slanju potvrde!");
		}	
	}


	/**
	 * Metoda koja generira {@link OutMessage} s predanim tekstom i stavlja ju u red za slanje.
	 * @param messageText tekst poruke.
	 */
	public static void generateOutMessage(String messageText) {
		OutMessage out = new OutMessage(sender.getMessageNumber(), UID, messageText);
		sender.putMessage(out);
	}

	/**
	 * Metoda za prekid komunikacije, tj. slanje BYE poruke.
	 */
	public static void exit() {
		ByeMessage bye = new ByeMessage(sender.getMessageNumber(), UID);
		sender.putMessage(bye);
	}
	
	/**
	 * Metoda za postavljanje zastavice za prekid komunikacije.
	 */
	public static void setExit() {
		exit.set(true);
	}
}
