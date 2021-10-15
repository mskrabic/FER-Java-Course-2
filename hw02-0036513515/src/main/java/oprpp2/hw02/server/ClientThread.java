package oprpp2.hw02.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import oprpp2.hw02.message.AckMessage;
import oprpp2.hw02.message.InMessage;

public class ClientThread extends Thread {
	
	private static final int RETRANSMISSIONS = 10;
	private static final long TIMEOUT = 5;
	private Client c;
	private DatagramSocket socket;
	private AtomicBoolean exit = new AtomicBoolean(false);
	
	public ClientThread(Client c) {
		this.c = c;
		this.setDaemon(true);

	}

	@Override
	public void run() {
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			System.out.println("Ne mogu otvoriti pristupnu točku!");
			return;
		}
		while(true) {
			InMessage in;
			while(true) {
				try {
					in = c.getInMessages().take();
					break;
				} catch (InterruptedException e) {}				
			}
			if (exit.get())
				break;
			DatagramPacket inPacket = new DatagramPacket(in.getData(), in.getData().length);
			inPacket.setAddress(c.getAdress());
			inPacket.setPort(c.getPort());
			
			int counter = 0;
			while(counter < RETRANSMISSIONS) {
				counter++;
				try {
					socket.send(inPacket);
				} catch (IOException e) {
					System.out.println("Greška pri slanju poruke. Prekidam vezu...");
					break;
				}
				
				AckMessage ack;
				while(true) {
					try {
						ack = c.getAcks().poll(TIMEOUT, TimeUnit.SECONDS);
						break;
					} catch (InterruptedException e) {}					
				}
				if (ack == null || ack.getMessageNumber() != in.getMessageNumber()) {
					System.out.println("Dretva korisnika: " + c.getUsername() + " nije dobila potvrdu za INMSG("
										+in.getMessageNumber() + ", " + in.getSender() + ", " + in.getMessageText() + ").");
					continue;
				}
				break;
			}
		}
		System.out.println(c.getUsername() + "(" + c.getUID() + ") završio s radom.");
	}

	public Client getClient() {
		return c;
	}

	public void finish() {
		c.putInMessage(new InMessage(-1, "EXIT", "EXIT"));
		exit.set(true);
		
	}
}
