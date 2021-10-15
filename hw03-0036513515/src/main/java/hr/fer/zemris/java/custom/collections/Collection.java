package hr.fer.zemris.java.custom.collections;

/**
 * Sučelje koje predstavlja općenitu kolekciju <code>Object</code> elemenata.
 * 
 * @author mskrabic
 *
 */
public interface Collection {
	
	/**
	 * Metoda provjerava je li kolekcija prazna.
	 * 
	 * @return <code>true</code> ako je kolekcija prazna, <code>false</code> inače.
	 */
	default boolean isEmpty() {
		return (this.size() == 0);
	}
	
	/**
	 * Metoda vraća veličinu kolekcije, tj. broj elemenata koji su trenutno pohranjeni u njoj.
	 * 
	 * @return broj elemenata u kolekciji.
	 */
	int size();
	
	/**
	 * Metoda koja dodaje element u kolekciju.
	 * 
	 * @param value vrijednost koja treba biti dodana u kolekciju.
	 */
	void add(Object value);
	
	/**
	 * Metoda koja provjerava sadrži li kolekcija traženi element.
	 * 
	 * @param value vrijednost čiju prisutnost u kolekciji želimo ispitati.
	 * 
	 * @return <code>true</code> ako kolekcija sadrži traženu vrijednost, <code>false</code> inače.
	 */
	boolean contains(Object value);
	
	/**
	 * Metoda koja izbacuje traženi element iz kolekcije.
	 * 
	 * @param value element koji se želi izbaciti iz kolekcije
	 * 
	 * @return <code>true</code> ako kolekcija sadrži traženu vrijednost i izbaci jedno njeno pojavljivanje.
	 * iz kolekcije, <code>false</code> inače.
	 */
	boolean remove(Object value);
	
	/**
	 * Metoda koja vraća novo polje koje sadrži elemente kolekcije.
	 * 
	 * @return novo polje koje sadrži elemente kolekcije.
	 */
	Object[] toArray();
	
	/**
	 * Metoda koja nad svakim elementom kolekcije poziva <code>process()</code> metodu predanog procesora.
	 * 
	 * @param processor procesor čiju metodu <code>process()</code> se želi pozvati nad elementima kolekcije.
	 */
	default void forEach(Processor processor) {
		ElementsGetter getter = this.createElementsGetter();
		while (getter.hasNextElement()) {
			processor.process(getter.getNextElement());
		}
	}
	
	/**
	 * Metoda koja u trenutnu kolekciju dodaje sve elemente iz predane kolekcije.
	 * Predana kolekcija pri tome ostaje nepromjenena.
	 * 
	 * @param other kolekcija čije elemente se želi dodati u trenutnu kolekciju.
	 */
	default void addAll(Collection other) {
		
		/**
		 * Razred koji predstavlja procesor za dodavanje elemenata u kolekciju.
		 * 
		 * @author mskrabic
		 *
		 */
		class AddAllProcessor implements Processor {
			
			/**
			 * Metoda koja predanu vrijednost dodaje u kolekciju.
			 */
			@Override
			public void process(Object value) {
				add(value);
			}
		}
		AddAllProcessor p = new AddAllProcessor();
		other.forEach(p);
	}
	
	/**
	 * Metoda koja izbacuje sve elemente iz kolekcije.
	 */
	void clear();
	
	/**
	 * Metoda vraća instancu <code>ElementsGetter</code> koja služi za pristupanje podatcima kolekcije.
	 * 
	 * @return ElementsGetter
	 */
	ElementsGetter createElementsGetter();
	
	/**
	 * Metoda dodaje u trenutnu kolekciju sve elemente predane kolekcije koji zadovoljavaju uvjet koji provjerava predani <code>Tester</code>.
	 * Predana kolekcija ostaje nepromjenjena.
	 * 
	 * @param col kolekcija čije elemente se želi dodati u trenutnu kolekciju.
	 * @param tester tester koji provjerava uvjet na osnovu kojeg dodajemo elemente u kolekciju.
	 */
	default void addAllSatisfying(Collection col, Tester tester) {
		ElementsGetter getter = col.createElementsGetter();
		while (getter.hasNextElement()) {
			Object element = getter.getNextElement();
			if (tester.test(element))
				add(element);
		}
	}
}