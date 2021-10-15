package hr.fer.zemris.java.custom.collections;

/**
 * Sučelje modelira objekte koji prime neki objekt te ispitaju je li taj objekt prihvatljiv ili nije, po nekom kriteriju.
 *  
 * @author mskrabic
 *
 */
public interface Tester {

	/**
	 * Metoda provjerava zadovoljava li predani objekt zadani uvjet.
	 * 
	 * @param obj objekt koji provjeravamo.
	 * 
	 * @return <code>true</code> ako objekt zadovoljava uvjet, <code>false</code> inače.
	 */
	boolean test(Object obj);
}
