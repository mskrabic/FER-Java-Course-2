package oprpp2.hw02.message;

import java.net.DatagramPacket;

/**
 * Vršni razred koji modelira općenitu poruku.
 * 
 * @author mskrabic
 *
 */
public abstract class Message {
	/**
	 * Kod za HELLO poruku.
	 */
	public static final int HELLO = 1;
	
	/**
	 * Kod za ACK poruku.
	 */
	public static final int ACK = 2;
	
	/**
	 * Kod za BYE poruku.
	 */
	public static final int BYE = 3;
	
	/**
	 * Kod za OUTMSG poruku.
	 */
	public static final int OUTMSG = 4;
	
	/**
	 * Kod za INMSG poruku.
	 */
	public static final int INMSG = 5;
	
	/**
	 * Polje bajtova poruke.
	 */
	private byte[] data;
	
	/**
	 * Redni broj poruke.
	 */
	private long messageNumber;
	
	/**
	 * Metoda vraća polje bajtova poruke.
	 * 
	 * @return polje bajtova.
	 */
	public byte[] getData() {
		return data;
	}
	
	/**
	 * Metoda postavlja polje bajtova poruke.
	 * @param data polje bajtova koje se želi postaviti.
	 */
	public void setData(byte[] data) {
		this.data = data;
	}
	
	/**
	 * Metoda koja pretvara poruku u {@link DatagramPacket}
	 * 
	 * @return paket.
	 */
	public DatagramPacket toPacket() {
		return new DatagramPacket(data, data.length);
	}
	
	/**
	 * Metoda za postavljanje rednog broja poruke.
	 * 
	 * @param messageNumber broj poruke koji se želi postaviti.
	 */
	protected void setMessageNumber(long messageNumber) {
		this.messageNumber = messageNumber;
	}
	
	/**
	 * Metoda dohvaća redni broj poruke.
	 * 
	 * @return redni broj poruke.
	 */
	public long getMessageNumber() {
		return messageNumber;
	};
	
	/**
	 * Metoda vraća tip poruke.
	 * 
	 * @return tip poruke.
	 */
	public abstract int getType();
}
