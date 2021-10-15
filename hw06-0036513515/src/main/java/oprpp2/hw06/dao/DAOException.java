package oprpp2.hw06.dao;

/**
 * Enkapsulira pogreške unutar DAO sloja.
 * 
 * @author mskrabic
 *
 */
public class DAOException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public DAOException(String message) {
		super(message);
	}
}