package hr.fer.zemris.java.webserver;

/**
 * Sučelje modelira jednog radnika za obradu zahtjeva.
 * 
 * @author mskrabic
 *
 */
public interface IWebWorker {
	/**
	 * Obrađuje zahtjev definiran predanim kontekstom.
	 * 
	 * @param context		kontekst zahtjeva.
	 * @throws Exception	u slučaju pogreške pri obradi zahtjeva.
	 */
	public void processRequest(RequestContext context) throws Exception;
}