package oprpp2.hw05.dao.sql;

import java.sql.Connection;

/**
 * Razred se koristi za pohranu veza prema bazi podataka u {@link ThreadLocal} objekt.
 * 
 * @author mskrabic
 *
 */
public class SQLConnectionProvider {

	/**
	 * Mapa veza prema bazi podataka.
	 */
	private static ThreadLocal<Connection> connections = new ThreadLocal<>();
	
	/**
	 * Postavi vezu za trenutnu dretvu (ili obriši zapis iz mape ako je argument <code>null</code>).
	 * 
	 * @param con veza prema bazi podataka.
	 */
	public static void setConnection(Connection con) {
		if(con==null) {
			connections.remove();
		} else {
			connections.set(con);
		}
	}
	
	/**
	 * Dohvati vezu koju trenutna dretva (pozivatelj) smije koristiti.
	 * 
	 * @return veza prema bazi podataka.
	 */
	public static Connection getConnection() {
		return connections.get();
	}
	
}