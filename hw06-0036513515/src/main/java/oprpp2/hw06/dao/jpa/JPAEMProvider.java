package oprpp2.hw06.dao.jpa;

import javax.persistence.EntityManager;

import oprpp2.hw06.dao.DAOException;

/**
 * Objekt za dohvat {@link EntityManager}-a.
 * 
 * @author mskrabic
 *
 */
public class JPAEMProvider {

	/**
	 * Mapa EntityManager-a.
	 */
	private static ThreadLocal<EntityManager> locals = new ThreadLocal<>();

	/**
	 * Getter za EntityManager.
	 * 
	 * @return EntityManager.
	 */
	public static EntityManager getEntityManager() {
		EntityManager em = locals.get();
		if (em == null) {
			em = JPAEMFProvider.getEmf().createEntityManager();
			em.getTransaction().begin();
			locals.set(em);
		}
		return em;
	}

	/**
	 * Zatvaranje EntityManagera.
	 * 
	 * @throws DAOException u slučaju pogreške.
	 */
	public static void close() throws DAOException {
		EntityManager em = locals.get();
		if (em == null) {
			return;
		}
		DAOException dex = null;
		try {
			em.getTransaction().commit();
		} catch (Exception ex) {
			dex = new DAOException("Unable to commit transaction.", ex);
		}
		try {
			em.close();
		} catch (Exception ex) {
			if (dex != null) {
				dex = new DAOException("Unable to close entity manager.", ex);
			}
		}
		locals.remove();
		if (dex != null)
			throw dex;
	}

}