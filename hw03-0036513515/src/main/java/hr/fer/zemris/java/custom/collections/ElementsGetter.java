package hr.fer.zemris.java.custom.collections;


/**
 * Sučelje koje se koristi za pristup elementima kolekcije.
 * 
 * @author mskrabic
 *
 */
public interface ElementsGetter {
	
	/**
	 * Metoda provjerava postoji li nepredanih elemenata u kolekciji.
	 * 
	 * @return <code>true</code> ako ElementsGetter nije dohvatio sve podatke, <code>false</code> inače.
	 */
	boolean hasNextElement();
	
	/**
	 * Metoda vraća sljedeći nepredani element iz kolekcije.
	 * 
	 * @return element kolekcije.
	 */
	Object getNextElement();
	
	/**
	 * Metoda poziva metodu <code>process()</code> predanog procesora nad svim preostalim elementima kolekcije.
	 * 
	 * @param p procesor čiju metodu <code>process()</code> se želi pozvati nad elementima kolekcije.
	 */
	default void processRemaining(Processor p) {
		while (this.hasNextElement()) {
			p.process(this.getNextElement());
		}
	}

}
