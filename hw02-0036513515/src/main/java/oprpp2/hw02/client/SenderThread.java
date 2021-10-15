package oprpp2.hw02.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import oprpp2.hw02.message.AckMessage;
import oprpp2.hw02.message.Message;

/**
 * Pomoćna dretva koja šalje klijentove poruke i čeka na primitak potvrde.
 * 
 * @author mskrabic
 *
 */
public class SenderThread extends Thread {
	/**
	 * Redni broj zadnje poruke.
	 */
	private int messageNumber = 1;
	
	/**
	 * Red poruka koje treba poslati.
	 */
	private LinkedBlockingQueue<Message> messages;
	
	/**
	 * Red primljenih potvrda.
	 */
	private LinkedBlockingQueue<AckMessage> acks;
	
	/**
	 * Najveći broj retransmisija prije prekidanja veze.
	 */
	private static final int RETRANSMISSIONS = 10;
	
	/**
	 * Vrijeme koje se čeka prije retransmisije. (u sekundama)
	 */
	private static final int TIMEOUT = 5;
	
	/**
	 * Pristupna točka preko koje klijent komunicira s poslužiteljem.
	 */
	private DatagramSocket socket;
	
	/**
	 * Adresa poslužitelja.
	 */
	private InetAddress adr;
	
	/**
	 * Port na kojem poslužitelj sluša.
	 */
	private int port;
	
	public SenderThread(DatagramSocket socket, InetAddress adr, int port) {
		super();
		messages = new LinkedBlockingQueue<>();
		acks = new LinkedBlockingQueue<>();
		this.socket = socket;
		this.adr = adr;
		this.port = port;
		this.setDaemon(true);
	}
	
	/**
	 * Metoda dohvaća broj zadnje poslane poruke.
	 * 
	 * @return broj zadnje poslane poruke.
	 */
	public int getMessageNumber() {
		return messageNumber;
	}
	
	/**
	 * Metoda za dodavanje potvrde u red pristiglih potvrda.
	 * 
	 * @param ack potvrda koja se želi dodati u red.
	 */
	public void putAckMessage(AckMessage ack) {
		acks.add(ack);
	}
	
	/**
	 * Metoda za dodavanje poruke u red poruka za slanje.
	 * 
	 * @param out poruka koja se želi dodati u red.
	 */
	public void putMessage(Message m) {
		messages.add(m);
	}
	
	@Override
	public void run() {
		while(true) {
			Message m;
			while(true) {
				try {
					m = messages.take();
					break;
				} catch (InterruptedException e) {}				
			}
			DatagramPacket outPacket = new DatagramPacket(m.getData(), m.getData().length);
			outPacket.setAddress(adr);
			outPacket.setPort(port);
			
			int counter = 0;
			while(counter < RETRANSMISSIONS) {
				counter++;
				try {
					socket.send(outPacket);
				} catch (IOException e) {
					System.out.println("Greška pri slanju poruke. Prekidam vezu...");
					break;
				}
				
				AckMessage ack;
				while(true) {
					try {
						ack = acks.poll(TIMEOUT, TimeUnit.SECONDS);
						break;
					} catch (InterruptedException e) {}					
				}
				if (ack == null || ack.getMessageNumber() != m.getMessageNumber())
					continue;
				System.out.println("Dobio sam ispravnu potvrdu.");
				messageNumber++;
				break;
			}
		}
	}
	
}
