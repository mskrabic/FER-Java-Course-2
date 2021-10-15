package oprpp2.hw06.util;

import java.security.MessageDigest;
import java.util.HashMap;

/**
 * Pomoćni razred koji se koristi pri hashiranju lozinki i kodiranju.
 * 
 * @author mskrabic
 *
 */
public class Util {
	/**
	 * Heksadekadske znamenke A-F.
	 */
	private static final HashMap<Character, Integer> HEX_DIGITS;
	static {
		HEX_DIGITS = new HashMap<>();
		HEX_DIGITS.put('a', 10);
		HEX_DIGITS.put('A', 10);
		HEX_DIGITS.put('b', 11);
		HEX_DIGITS.put('B', 11);
		HEX_DIGITS.put('c', 12);
		HEX_DIGITS.put('C', 12);
		HEX_DIGITS.put('d', 13);
		HEX_DIGITS.put('D', 13);
		HEX_DIGITS.put('e', 14);
		HEX_DIGITS.put('E', 14);
		HEX_DIGITS.put('f', 15);
		HEX_DIGITS.put('F', 15);
	}
	
	/**
	 * Metoda koja hex-enkodirani string pretvara u polje bajtova.
	 * 
	 * @param keyText ulazni string.
	 * 
	 * @return polje bajtova koje string predstavlja.
	 */
	public static byte[] hexToByte(String keyText) {
		if (keyText.length() % 2 != 0) {
			throw new IllegalArgumentException("Invalid hex-encoded string!");
		}
		
		byte[] result = new byte[keyText.length()/2];
		for (int i = 0; i < result.length; i++) {
			result[i] = calculateByte(keyText.charAt(2*i), keyText.charAt(2*i+1));
		}
		
		return result;
	}
	
	/**
	 * Metoda koja pretvara niz bajtova u hex-enkodirani string.
	 * 
	 * @param byteArray polje bajtova.
	 * 
	 * @return hex-enkodirani string.
	 */
	public static String byteToHex(byte[] byteArray) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < byteArray.length; i++) {
			
			byte first = (byte)((byteArray[i]>>>4) & 15);
			byte second = (byte)(byteArray[i] & 15);
			sb.append(evaluateByte(first));
			sb.append(evaluateByte(second));
		}
		
		return sb.toString();
	}
	
	/**
	 * Pomoćna metoda koja računa vrijednost bajta zapisanu kao 2 heksadekadske znamenke.
	 * 
	 * @param first prva znamenka.
	 * @param second druga znamenka.
	 * 
	 * @return bajt.
	 */
	private static byte calculateByte(char first, char second) {
		byte result = (byte)(evaluateChar(first) << 4);
		result |= (byte)evaluateChar(second);
		
		return result;
	}
	
	/**
	 * Pomoćna metoda koja vraća dekadsku vrijednost heksadekadske znamenke.
	 * 
	 * @param c znak koji predstavlja heksadekadsku znamenku.
	 * 
	 * @return dekadska vrijednost znamenke.
	 */
	private static int evaluateChar(char c) {
		if (Character.isDigit(c)) {
			return Character.getNumericValue(c);
		} else if (HEX_DIGITS.containsKey(Character.valueOf(c))) {
			return HEX_DIGITS.get(Character.valueOf(c)).intValue();
		}
		
		throw new IllegalArgumentException("Invalid character: " + c);
	}
	
	
	/**
	 * Pomoćna metoda koja iz vrijednosti bajta računa znak heksadekadske znameke.
	 * 
	 * @param b bajt.
	 * 
	 * @return string koji predstavlja heksadekadsku znamenku.
	 */
	private static String evaluateByte(byte b) {
		StringBuilder sb = new StringBuilder();
		if ((int)b < 10) {
			sb.append((int)b);
		} else if (HEX_DIGITS.containsValue((int) b)) {
			HEX_DIGITS.entrySet().stream()
				.filter(e -> e.getValue() == (int)b && Character.isLowerCase(e.getKey()))
				.forEach(e -> sb.append(e.getKey()));
		} else {
			throw new IllegalArgumentException("Invalid byte: " + b);
		}
		
		return sb.toString();
	}

	/**
	 * Metoda računa SHA-1 sažetak lozinke.
	 * 
	 * @param password	lozinka.
	 * 
	 * @return SHA-1 sažetak lozinke.
	 */
	public static byte[] hash(String password) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		md.update(password.getBytes());
		return md.digest();
	}
	
	/**
	 * Pomoćna metoda koja <code>null</code> stringove konvertira u prazne stringove, što je
	 * puno pogodnije za uporabu na webu.
	 * 
	 * @param s string
	 * @return primljeni string ako je različit od <code>null</code>, prazan string inače.
	 */
	public static String prepareString(String s) {
		if(s==null) return "";
		
		return s.trim();
	}

}
