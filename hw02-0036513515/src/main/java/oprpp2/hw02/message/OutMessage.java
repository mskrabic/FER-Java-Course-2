package oprpp2.hw02.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Razred modelira OUTMSG poruku, tj. poruku koju klijent šalje poslužitelju.
 * 
 * @author mskrabic
 *
 */
public class OutMessage extends Message {
	
	/**
	 * User ID pošiljatelja poruke.
	 */
	private long UID;
	
	/**
	 * Tekst poruke.
	 */
	private String messageText;
	
	/**
	 * Konstruktor.
	 * @param messageNumber redni broj poruke.
	 * @param UID user ID pošiljatelja.
	 * @param messageText tekst poruke.
	 */
	public OutMessage(long messageNumber, long UID, String messageText) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);

		try {
			dos.writeByte(Message.OUTMSG);
			dos.writeLong(messageNumber);
			dos.writeLong(UID);
			dos.writeUTF(messageText);
			dos.close();
		} catch (IOException e) {
			System.out.println("Greška pri stvaranju HELLO poruke.");
		}
		this.setUID(UID);
		this.setMessageText(messageText);
		this.setData(bos.toByteArray());
		this.setMessageNumber(messageNumber);
	}

	/**
	 * Metoda vraća tekst poruke.
	 * 
	 * @return tekst poruke.
	 */
	public String getMessageText() {
		return messageText;
	}

	/**
	 * Metoda postavlja tekst poruke.
	 * 
	 * @param messageText tekst poruke koji se želi postaviti.
	 */
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	/**
	 * Metoda postavlja UID pošiljatelja poruke.
	 * 
	 * @param UID user ID pošiljatelja.
	 */
	private void setUID(long UID) {
		this.UID = UID;
		
	}
	
	/**
	 * Metoda dohvaća UID pošiljatelja poruke.
	 * 
	 * @return user ID pošiljatelja.
	 */
	public long getUID() {
		return UID;
	}

	@Override
	public int getType() {
		return Message.OUTMSG;
	}

	/**
	 * Statička metoda "tvornica", stvara poruku na osnovu predanog polja bajtova.
	 * 
	 * @param data polje bajtova.
	 * @return poruka, odnosno <code>null</code> u slučaju pogreške.
	 */
	public static OutMessage fromData(byte[] data) {
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		DataInputStream dis = new DataInputStream(bis);
		long messageNumber, UID;
		String messageText;
		try {
			if (dis.readByte() != Message.OUTMSG) {
				throw new RuntimeException("Nije OUTMSG!");
			}
			messageNumber = dis.readLong();
			UID = dis.readLong();
			messageText = dis.readUTF();	
		} catch (IOException e) {
			throw new RuntimeException("Greška pri stvaranju HELLO poruke!");
		}
		OutMessage out = new OutMessage(messageNumber, UID, messageText);
		return out;
	}

}
