package oprpp2.hw02.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Razred modelira HELLO poruku, tj. poruku za početak komunikacije.
 * 
 * @author mskrabic
 *
 */
public class HelloMessage extends Message {
	
	/**
	 * Slučajni ključ korisnika.
	 */
	private long key;
	
	/**
	 * Korisničko ime.
	 */
	private String username;
	
	
	/**
	 * Konstruktor.
	 * @param messageNumber broj poruke.
	 * @param username korisničko ime.
	 * @param key slučajni ključ.
	 */
	public HelloMessage(long messageNumber, String username, long key) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);

		try {
			dos.writeByte(Message.HELLO);
			dos.writeLong(messageNumber);
			dos.writeUTF(username);
			dos.writeLong(key);
			dos.close();
		} catch (IOException e) {
			System.out.println("Greška pri stvaranju HELLO poruke.");
		}
		this.setData(bos.toByteArray());
		this.setMessageNumber(messageNumber);
		this.setUsername(username);
		this.setKey(key);
	}
	
	/**
	 * Metoda postavlja korisničko ime.
	 * 
	 * @param username korisničko ime koje se želi postaviti.
	 */
	private void setUsername(String username) {
		this.username = username;		
	}
	
	/**
	 * Metoda vraća korisničko ime pošiljatelja.
	 * 
	 * @return korisničko ime.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Metoda postavlja ključ poruke.
	 * 
	 * @param key ključ koji se želi postaviti.
	 */
	public void setKey(long key) {
		this.key = key;
	}
	
	/**
	 * Metoda vraća ključ poruke.
	 * 
	 * @return ključ.
	 */
	public long getKey() {
		return key;
	}

	@Override
	public int getType() {
		return Message.HELLO;
	}

	/**
	 * Statička metoda "tvornica" koja stvara poruku iz predanog polja bajtova.
	 * 
	 * @param data polje bajtova poruke.
	 * 
	 * @return poruka, odnosno <code>null</code> u slučaju pogreške.
	 */
	public static HelloMessage fromData(byte[] data) {
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		DataInputStream dis = new DataInputStream(bis);
		long messageNumber, key;
		String username;
		try {
			if (dis.readByte() != Message.HELLO) {
				throw new RuntimeException("Nije HELLO!");
			}
			messageNumber = dis.readLong();
			username = dis.readUTF();
			key = dis.readLong();	
		} catch (IOException e) {
			throw new RuntimeException("Greška pri stvaranju HELLO poruke!");
		}
		HelloMessage hello = new HelloMessage(messageNumber, username, key);
		return hello;
	}

}
