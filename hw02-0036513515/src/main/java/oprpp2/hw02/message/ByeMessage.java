package oprpp2.hw02.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Razred modelira BYE poruku, tj. poruku za prekid komunikacije.
 * 
 * @author mskrabic
 *
 */
public class ByeMessage extends Message {
	
	/**
	 * User ID klijenta koji je poslao poruku.
	 */
	private long UID;

	/**
	 * Konstruktor.
	 * @param messageNumber broj poruke.
	 * @param UID user ID.
	 */
	public ByeMessage(long messageNumber, long UID) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeByte(Message.BYE);
			dos.writeLong(messageNumber);
			dos.writeLong(UID);
			dos.close();
		} catch (IOException e) {
			System.out.println("Greška pri stvaranju BYE paketa!");
		}
		this.setData(bos.toByteArray());
		this.setMessageNumber(messageNumber);
		this.setUID(UID);
	}

	@Override
	public int getType() {
		return Message.BYE;
	}
	
	/**
	 * Metoda postavlja UID pošiljatelja poruke.
	 * @param UID user ID koji se želi postaviti.
	 */
	private void setUID(long UID) {
		this.UID = UID;
	}
	
	/**
	 * Metoda koja vraća UID pošiljatelja poruke.
	 * @return
	 */
	public long getUID() {
		return UID;
	}
	/**
	 * Statička metoda "tvornica", stvara poruku iz predanog polja bajtova.
	 * 
	 * @param data polje bajtova poruka.
	 * 
	 * @return poruka, odnosno <code>null</code> u slučaju greške.
	 */
	public static ByeMessage fromData(byte[] data) {
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		DataInputStream dis = new DataInputStream(bis);
		long messageNumber, UID;
		try {
			if (dis.readByte() != Message.BYE) {
				throw new RuntimeException("Nije BYE!");
			}
			messageNumber = dis.readLong();
			UID = dis.readLong();	
		} catch (IOException e) {
			throw new RuntimeException("Greška pri stvaranju BYE poruke!");
		}
		ByeMessage bye = new ByeMessage(messageNumber, UID);
		return bye;
	}

}
