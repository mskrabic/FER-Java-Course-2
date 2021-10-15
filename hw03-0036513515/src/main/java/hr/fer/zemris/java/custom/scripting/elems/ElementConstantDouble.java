package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Razred predstavlja element koji sadrži decimalni broj.
 * 
 * @author mskrabic
 *
 */
public class ElementConstantDouble extends Element {
	
	/**
	 * Vrijednost elementa.
	 */
	private double value;
	
	/**
	 * Konstruktor koji inicijalizira vrijednost elementa.
	 * 
	 * @param value vrijednost elementa koja se želi postaviti.
	 */
	public ElementConstantDouble(double value) {
		this.value = value;
	}
	
	/**
	 * Metoda vraća vrijednost elementa kao decimalni broj (double).
	 * 
	 * @return vrijednost čvora.
	 */
	public double getValue() {
		return value;
	}
	
	@Override
	public String asText() {
		return Double.toString(value);
	}
}
