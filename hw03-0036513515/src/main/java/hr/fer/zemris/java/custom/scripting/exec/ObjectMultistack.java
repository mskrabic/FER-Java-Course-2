package hr.fer.zemris.java.custom.scripting.exec;

import java.util.HashMap;
import java.util.Map;

/**
 * Razred predstavlja model "višestrukog stoga", gdje je svakom ključu pridružen odgovarajući stog.
 * 
 * @author mskrabic
 *
 */
public class ObjectMultistack {
	/**
	 * Mapa koja se interno koristi za pohranu elemenata.
	 */
	private Map<String, MultistackEntry> map;
	
	/**
	 * Konstruktor.
	 */
	public ObjectMultistack() {
		map = new HashMap<>();
	}
	
	/**
	 * Stavlja novu vrijednost na vrh stoga pridruženog predanom ključu.
	 * 
	 * @param keyName		ključ stoga.
	 * @param valueWrapper	vrijednost koja se stavlja na stog.
	 */
	public void push(String keyName, ValueWrapper valueWrapper) {
		MultistackEntry newEntry = new MultistackEntry(valueWrapper);
		newEntry.setNext(map.get(keyName));
		map.put(keyName, newEntry);
	}

	/**
	 * Skida vrijednost s vrha stoga pridruženog predanom ključu.
	 * 
	 * @param keyName			ključ stoga.
	 * 
	 * @return					vrijednost s vrha traženog stoga.
	 * 
	 * @throws RuntimeException ako je traženi stog prazan.
	 */
	public ValueWrapper pop(String keyName) {
		if (isEmpty(keyName))
			throw new RuntimeException("Multistack empty: No entry for the given key!");
		
		MultistackEntry head = map.get(keyName);
		map.put(keyName, head.getNext());
		
		return head.getValue();
	}
	
	/**
	 * Dohvaća vrijednost s vrha stoga pridruženog predanom ključu, ali ju ostavlja na vrhu stoga.
	 * 
	 * @param keyName			ključ stoga.
	 * 
	 * @return					vrijednost s vrha traženog stoga.
	 * 
	 * @throws RuntimeException	ako je traženi stog prazan.
	 */
	public ValueWrapper peek(String keyName) {
		if (isEmpty(keyName))
			throw new RuntimeException("Multistack empty: No entry for the given key!");
		
		return map.get(keyName).getValue();
	}
	
	/**
	 * Provjerava je li stog pridružen predanom ključu prazan.
	 * 
	 * @param keyName	ključ stoga.
	 * 
	 * @return			<code>true</code> ako je prazan, <code>false</code> inače.
	 */
	public boolean isEmpty(String keyName) {
		return (map.get(keyName) == null);
	}
	
	/**
	 * Razred modelira jednu stavku na višestrukom stogu.
	 * 
	 * @author mskrabic
	 *
	 */
	private static class MultistackEntry {
		/**
		 * Vrijednost stavke.
		 */
		private ValueWrapper value;
		
		/**
		 * Sljedeća stavka stoga.
		 */
		private MultistackEntry next;
		
		/**
		 * Konstruktor.
		 * 
		 * @param value	vrijednost stavke.
		 */
		public MultistackEntry(ValueWrapper value) {
			this.value = value;
			this.next = null;
		}
		
		/**
		 * Vraća vrijednost stavke.
		 * 
		 * @return	vrijednost stavke.
		 */
		public ValueWrapper getValue() {
			return value;
		}
		
		/**
		 * Vraća sljedeću stavku.
		 * 
		 * @return	sljedeća stavka.
		 */
		public MultistackEntry getNext() {
			return next;
		}
		
		/**
		 * Postavlja sljedeću stavku.
		 * 
		 * @param next	sljedeća stavka koja se želi postaviti.
		 */
		public void setNext(MultistackEntry next) {
			this.next = next;
		}
	}
}