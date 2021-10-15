package hr.fer.zemris.java.custom.collections;

/**
 * Sučelje koje modelira jednostavnu listu.
 * 
 * @author mskrabic
 *
 */
public interface List extends Collection {
	
	/**
	 * Metoda dohvaća element koji se nalazi na predanoj poziciji.
	 * 
	 * @param index pozicija s koje se želi dohvatiti element.
	 * 
	 * @return element s tražene pozicije.
	 */
	Object get(int index);
	
	/**
	 * Metoda ubacuje predani objekt u kolekciju na željenu poziciju.
	 * 
	 * @param value objekt koji se želi dodati u listu.
	 * 
	 * @param position pozicija na koju se želi dodati novi element.
	 */
	void insert(Object value, int position);
	
	/**
	 * Metoda vraća poziciju na kojoj se prvi put pojavljuje predani element, odnosno -1 ako element nije u listi.
	 * 
	 * @param value element čiju poziciju se želi saznati.
	 * 
	 * @return pozicija prvog pojavljivanja elementa, odnosno -1 ako element nije u listi.
	 */
	int indexOf(Object value);
	
	/**
	 * Metoda izbacuje element na predanoj poziciji iz liste.
	 * 
	 * @param index pozicija s koje se želi izbaciti element.
	 */
	void remove(int index);	
}
