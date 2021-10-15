package hr.fer.zemris.java.custom.collections;

/**
 * Razred koji predstavlja objektni stog. Koristi <code>ArrayIndexedCollection</code> za pohranu elemenata.
 * 
 * @author mskrabic
 */
public class ObjectStack {
	
	/**
	 * Kolekcija koja se interno koristi za pohranu elemenata.
	 */
	private ArrayIndexedCollection data;
	
	/**
	 * Pretpostavljeni konstruktor koji inicijalizira prazni stog.
	 */
	public ObjectStack() {
		this.data = new ArrayIndexedCollection();
	}
	
	/**
	 * Metoda provjerava je li stog prazan.
	 * 
	 * @return <code>true</code> ako je stog prazan, <code>false</code> inače.
	 */
	public boolean isEmpty() {
		return data.isEmpty();
	}
	
	/**
	 * Metoda vraća broj elemenata trenutno pohranjenih na stogu.
	 * 
	 * @return broj elemenata na stogu.
	 */
	public int size() {
		return data.size();
	}
	
	/**
	 * Metoda dodaje element na vrh stoga.
	 * 
	 * @param value element koji se želi dodati na vrh stoga.
	 * 
	 * @throws NullPointerException ako se pokuša dodati <code>null</code> na stog.
	 */
	public void push(Object value) {
		if (value == null)
			throw new NullPointerException("Value to be pushed must not be null!");
		
		data.add(value);
	}
	
	/**
	 * Metoda vraća element s vrha stoga i uklanja ga sa stoga.
	 * 
	 * @return element s vrha stoga.
	 * 
	 * @throws EmptyStackException ako je stog prazan u trenutku poziva metode.
	 */
	public Object pop() {
		if (data.size() == 0)
			throw new EmptyStackException();
		
		Object value = peek();
		data.remove(data.size()-1);
		
		return value;
	}
	
	/**
	 * Metoda vraća element s vrha stoga, ali ga ostavlja na stogu.
	 * 
	 * @return element s vrha stoga.
	 * 
	 * @throws EmptyStackException ako je stog prazan u trenutku poziva metode.
	 */
	public Object peek() {
		if (data.size() == 0)
			throw new EmptyStackException();
		
		return data.get(data.size()-1);
	}
	
	/**
	 * Metoda uklanja sve elemente sa stoga.
	 */
	public void clear() {
		data.clear();
	}

}
