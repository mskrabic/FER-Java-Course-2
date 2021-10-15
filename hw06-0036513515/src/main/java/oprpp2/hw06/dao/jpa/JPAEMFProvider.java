package oprpp2.hw06.dao.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * Objekt za dohvat {@link EntityManagerFactory}-a.
 * 
 * @author mskrabic
 *
 */
public class JPAEMFProvider {

	/**
	 * Tvornica {@EntityManager}-a.
	 */
	public static EntityManagerFactory emf;

	/**
	 * Getter za tvornicu.
	 * 
	 * @return tvornica.
	 */
	public static EntityManagerFactory getEmf() {
		return emf;
	}

	/**
	 * Setter za tvornicu.
	 * 
	 * @param emf tvornica koja se želi postaviti.
	 */
	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}
}