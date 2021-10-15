package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Razred predstavlja element koji sadrži String.
 * 
 * @author mskrabic
 *
 */
public class ElementString extends Element {
	/**
	 * Vrijednost elementa.
	 */
	private String value;
	
	/**
	 * Konstruktor koji inicijalizira String unutar elementa.
	 * 
	 * @param value String koji se želi postaviti.
	 */
	public ElementString(String value) {
		this.value = value;
	}
	
	@Override
	public String asText() {
		return value;
	}
	
	/**
	 * Nadjačanu metodu toString() koristimo pri rekonstrukciji dokumenta kako bi pravilno "escapali" specijalne znakove.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (char c : asText().toCharArray()) {
			if (c == '\n' || c == '\r' || c == '\t' || c == '\\' || c == '"') {
				sb.append("\\" + c);
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
}
