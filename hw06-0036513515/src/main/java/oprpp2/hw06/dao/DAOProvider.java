package oprpp2.hw06.dao;

import oprpp2.hw06.dao.jpa.JPADAOImpl;

/**
 * Jedinstveni objekt za dohvat DAO implementacije.
 * 
 * @author mskrabic
 *
 */
public class DAOProvider {

	/**
	 * Implementacija DAO.
	 */
	private static DAO dao = new JPADAOImpl();
	
	/**
	 * Dohvat primjerka.
	 * 
	 * @return objekt koji enkapsulira pristup sloju za perzistenciju podataka.
	 */
	public static DAO getDAO() {
		return dao;
	}
	
}