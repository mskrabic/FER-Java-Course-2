package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Razred predstavlja element koji sadrži funkciju, tj. ime funkcije.
 * 
 * @author mskrabic
 *
 */
public class ElementFunction extends Element {
	
	/**
	 * Ime funkcije.
	 */
	private String name;
	
	/**
	 * Konstruktor koji inicijalizira ime funkcije.
	 * 
	 * @param name ime koje se želi postaviti.
	 */
	public ElementFunction(String name) {
		this.name = name;
	}
	
	@Override
	public String asText() {
		return name;
	}
}