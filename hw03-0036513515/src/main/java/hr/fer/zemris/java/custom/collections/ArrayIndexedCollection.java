package hr.fer.zemris.java.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Razred predstavlja implementaciju kolekcije koja objekte pohranjuje unutar polja.
 * Može sadržavati duplikate (više istih elemenata), ali ne i <code>null</code> vrijednosti.
 * 
 * @author mskrabic
 */
public class ArrayIndexedCollection implements List {
	
	/**
	 * Razred koji predstavlja implementaciju <code>ElementsGetter</code> sučelja za ArrayIndexedCollection.
	 * 
	 * @author mskrabic
	 */
	private static class ArrayIndexedElementsGetter implements ElementsGetter {
		/**
		 * Kolekcija nad kojom ArrayIndexedElementsGetter djeluje.
		 */
		private ArrayIndexedCollection col;
		/**
		 * Trenutna pozicija ArrayIndexedElementsGettera.
		 */
		private int currentIndex;
		/**
		 * Broj strukturnih modifikacija obavljenih nad kolekcijom u trenutku stvaranja ArrayIndexedElementsGettera.
		 */
		private long savedModificationCount;
		
		/**
		 * Konstruktor koji pohranjuje referencu na kolekciju, postavlja poziciju na početak i sprema broj obavljenih strukturnih modifikacija.
		 * 
		 * @param col kolekcija nad kojom ArrayIndexedElementsGetter djeluje.
		 */
		private ArrayIndexedElementsGetter(ArrayIndexedCollection col) {
			this.col = col;
			this.currentIndex = 0;
			this.savedModificationCount = col.modificationCount;
		}
		/**
		 * @throws ConcurrentModificationException ako se pozove kada je kolekcija nakon stvaranja ArrayIndexedElementsGettera imala strukturnih promjena.
		 */
		@Override
		public boolean hasNextElement() {
			if (savedModificationCount != col.modificationCount)
				throw new ConcurrentModificationException("The collection has been changed!");
			
			return (currentIndex < col.size());
		}

		/**
		 * @throws ConcurrentModificationException ako se pozove kada je kolekcija nakon stvaranja ArrayIndexedElementsGettera imala strukturnih promjena.
		 * @throws NoSuchElementException ako su svi elementi kolekcije već "potrošeni", tj. ArrayIndexedElementsGetter ih je sve već dohvatio.
		 */
		@Override
		public Object getNextElement() {
			if (savedModificationCount != col.modificationCount)
				throw new ConcurrentModificationException("The collection has been changed!");
			if (currentIndex == col.size())
				throw new NoSuchElementException("No more elements!");
			
			return col.get(currentIndex++);
		}
	}
	
	/**
	 * Veličina kolekcije, tj. broj elemenata trenutno pohranjenih u njoj.
	 */
	private int size;
	
	/**
	 * Polje u kojemu se interno pohranjuju elementi.
	 */
	private Object[] array;
	
	/**
	 * Pretpostavljena vrijednost kapaciteta polja.
	 */
	private static final int DEFAULT_CAPACITY = 16;
	
	/**
	 * Broj strukturnih promjena kolekcije.
	 */
	private long modificationCount;

	/**
	 * Pretpostavljeni konstruktor koji postavlja kapacitet kolekcije na 16.
	 */
	public ArrayIndexedCollection() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * Konstruktor koji postavlja kapacitet kolekcije na predanu vrijednost.
	 * 
	 * @param initialCapacity željeni kapacitet kolekcije.
	 * 
	 * @throws IllegalArgumentException ako se preda vrijednost manja od 1.
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		if (initialCapacity < 1)
			throw new IllegalArgumentException(
					"Initial capacity must be atleast 1, and it was " + initialCapacity + ".");
		
		this.size = 0;
		this.modificationCount = 0;
		this.array = new Object[initialCapacity];
	}

	/**
	 * Konstruktor koji u novu kolekciju kopira sve elemente iz predane kolekcije.
	 * Predana kolekcija ostaje nepromjenjena.
	 * 
	 * @param other kolekcija čije elemente se želi kopirati u novu.
	 */
	public ArrayIndexedCollection(Collection other) {
		this(other, DEFAULT_CAPACITY);
	}

	/**
	 * Konstruktor koji postavlja kapacitet nove kolekcije na predanu vrijednost i kopira elemente iz predane kolekcije u novu.
	 * U slučaju da je predani kapacitet premali, postavlja se na veličinu predane kolekcije.
	 * 
	 * @param other kolekcija čije elemente se želi kopirati u novu.
	 * @param initialCapacity željeni kapacitet nove kolekcije.
	 * 
	 * @throws NullPointerException ako se preda <code>null</code> umjesto kolekcije.
	 */
	public ArrayIndexedCollection(Collection other, int initialCapacity) {
		if (other == null)
			throw new NullPointerException("Collection given to the constructor must not be null!");
		
		this.modificationCount = 0;
		this.array = new Object[((other.size() > initialCapacity) ? other.size() : initialCapacity)];
		this.addAll(other);
	}

	/**
	 * Metoda vraća veličinu kolekcije, tj. broj elemenata koji su trenutno pohranjeni u njoj.
	 * 
	 * @return broj elemenata u kolekciji.
	 */
	@Override
	public int size() {
		return this.size;
	}

	/**
	 * Metoda dodaje element u kolekciju.
	 * 
	 * @throws NullPointerException ako se pokuša dodati <code>null</code> u kolekciju.
	 */
	@Override
	public void add(Object value) {
		if (value == null)
			throw new NullPointerException("Value to be added must not be null!");
		
		if (this.size == this.array.length) {
			Object[] newArray = new Object[2 * this.size];
			for (int i = 0; i < this.size; i++) {
				newArray[i] = this.array[i];
			}
			this.array = newArray;
		}
		
		this.array[this.size++] = value;
		this.modificationCount++;
	}

	/**
	 * Metoda provjerava sadrži li kolekcija traženi element.
	 */
	@Override
	public boolean contains(Object value) {
		for (int i = 0; i < this.size; i++) {
			if (this.array[i].equals(value))
				return true;
		}
		
		return false;
	}

	/**
	 * Metoda izbacuje predani element iz kolekcije.
	 */
	@Override
	public boolean remove(Object value) {
		for (int i = 0; i < this.size; i++) {
			if (this.array[i].equals(value)) {
				for (int j = i; j < this.size; j++) {
					this.array[j] = ((j+1 == this.array.length) ? null : this.array[j + 1]);
				}
				this.size--;
				this.modificationCount++;
				return true;
			}
		}
		return false;
	}

	/**
	 * Metoda izbacuje iz kolekcije element na predanoj poziciji.
	 * 
	 * @param index pozicija elementa koji se želi izbaciti.
	 * 
	 * @throws IndexOutOfBounds ako se preda neispravna pozicija.
	 */
	public void remove(int index) {
		if (index < 0 || index >= this.size)
			throw new IndexOutOfBoundsException(
					"Index should be between 0 and " + (this.size - 1) + ", and it was " + index + ".");
		
		for (int i = index; i < this.size-1; i++) {
			this.array[i] = this.array[i + 1];
		}
		this.array[this.size-1] = null;
		this.size--;
		this.modificationCount++;
	}
	
	/**
	 * Metoda vraća novo polje koje sadrži elemente kolekcije.
	 */
	@Override
	public Object[] toArray() {
		Object[] result = new Object[this.size];
		
		for (int i = 0; i < this.size; i++) {
			result[i] = this.array[i];
		}
		
		return result;
	}

	/**
	 * Metoda izbacuje sve elemente iz kolekcije.
	 */
	@Override
	public void clear() {
		for (int i = 0; i < this.size; i++) {
			this.array[i] = null;
		}
		this.size = 0;
		this.modificationCount++;
	}

	/**
	 * Metoda vraća element s tražene pozicije u kolekciji.
	 * 
	 * @param index pozicija traženog elementa.
	 * 
	 * @return element kolekcije na poziciji <code>index</code>.
	 * 
	 * @throws IndexOutOfBoundsException ako se preda neispravna pozicija.
	 */
	public Object get(int index) {
		if (index < 0 || index >= this.size)
			throw new IndexOutOfBoundsException(
					"Index should be between 0 and " + (this.size - 1) + ", and it was " + index + ".");
		
		return this.array[index];
	}

	/**
	 * Metoda vraća poziciju na kojoj se prvi put pojavljuje predani element ili -1 ako se element ne pojavljuje u kolekciji.
	 * 
	 * @param value element čiju poziciju se traži.
	 * 
	 * @return pozicija traženog elementa, odnosno -1 ako kolekcija ne sadrži element.
	 */
	public int indexOf(Object value) {
		if (value == null)
			return -1;
		
		for (int i = 0; i < this.size; i++) {
			if (this.array[i].equals(value))
				return i;
		}
		
		return -1;
	}

	/**
	 * Metoda ubacuje vrijednost na željenu poziciju u kolekciji, a elemente na većim indeksima posmiče za jedno mjesto u desno.
	 * 
	 * @param value vrijednost koju se želi dodati u kolekciju.
	 * @param position pozicija na koju se novi element želi dodati.
	 * 
	 * @throws NullPointerException ako se pokuša dodati <code>null</code> u kolekciju.
	 * @throws IndexOutOfBoundsException ako se preda neispravna pozicija.
	 */
	public void insert(Object value, int position) {
		if (value == null)
			throw new NullPointerException("Value to be added must not be null!");
		
		if (position < 0 || position > this.size)
			throw new IndexOutOfBoundsException(
					"Index should be between 0 and " + (this.size) + ", and it was " + position + ".");
		
		if (this.size == this.array.length) {
			Object[] newArray = new Object[2 * this.size];
			for (int i = 0; i < this.size; i++) {
				newArray[i] = this.array[i];
			}
			this.array = newArray;
		}
		
		for (int i = this.size; i > position; i--) {
			this.array[i] = this.array[i - 1];
		}
		this.array[position] = value;
		this.size++;
		this.modificationCount++;
	}

	@Override
	public ElementsGetter createElementsGetter() {
		return new ArrayIndexedElementsGetter(this);	
	}
}
