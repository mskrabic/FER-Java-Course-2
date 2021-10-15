package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Razred predstavlja element koji sadrži varijablu (ime varijable).
 * 
 * @author mskrabic
 *
 */
public class ElementVariable extends Element {
	
	/**
	 * Ime varijable.
	 */
	private String name;
	
	/**
	 * Konstruktor postavlja ime varijable unutar elementa.
	 * 
	 * @param name ime koje se želi postaviti.
	 */
	public ElementVariable(String name) {
		this.name = name;
	}
	
	@Override
	public String asText() {
		return name;
	}
}
