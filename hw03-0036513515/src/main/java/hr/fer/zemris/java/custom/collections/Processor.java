package hr.fer.zemris.java.custom.collections;

/**
 * Sučelje koje predstavlja jednostavni model procesora.
 * 
 * @author mskrabic
 */
public interface Processor {
	
	/**
	 * Metoda koja obrađuje predanu vrijednost.
	 * 
	 * @param value vrijednost koju se želi obraditi.
	 */
	void process(Object value);
}
