package hr.fer.zemris.java.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Razred predstavlja implementaciju kolekcije koja objekte pohranjuje u dvostruko povezanoj listi.
 * Može sadržavati duplikate (više istih elemenata), ali ne i <code>null</code> vrijednosti.
 * 
 * @author mskrabic
 */
public class LinkedListIndexedCollection implements List {
	
	/**
	 * Razred predstavlja čvor u dvostruko povezanoj listi.
	 * 
	 * @author mskrabic
	 */
	private static class ListNode {
		
		/**
		 * Prethodni element u listi.
		 */
		private ListNode previous;
		
		/**
		 * Sljedeći element u listi.
		 */
		private ListNode next;
		
		/**
		 * Vrijednost elementa.
		 */
		private Object value;
	}
	
	/**
	 * Razred koji predstavlja implementaciju <code>ElementsGetter</code> sučelja za LinkedListIndexedCollection.
	 * 
	 * @author mskrabic
	 */
	private static class LinkedListIndexedElementsGetter implements ElementsGetter {
		/**
		 * Kolekcija nad kojom LinkedListIndexedElementsGetter djeluje.
		 */
		private LinkedListIndexedCollection col;
		/**
		 * Trenutni čvor na kojem se LinkedListIndexedElementsGetter nalazi.
		 */
		private ListNode currentNode;
		/**
		 * Broj strukturnih modifikacija obavljenih nad kolekcijom u trenutku stvaranja LinkedListIndexedElementsGettera.
		 */
		private long savedModificationCount;
		
		
		/**
		 * Konstruktor koji pohranjuje referencu na kolekciju, postavlja poziciju na početni čvor i sprema broj do tad obavljenih strukturnih modifikacija nad kolekcijom.
		 * 
		 * @param col kolekcija nad kojom LinkedListIndexedElementsGetter djeluje.
		 */
		private LinkedListIndexedElementsGetter(LinkedListIndexedCollection col) {
			this.col = col;
			this.currentNode = col.first;
			this.savedModificationCount = col.modificationCount;
		}

		/**
		 * @throws ConcurrentModificationException ako se pozove kada je kolekcija nakon stvaranja LinkedListIndexedElementsGettera imala strukturnih promjena.
		 */
		@Override
		public boolean hasNextElement() {
			if (savedModificationCount != col.modificationCount)
				throw new ConcurrentModificationException("The collection has been changed!");
			
			return (currentNode != null);
		}

		/**
		 * @throws ConcurrentModificationException ako se pozove kada je kolekcija nakon stvaranja LinkedListIndexedElementsGettera imala strukturnih promjena.
		 * @throws NoSuchElementException ako su svi elementi kolekcije već "potrošeni", tj. LinkedListIndexedElementsGetter ih je sve već dohvatio.
		 */
		@Override
		public Object getNextElement() {
			if (savedModificationCount != col.modificationCount)
				throw new ConcurrentModificationException("The collection has been changed!");	
			if (currentNode == null)
				throw new NoSuchElementException("No more elements!");
			
			Object element = currentNode.value;
			currentNode = currentNode.next;
			return element;
		}
	}
	
	/**
	 * Veličina kolekcije, tj. broj elemenata trenutno pohranjenih u njoj.
	 */
	private int size;
	
	/**
	 * Prvi element u kolekciji.
	 */
	private ListNode first;
	
	/**
	 * Posljednji element u kolekciji.
	 */
	private ListNode last;
	
	/**
	 * Broj strukturnih modifikacija provedenih nad kolekcijom.
	 */
	private long modificationCount;
	
	/**
	 * Pretpostavljeni konstruktor koji inicijalizira praznu listu.
	 */
	public LinkedListIndexedCollection() {
		this.first = this.last = null;
		this.size = 0;
		this.modificationCount = 0;
	}
	
	/**
	 * Konstruktor koji u novu kolekciju kopira elemente iz predane kolekcije.
	 * Predana kolekcija pri tome ostaje nepromjenjena.
	 * 
	 * @param other kolekcija čije elemente se želi kopirati u novu.
	 * 
	 * @throws NullPointerException ako se preda <code>null</code> umjesto kolekcije.
	 */
	public LinkedListIndexedCollection(Collection other) {
		if (other == null)
			throw new NullPointerException("Collection passed to the constructor must not be null!");
		
		this.modificationCount = 0;
		this.size = 0;
		Object[] elements = other.toArray();
		for (int i = 0; i < other.size(); i++) {
			this.add(elements[i]);
		}
	}

	/**
	 * Metoda vraća veličinu kolekcije, tj. broj elemenata koji su trenutno pohranjeni u njoj.
	 */
	@Override
	public int size() {
		return this.size;
	}

	/**
	 * Metoda dodaje element u kolekciju, na kraj liste.
	 * 
	 * @param value element koji se želi dodati.
	 * 
	 * @throws NullPointerException ako se pokuša dodati <code>null</code>.
	 */
	@Override
	public void add(Object value) {
		if (value == null)
			throw new NullPointerException("Value to be added must not be null!");
		
		this.modificationCount++;
		ListNode newNode = new ListNode();
		newNode.previous = this.last;
		newNode.next = null;
		newNode.value = value;
		if (size == 0) {
			this.first = this.last = newNode;
		} else {
			this.last.next = newNode;
			this.last = newNode;
		}
		this.size++;
	}

	/**
	 * Metoda provjerava sadrži li kolekcija traženi element.
	 */
	@Override
	public boolean contains(Object value) {
		if (value == null)
			return false;
		
		for (ListNode node = this.first; node != null; node = node.next) {
			if (node.value.equals(value))
				return true;
		}
		
		return false;
	}

	/**
	 * Metoda izbacuje predani element (njegovo prvo pojavljivanje u listi) iz kolekcije.
	 * 
	 * @return <code>true</code> ako kolekcija sadrži element i metoda ga uspješno izbaci, 
	 * <code>false</code> inače.
	 */
	@Override
	public boolean remove(Object value) {
		if (value == null)
			return false;
		
		for (ListNode node = this.first; node != null; node = node.next) {
			if (node.value.equals(value)) {
				if (node == this.first) 
					this.first = node.next;
				if (node == this.last)
					this.last = node.previous;
				
				if (node.next != null)
					node.next.previous = node.previous;
				if (node.previous != null)
					node.previous.next = node.next;
				this.size--;
				
				this.modificationCount++;
				return true;
			}
		}
		return false;
	}

	/**
	 * Metoda vraća novo polje koje sadrži elemente kolekcije.
	 */
	@Override
	public Object[] toArray() {
		Object[] array = new Object[this.size];
		int idx = 0;
		for (ListNode node = this.first; node != null; node = node.next) {
			array[idx++] = node.value;
		}
		return array;
	}

	/**
	 * Metoda izbacuje sve elemente iz kolekcije.
	 */
	@Override
	public void clear() {
		this.first = this.last = null;
		this.size = 0;
		this.modificationCount++;
	}
	
	/**
	 * Metoda dohvaća element s tražene pozicije iz kolekcije.
	 * 
	 * @param index pozicija traženog elementa.
	 * 
	 * @return element s pozicije <code>index</code> u kolekciji.
	 * 
	 * @throws IndexOutOfBoundsException ako se preda neispravna pozicija.
	 */
	public Object get(int index) {
		if (this.isEmpty())
			throw new RuntimeException("The collection is empty!");
		if (index < 0 || index >= this.size)
			throw new IndexOutOfBoundsException("Index should be between 0 and " + (this.size-1) + ",and was " + index + ".");
		
		if (index < (this.size-1-index)) {
			ListNode node = this.first;
			for (int i = 0; i < index; i++) {
				node = node.next;
			}
			return node.value;
		}
		ListNode node = this.last;
		for (int i = this.size-1; i > index; i--) {
			node = node.previous;
		}
		return node.value;
	}
	
	/**
	 * Metoda ubacuje element na željenu poziciju unutar kolekcije. Elemente na većim indeksima posmiče
	 * za jedno mjesto u desno.
	 * 
	 * @param value element koji želimo dodati u kolekciju.
	 * @param position pozicija na koju želimo dodati element.
	 * 
	 * @throws IndexOutOfBoundsException ako se preda neispravna pozicija.
	 */
	public void insert(Object value, int position) {
		if (position < 0 || position > this.size)
			throw new IndexOutOfBoundsException("Position should be between 0 and " + (this.size) + ",and was " + position + ".");
		if (position == this.size) {
			add(value);
			return;
		}
		ListNode node = this.first;
		for (int i = 0; i < position; i++, node = node.next)
			;
		ListNode newNode = new ListNode();
		newNode.previous = node.previous;
		newNode.next = node;
		if (node.previous != null)
			node.previous.next = newNode;
		if (this.first == node) {
			this.first = newNode;
		}
		node.previous = newNode;
		newNode.value = value;	
		this.size++;
		this.modificationCount++;
	}
	
	/**
	 * Metoda vraća poziciju na kojoj se prvi put pojavljuje traženi element, odnosno -1 ako se element
	 * ne nalazi u kolekciji.
	 * 
	 * @param value element čija se pozicija traži.
	 * 
	 * @return pozicija traženog elementa ili -1 ako se element ne nalazi u kolekciji.
	 */
	public int indexOf(Object value) {
		if (value == null)
			return -1;
		
		int index = 0;
		for (ListNode node = this.first; node != null; node = node.next, index++) {
			if (node.value.equals(value)) {
				return index;
			}
		}
		
		return -1;
	}
	
	/**
	 * Metoda izbacuje element s predane pozicije iz kolekcije.
	 * 
	 * @param index pozicija s koje se želi izbaciti element.
	 * 
	 * @throws IndexOutOfBoundsException ako se preda neispravna pozicija.
	 */
	public void remove(int index) {
		if (index < 0 || index >= this.size) 
			throw new IndexOutOfBoundsException("Index should be between 0 and " + (this.size-1) + ",and was " + index + ".");
		
		ListNode node = this.first;
		for (int i = 0; i < index; i++, node = node.next)
			;
		if (node == this.first)
			this.first = node.next;
		if (node == this.last)
			this.last = node.previous;
		if (node.previous != null)
			node.previous.next = node.next; 
		if (node.next != null)
			node.next.previous = node.previous;
		this.size--;
		this.modificationCount++;
	}

	@Override
	public ElementsGetter createElementsGetter() {
		return new LinkedListIndexedElementsGetter(this);
	}
	
}
