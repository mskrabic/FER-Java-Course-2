package oprpp2.hw02.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Razred modelira ACK poruku, tj. potvrdu kojom se koriste klijent i poslužitelj u komunikaciji.
 * 
 * @author mskrabic
 *
 */
public class AckMessage extends Message {
	
	/**
	 * User ID.
	 */
	private long UID;
	
	/**
	 * Veličina (u bajtovima) jedne potvrde.
	 */
	public static final int ACK_SIZE = 17;
	
	/**
	 * Konstruktor.
	 * 
	 * @param messageNumber broj potvrde.
	 * @param UID user ID potvrde.
	 */
	public AckMessage(long messageNumber, long UID) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeByte(Message.ACK);
			dos.writeLong(messageNumber);
			dos.writeLong(UID);
			dos.close();
		} catch (IOException e) {
			System.out.println("Greška pri stvaranju ACK paketa!");
		}
		this.setData(bos.toByteArray());
		this.setMessageNumber(messageNumber);
		this.setUID(UID);
	}
	
	/**
	 * Metoda dohvaća UID potvrde.
	 * 
	 * @return UID potvrde.
	 */
	public long getUID() {
		return UID;
	}
	
	/**
	 * Metoda postavlja UID potvrde.
	 * 
	 * @param UID user ID koji se želi postaviti.
	 */
	private void setUID(long UID) {
		this.UID = UID;
	}

	@Override
	public int getType() {
		return Message.ACK;
	}
	
	/**
	 * Statička metoda "tvornica", stvara potvrdu iz predanog polja bajtova.
	 * 
	 * @param data polje bajtova potvrde.
	 * 
	 * @return potvrda, odnosno <code>null</code> u slučaju greške.
	 */
	public static AckMessage fromData(byte[] data) {
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		DataInputStream dis = new DataInputStream(bis);
		long messageNumber, UID;
		try {
			int code = dis.readByte();
			if (code != Message.ACK) {
				System.out.println("Nije ACK! "  + code);
				return null;
			}
			messageNumber = dis.readLong();
			UID = dis.readLong();
			dis.close();
		} catch (IOException e) {
			System.out.println("Greška pri čitanju ACK paketa!");
			throw new RuntimeException();
		}
		return new AckMessage(messageNumber, UID);
	}


}
