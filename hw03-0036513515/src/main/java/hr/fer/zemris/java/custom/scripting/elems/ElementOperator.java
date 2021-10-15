package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Razred predstavlja element koji sadrži operator.
 * 
 * @author mskrabic
 *
 */
public class ElementOperator extends Element {
	
	/**
	 * Simbol operatora.
	 */
	private String symbol;
	
	/**
	 * Konstruktor koji postavlja simbol operatora.
	 * 
	 * @param symbol simbol koji se želi postaviti.
	 */
	public ElementOperator(String symbol) {
		this.symbol = symbol;
	}
	
	@Override
	public String asText() {
		return symbol;
	}
}