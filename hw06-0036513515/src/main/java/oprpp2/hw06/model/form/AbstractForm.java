package oprpp2.hw06.model.form;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Apstraktni formular za prikaz domenskih objekata.
 * 
 * @author mskrabic
 *
 */
public abstract class AbstractForm {
	/**
	 * Mapa s pogreškama. Očekuje se da su ključevi nazivi svojstava a vrijednosti
	 * tekstovi pogrešaka.
	 */
	Map<String, String> errors = new HashMap<>();

	/**
	 * Dohvaća poruku pogreške za traženo svojstvo.
	 * 
	 * @param ime naziv svojstva za koje se traži poruka pogreške
	 * @return poruku pogreške ili <code>null</code> ako svojstvo nema pridruženu pogrešku
	 */
	public String getError(String name) {
		return errors.get(name);
	}
	
	/**
	 * Postavlja pogrešku za predani ključ.
	 * 
	 * @param key	ključ pogreške.
	 * @param error	tekst pogreške.
	 */
	public void setError(String key, String error) {
		errors.put(key, error);
	}	
	
	/**
	 * Provjera ima li barem jedno od svojstava pridruženu pogrešku.
	 * 
	 * @return <code>true</code> ako ima, <code>false</code> inače.
	 */
	public boolean hasErrors() {
		return !errors.isEmpty();
	}
	
	/**
	 * Provjerava ima li traženo svojstvo pridruženu pogrešku. 
	 * 
	 * @param ime naziv svojstva za koje se ispituje postojanje pogreške
	 * @return <code>true</code> ako ima, <code>false</code> inače.
	 */
	public boolean hasError(String name) {
		return errors.containsKey(name);
	}
	
	/**
	 * Na temelju parametara primljenih kroz {@link HttpServletRequest} popunjava
	 * svojstva ovog formulara.
	 * 
	 * @param req objekt s parametrima
	 */
	public abstract void fillFromRequest(HttpServletRequest req);
	
	/**
	 * Metoda obavlja validaciju formulara. Formular je prethodno na neki način potrebno
	 * napuniti. Metoda provjerava semantičku korektnost svih podataka te po potrebi
	 * registrira pogreške u mapu pogrešaka.
	 */
	public abstract void validate();

}
