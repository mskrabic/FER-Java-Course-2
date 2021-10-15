package hr.fer.zemris.java.webserver;

/**
 * Sučelje modelira jednog dispečera poslova.
 * 
 * @author mskrabic
 *
 */
public interface IDispatcher {
	
	/**
	 * Šalje zahtjev za predani put.
	 * 
	 * @param urlPath		put zahtjeva.
	 * @throws Exception	u slučaju pogreške pri slanju zahtjeva.
	 */
	void dispatchRequest(String urlPath) throws Exception;
}