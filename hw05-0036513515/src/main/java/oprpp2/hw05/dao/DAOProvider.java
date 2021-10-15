package oprpp2.hw05.dao;

import oprpp2.hw05.dao.sql.SQLDAO;

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
	private static DAO dao = new SQLDAO();
	
	/**
	 * Dohvat primjerka.
	 * 
	 * @return objekt koji enkapsulira pristup sloju za perzistenciju podataka.
	 */
	public static DAO getDao() {
		return dao;
	}
	
}