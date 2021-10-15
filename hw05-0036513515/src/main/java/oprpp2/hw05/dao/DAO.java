package oprpp2.hw05.dao;

import java.util.List;

import oprpp2.hw05.model.Poll;
import oprpp2.hw05.model.PollOption;

/**
 * Sučelje prema podsustavu za perzistenciju podataka.
 * 
 * @author mskrabic
 *
 */
public interface DAO {

	/**
	 * Dohvaća sve dostupne ankete.
	 * 
	 * @return	lista anketa.
	 */
	List<Poll> getPolls();

	/**
	 * Registrira glas za traženu opciju u anketi.
	 * 
	 * @param pollID 	ID ankete.
	 * @param id		ID opcije u anketi.
	 */
	void incrementVotes(long pollID, long id);

	/**
	 * Dohvaća rezultate ankete.
	 * 
	 * @param pollID 	ID ankete.
	 * 
	 * @return	rezultati ankete.
	 */
	List<PollOption> getPollOptions(long pollID);

	/**
	 * Dohvaća anketu s predanim ID-em.
	 * 
	 * @param pollID 	ID tražene ankete.
	 * 
	 * @return	anketa.
	 */
	Poll getPoll(long pollID);
}