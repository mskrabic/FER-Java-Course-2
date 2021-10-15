package oprpp2.hw02.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import oprpp2.hw02.message.AckMessage;
import oprpp2.hw02.message.ByeMessage;
import oprpp2.hw02.message.HelloMessage;
import oprpp2.hw02.message.InMessage;
import oprpp2.hw02.message.Message;
import oprpp2.hw02.message.OutMessage;

/**
 * Glavni razred koji predstavlja poslužiteljsku stranu aplikacije.
 * 
 * @author mskrabic
 *
 */
public class Main {	
	/**
	 * Sljedeći UID za dodjeljivanje.
	 */
	private static long UID;
	
	/**
	 * Maksimalna duljina poruke.
	 */
	private static final int MAX_LENGTH = 4000;
	
	/**
	 * Mapa spojenih klijenata po generiranom ključu.
	 */
	private static HashMap<Long, Client> clients = new HashMap<>();
	
	/**
	 * Priključna točka na kojoj poslužitelj sluša.
	 */
	private static DatagramSocket socket = null;
	
	/**
	 * Dretve za obradu komunikacije s klijentima.
	 */
	private static HashSet<ClientThread> threads = new HashSet<>();
	
	/**
	 * Glavna metoda za pokretanje poslužiteljskog programa.
	 * 
	 * @param args Prima 1 argument: port na kojem treba slušati.
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Dragi korisniče, očekivao sam port!");
			return;
		}
		int port = 0;
		try {
			port= Integer.parseInt(args[0]);	
		} catch (NumberFormatException e) {
			System.out.println("Nije broj: " + args[0]);
			return;
		}
		if (port < 1 || port > 65535) {
			System.out.println("Dragi korisniče, port mora biti između 1 i 65535.");
			return;
		}
		
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			System.out.println("Nije moguće otvoriti pristupnu točku: " + e.getMessage());
			return;
		}
		System.out.println("Poslužitelj sluša na portu " + port + ".");
		
		Random r = new Random();
		UID = r.nextLong();

		while(true) {
			byte[] buff = new byte[MAX_LENGTH];
			DatagramPacket packet = new DatagramPacket(buff, buff.length);
			try {
				socket.receive(packet);
			} catch(IOException e) {
				continue;
			}
			
			byte[] data = packet.getData();
			
			switch (data[0]) {
			case Message.HELLO:
				hello(packet);
				break;
			case Message.ACK:
				ack(packet);
				break;
			case Message.OUTMSG:
				out(packet);
				break;
			case Message.BYE:
				bye(packet);
			default:
				continue;
			}
		}
	}
	
	/**
	 * Pomoćna metoda za obradu HELLO poruke.
	 * 
	 * @param packet primljeni paket.
	 */
	private static void hello(DatagramPacket packet) {
		HelloMessage hello = HelloMessage.fromData(packet.getData());
		System.out.println("Primio sam HELLO(" + hello.getMessageNumber() + ", " 
						  	+ hello.getUsername() + ", " + hello.getKey() + ").");
		
		Client c = findClientByKeyAndAdress(hello.getKey(), packet.getAddress(), packet.getPort());
		if (c == null) {
			c = new Client(hello.getKey(), UID++, hello.getUsername(), packet.getAddress(), packet.getPort());
			clients.put(c.getUID(), c);			
			ClientThread ct = new ClientThread(c);
			threads.add(ct);
			ct.start();
		}
		
		AckMessage ack = new AckMessage(hello.getMessageNumber(), c.getUID());
		sendAck(ack, c.getAdress(), c.getPort());
	}
	
	/**
	 * Pomoćna metoda za obradu ACK poruke.
	 * 
	 * @param packet primljeni paket.
	 */
	private static void ack(DatagramPacket packet) {
		System.out.println("Primio sam ACK.");
		AckMessage ack = AckMessage.fromData(packet.getData());
		
		clients.get(ack.getUID()).putAck(ack);	
	}

	/**
	 * Pomoćna metoda za obradu OUTMSG poruke.
	 * 
	 * @param packet primljeni paket.
	 */
	private static void out(DatagramPacket packet) {
		OutMessage out = OutMessage.fromData(packet.getData());
		System.out.println("Primio sam OUTMSG(" + out.getMessageNumber() + ", " 
							+ out.getUID() + ", " + out.getMessageText() + ").");
		AckMessage ack = new AckMessage(out.getMessageNumber(), out.getUID());
		sendAck(ack, packet.getAddress(), packet.getPort());
		
		String sender = clients.get(out.getUID()).getUsername();
		for (Client c: clients.values()) {
			InMessage in = new InMessage(c.getMessageNumber(), sender, out.getMessageText());
			c.putInMessage(in);
			c.incrementMessageNumber();
		}
	}
	
	/**
	 * Pomoćna metoda za obradu BYE poruke.
	 * 
	 * @param packet primljeni paket.
	 */
	private static void bye(DatagramPacket packet) {
		ByeMessage bye = ByeMessage.fromData(packet.getData());
		System.out.println("Primio sam BYE(" + bye.getMessageNumber() + ", " + bye.getUID() + ").");
		long byeUID = bye.getUID();
		AckMessage ack = new AckMessage(bye.getMessageNumber(), byeUID);
		sendAck(ack, packet.getAddress(), packet.getPort());
		
		for (ClientThread ct: threads)
			if (ct.getClient().getUID() == byeUID) {
				ct.finish();
				clients.remove(ct.getClient().getUID());
				threads.remove(ct);
				System.out.println("trenutno korisnika: " + clients.size());
				System.out.println("trenutno dretvi: " + threads.size());
				return;
			}
	}

	/**
	 * Pomoćna metoda koja vraća klijenta s predanim ključem.
	 * 
	 * @param key ključ klijenta.
	 * 
	 * @return klijent, odnosno <code>null</code> ako takav klijent ne postoji.
	 */
	private static Client findClientByKeyAndAdress(long key, InetAddress adr, int port) {
		for (Client c: clients.values()) {
			if (c.getKey() == key && c.getAdress().equals(adr) && c.getPort() == port) {
				return c;
			}
		}
		return null;
	}

	/**
	 * Pomoćna metoda za slanje potvrde.
	 * 
	 * @param ackMessage potvrda.
	 * @param adr adresa klijenta.
	 * @param port port klijenta.
	 */
	private static void sendAck(AckMessage ackMessage, InetAddress adr, int port) {
		DatagramPacket p = ackMessage.toPacket();
		p.setAddress(adr);
		p.setPort(port);

		try {
			socket.send(p);
		} catch (IOException e) {
			System.out.println("Greška pri slanju potvrde!");
		}	
	}

}
