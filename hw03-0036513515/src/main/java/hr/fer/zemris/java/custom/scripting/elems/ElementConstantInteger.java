package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Razred predstavlja element koji sadrži cijeli broj.
 * 
 * @author mskrabic
 *
 */
public class ElementConstantInteger extends Element {
	/**
	 * Vrijednost elementa.
	 */
	private int value;
	
	/**
	 * Konstruktor koji inicijalizira vrijednost elementa.
	 * 
	 * @param value vrijednost koja se želi postaviti.
	 */
	public ElementConstantInteger(int value) {
		this.value = value;
	}
	
	/**
	 * Metoda vraća vrijednost elementa, kao cijeli broj (int).
	 * 
	 * @return vrijednost elementa.
	 */
	public int getValue() {
		return value;
	}
	
	@Override
	public String asText() {
		return Integer.toString(value);
	}
}
