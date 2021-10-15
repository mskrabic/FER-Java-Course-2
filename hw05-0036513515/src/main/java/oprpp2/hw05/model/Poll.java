package oprpp2.hw05.model;

/**
 * Razred modelira jednu anketu.
 * 
 * @author mskrabic
 *
 */
public class Poll {
	
	/**
	 * ID ankete.
	 */
	private long id;
	
	/**
	 * Naslov ankete.
	 */
	private String title;
	
	/**
	 * Poruka ankete.
	 */
	private String message;
	
	/**
	 * Konstruktor.
	 */
	public Poll(long id, String title, String message) {
		this.id = id;
		this.title = title;
		this.message = message;
	}

	/**
	 * Getter za ID.
	 * 
	 * @return	ID ankete.
	 */
	public long getId() {
		return id;
	}

	/**
	 * Getter za naslov.
	 * 
	 * @return	naslov ankete.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Getter za poruku.
	 * 
	 * @return	poruka ankete.
	 */
	public String getMessage() {
		return message;
	}

}
