package oprpp2.hw05.dao.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import oprpp2.hw05.dao.DAO;
import oprpp2.hw05.dao.DAOException;
import oprpp2.hw05.model.Poll;
import oprpp2.hw05.model.PollOption;

/**
 * Implementacija DAO podsustava za SQL.
 * 
 * @author mskrabic
 */
public class SQLDAO implements DAO {

	@Override
	public List<Poll> getPolls() {
		List<Poll> polls = new ArrayList<>();
		
		Connection con = SQLConnectionProvider.getConnection();
		try (PreparedStatement pst = con.prepareStatement("select * from Polls order by id")) {
			try (ResultSet rs = pst.executeQuery()) {
					while (rs != null && rs.next()) {
						Poll poll = new Poll(rs.getLong(1), rs.getString(2), rs.getString(3));
						polls.add(poll);
					}
			}
		} catch (Exception ex) {
			throw new DAOException("Error while fetching the available polls.", ex);
		}
		return polls;
	}

	@Override
	public void incrementVotes(long pollID, long id) {
		Connection con = SQLConnectionProvider.getConnection();
		
		try (PreparedStatement pst = con.prepareStatement("UPDATE PollOptions SET votesCount = votesCount +1 WHERE pollID = ? AND id = ?")){
			pst.setLong(1, pollID);
			pst.setLong(2, id);
			pst.executeUpdate();
		} catch (Exception e) {
			throw new DAOException("Error while fetching the available polls.", e);
		}
	}

	@Override
	public List<PollOption> getPollOptions(long pollID) {
		List<PollOption> results = new ArrayList<>();
		
		Connection con = SQLConnectionProvider.getConnection();
		try (PreparedStatement pst = con.prepareStatement("SELECT * FROM PollOptions WHERE pollID = ?")) {
			pst.setLong(1, pollID);
			try (ResultSet rs = pst.executeQuery()) {
				while (rs != null & rs.next()) {
					results.add(new PollOption(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getLong(4), rs.getInt(5)));
				}
			}
		} catch (Exception e) {
			throw new DAOException("Error while fetching the available options.", e);
		}
		return results;
	}

	@Override
	public Poll getPoll(long pollID) {
		Connection con = SQLConnectionProvider.getConnection();
		
		try (PreparedStatement pst = con.prepareStatement("SELECT * FROM Polls WHERE id = ?") ){
			pst.setLong(1, pollID);
			try (ResultSet rs = pst.executeQuery()) {
				rs.next();
				return new Poll(pollID, rs.getString(2), rs.getString(3));
			}
		} catch (Exception e) {
			throw new DAOException("Error while fetching the poll.", e);
		}
	}

}