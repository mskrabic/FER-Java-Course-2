package oprpp2.hw02.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Razred modelira INMSG, tj. ulaznu poruku koju poslužitelj šalje klijentima.
 * 
 * @author mskrabic
 *
 */
public class InMessage extends Message {
	
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
	 * 
	 * @param messageNumber redni broj poruke.
	 * @param sender korisničko ime pošiljatelja.
	 * @param messageText tekst poruke.
	 */
	public InMessage(long messageNumber, String sender, String messageText) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeByte(Message.INMSG);
			dos.writeLong(messageNumber);
			dos.writeUTF(sender);
			dos.writeUTF(messageText);
		} catch (IOException e) {
			System.out.println("Greška pri stvaranju INMSG!");
		}
		this.setData(bos.toByteArray());
		this.setMessageNumber(messageNumber);
		this.setSender(sender);
		this.setMessageText(messageText);
	}
	
	/**
	 * Metoda postavlja tekst poruke.
	 * 
	 * @param messageText tekst koji se želi postaviti.
	 */
	private void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	/**
	 * Metoda postavlja korisničko ime pošiljatelja.
	 * @param sender korisničko ime pošiljatelja.
	 */
	private void setSender(String sender) {
		this.sender = sender;
	}
	
	/**
	 * Metoda koja vraća tekst poruke.
	 * 
	 * @return tekst poruke.
	 */
	public String getMessageText() {
		return this.messageText;
	}

	
	/**
	 * Metoda koja vraća korisničko ime pošiljatelja.
	 * 
	 * @return korisničko ime pošiljatelja.
	 */
	public String getSender() {
		return this.sender;
	}

	@Override
	public int getType() {
		return Message.INMSG;
	}
	
	/**
	 * Statička metoda "tvornica" koja stvara poruku iz predanog polja bajtova.
	 * 
	 * @param data polje bajtova poruke.
	 * 
	 * @return poruka, odnosno <code>null</code> u slučaju pogreške.
	 */
	public static InMessage fromData(byte[] data) {
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		DataInputStream dis = new DataInputStream(bis);
		long messageNumber;
		String sender, messageText;
		try {
			int code = dis.readByte();
			if (code != Message.INMSG) {
				System.out.println(code);
				throw new RuntimeException("Nije INMSG!");
			}
			messageNumber = dis.readLong();
			sender = dis.readUTF();
			messageText = dis.readUTF();
			dis.close();
		} catch (IOException e) {
			System.out.println("Greška pri čitanju INMSG paketa!");
			throw new RuntimeException();
		}
		return new InMessage(messageNumber, sender, messageText);
	}



}
