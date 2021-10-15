package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Razred predstavlja vršni član hijerarhije elemenata. Svi ostali elementi nasljeđuju ovaj razred.
 * 
 * @author mskrabic
 *
 */
public abstract class Element {
	
	/**
	 * Metoda vraća vrijednost elementa kao String.
	 * 
	 * @return vrijednost elementa.
	 */
	public abstract String asText();
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Element))
			return false;
		
		Element otherElement = (Element) other;
		
		return this.asText().equals(otherElement.asText());
	}

}
