package oprpp2.hw02.server;

import java.net.InetAddress;
import java.util.concurrent.LinkedBlockingQueue;

import oprpp2.hw02.message.AckMessage;
import oprpp2.hw02.message.InMessage;

/**
 * Razred koji predstavlja strukturu podataka koju za svakog spojenog klijenta čuva poslužitelj.
 * 
 * @author mskrabic
 *
 */
public class Client {
	
	/**
	 * User ID klijenta.
	 */
	private long UID;
	
	/**
	 * Slučajni ključ koji je klijent generirao.
	 */
	private long key;
	
	/**
	 * Korisničko ime klijenta.
	 */
	private String username;
	
	/**
	 * Adresa klijenta.
	 */
	private InetAddress address;
	
	/**
	 * Port s kojeg klijent komunicira s poslužiteljem.
	 */
	private int port;
	
	/**
	 * Red pristiglih potvrda.
	 */
	private LinkedBlockingQueue<AckMessage> acks;
	
	/**
	 * Red dolaznih poruka.
	 */
	private LinkedBlockingQueue<InMessage> inMessages;
	
	/**
	 * Redni broj poruke.
	 */
	private long messageNumber = 1;
	
	/**
	 * Konstruktor.
	 * 
	 * @param key slučajni ključ klijenta.
	 * @param UID user ID klijenta.
	 * @param username korisničko ime.
	 * @param adress adresa.
	 * @param port port.
	 */
	public Client(long key, long UID, String username, InetAddress address, int port) {
		this.UID = UID;
		this.key = key;
		this.username = username;
		this.address = address;
		this.port = port;
		this.acks = new LinkedBlockingQueue<>();
		this.inMessages = new LinkedBlockingQueue<>();
	}
	
	/**
	 * Metoda uvećava redni broj poruke.
	 */
	public void incrementMessageNumber() {
		messageNumber++;
	}
	
	/**
	 * Metoda dohvaća redni broj poruke.
	 * 
	 * @return redni broj poruke.
	 */
	public long getMessageNumber() {
		return messageNumber;
	}
	
	/**
	 * Metoda stavlja potvrdu u red primljenih potvrda.
	 * 
	 * @param ack potvrda koja se želi dodati u red.
	 */
	public void putAck(AckMessage ack) {
		acks.add(ack);
	}
	
	/**
	 * Metoda koja stavlja poruku u red primljenih poruka.
	 * 
	 * @param in poruka.
	 */
	public void putInMessage(InMessage in) {
		inMessages.add(in);
	}

	/**
	 * Metoda dohvaća red primljenih potvrda.
	 * 
	 * @return red primljenih potvrda.
	 */
	public LinkedBlockingQueue<AckMessage> getAcks() {
		return acks;
	}

	/**
	 * Metoda dohvaća red primljenih poruka.
	 * 
	 * @return red primljenih poruka.
	 */
	public LinkedBlockingQueue<InMessage> getInMessages() {
		return inMessages;
	}

	/**
	 * Metoda dohvaća adresu klijenta.
	 * 
	 * @return adresa klijenta.
	 */
	public InetAddress getAdress() {
		return address;
	}

	/**
	 * Metoda postavlja adresu klijenta.
	 * 
	 * @param adress adresa koja se želi postaviti.
	 */
	public void setAdress(InetAddress address) {
		this.address = address;
	}

	/**
	 * Metoda dohvaća port klijenta.
	 * 
	 * @return port klijenta.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Metoda postavlja port klijenta.
	 * 
	 * @param port port klijenta.
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Metoda dohvaća UID klijenta.
	 * 
	 * @return UID klijenta.
	 */
	public long getUID() {
		return UID;
	}

	/**
	 * Metoda postavlja UID klijenta.
	 * 
	 * @param UID user ID koji se želi postaviti.
	 */
	public void setUID(long UID) {
		this.UID = UID;
	}
	
	/**
	 * Metoda dohvaća ključ klijenta.
	 * 
	 * @return ključ klijenta.
	 */
	public long getKey() {
		return key;
	}

	/**
	 * Metoda postavlja ključ klijenta.
	 * 
	 * @param key ključ koji se želi postaviti.
	 */
	public void setKey(long key) {
		this.key = key;
	}

	/**
	 * Metoda dohvaća korisničko ime klijenta.
	 * 
	 * @return korisničko ime klijenta.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Metoda postavlja korisničko ime klijenta.
	 * 
	 * @param username korisničko ime koje se želi postaviti.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

}
