package oprpp2.hw05.model;

/**
 * Razred modelira jednu opciju u anketi.
 * 
 * @author mskrabic
 *
 */
public class PollOption {

	/**
	 * ID opcije.
	 */
	private long id;

	/**
	 * Naziv opcije.
	 */
	private String title;

	/**
	 * Link opcije.
	 */
	private String link;

	/**
	 * ID ankete kojoj pripada.
	 */
	private long pollID;

	/**
	 * Broj glasova.
	 */
	private int votesCount;

	/**
	 * Konstruktor.
	 */
	public PollOption(long id, String title, String link, long pollID, int votesCount) {
		this.id = id;
		this.title = title;
		this.link = link;
		this.pollID = pollID;
		this.votesCount = votesCount;
	}

	/**
	 * Getter za ID.
	 * 
	 * @return	ID opcije.
	 */
	public long getId() {
		return id;
	}

	/**
	 * Getter za naslov.
	 * 
	 * @return	naslov opcije.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Getter za link.
	 * 
	 * @return link opcije.
	 */
	public String getLink() {
		return link;
	}

	/**
	 * Getter za ID ankete.
	 * 
	 * @return	ID ankete.
	 */
	public long getPollID() {
		return pollID;
	}

	/**
	 * Getter za broj glasova.
	 * 
	 * @return broj glasova.
	 */
	public int getVotesCount() {
		return votesCount;
	}

	/**
	 * Setter za broj glasova.
	 * @param votesCount	novi broj glasova.
	 */
	public void setVotesCount(int votesCount) {
		this.votesCount = votesCount;
	}
}
