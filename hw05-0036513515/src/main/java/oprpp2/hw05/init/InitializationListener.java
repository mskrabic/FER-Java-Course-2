package oprpp2.hw05.init;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import oprpp2.hw05.dao.DAOException;
import oprpp2.hw05.model.PollOption;

@WebListener
public class InitializationListener implements ServletContextListener {

	private static final String POLL1 = "/WEB-INF/glasanje-definicija1.txt";
	private static final String RESULTS1 = "/WEB-INF/glasanje-rezultati1.txt";
	private static final String POLL2 = "/WEB-INF/glasanje-definicija2.txt";
	private static final String RESULTS2 = "/WEB-INF/glasanje-rezultati2.txt";
	private static final String TITLE1 = "Glasanje za omiljeni bend";
	private static final String MESSAGE1 = "Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste glasali!";
	private static final String TITLE2 = "Glasanje za omiljenog šahista";
	private static final String MESSAGE2 = "Od sljedećih šahista, koji Vam je šahist najdraži? Kliknite na link kako biste glasali!";

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		Properties p = new Properties();
		String path = sce.getServletContext().getRealPath("/WEB-INF/dbsettings.properties");
		try {
			InputStreamReader isr = new InputStreamReader(Files.newInputStream(Path.of(path)));
			p.load(isr);
		} catch (IOException e) {
			throw new RuntimeException("Error while loading database perties.");
		}

		if (!(p.containsKey("host") && p.containsKey("port") && p.containsKey("name") && p.containsKey("user")
				&& p.containsKey("password"))) {
			throw new RuntimeException("Error while loading database properties!");
		}

		String dbName = p.getProperty("name");
		String host = p.getProperty("host");
		String port = p.getProperty("port");
		String user = p.getProperty("user");
		String password = p.getProperty("password");
		String connectionURL = "jdbc:derby://" + host + ":" + port + "/" + dbName + ";user=" + user + ";password=" + password;

		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
		} catch (PropertyVetoException e) {
			throw new DAOException("Error while initializing the connection pool.", e);
		}
		cpds.setJdbcUrl(connectionURL);

		createTables(sce, cpds);

		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);
	}

	/**
	 * Stvara tablice <code>Polls</code> i <code>PollOptions</code> ako ne postoje u bazi.
	 * 
	 * @param sce {@link ServletContextEvent} stvaranja konteksta.
	 * @param cpds	bazen veza.
	 */
	private void createTables(ServletContextEvent sce, ComboPooledDataSource cpds) {
		try (Connection con = cpds.getConnection()) {
			DatabaseMetaData dmd = con.getMetaData();
			List<String> tables = new ArrayList<>();
			try (ResultSet rs = dmd.getTables(null, null, "%", null)) {
				while (rs != null && rs.next())
					tables.add(rs.getString(3));
			}
			if (!tables.contains("POLLS")) {
				String sql = "CREATE TABLE Polls\n" + 
							 "(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\n" + 
							  "title VARCHAR(150) NOT NULL,\n" + 
							 "message CLOB(2048) NOT NULL\n" + 
							 ")";
				PreparedStatement pst = con.prepareStatement(sql);
				pst.executeUpdate();
			}
			if (!tables.contains("POLLOPTIONS")) {
				String sql = "CREATE TABLE PollOptions\n" + 
							"(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\n" + 
							"optionTitle VARCHAR(100) NOT NULL,\n" + 
							"optionLink VARCHAR(150) NOT NULL,\n" +
							"pollID BIGINT,\n" +
							"votesCount BIGINT,\n" + 
							"FOREIGN KEY (pollID) REFERENCES Polls(id)\n" +
							")";
				PreparedStatement pst = con.prepareStatement(sql);
				pst.executeUpdate();
			}
			if (empty(con)) {
				insertData(sce, con);
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error while initializing the database.");
		}
	}

	/**
	 * Provjerava postoje li već neke ankete u bazi podataka.
	 * 
	 * @param con	konekcija prema bazi.
	 * 
	 * @return	<code>true</code> ako nema anketa u bazi, <code>false</code> inače.
	 * 
	 * @throws SQLException	u slučaju pogreške pri pristupu bazi.
	 */
	private boolean empty(Connection con) throws SQLException {
        PreparedStatement pst = con.prepareStatement("SELECT * FROM POLLS");

        ResultSet rset = pst.executeQuery();

        return !(rset != null && rset.next());
	}

	/**
	 * Dodaje dvije default ankete u bazu podataka.
	 * 
	 * @param sce	{@link ServletContextEvent} stvaranja konteksta.
	 * @param con	veza prema bazi podataka.
	 */
	private void insertData(ServletContextEvent sce, Connection con) {
		List<PollOption> results1 = getResults(sce, POLL1, RESULTS1);
		List<PollOption> results2 = getResults(sce, POLL2, RESULTS2);

		createPoll(con, TITLE1, MESSAGE1, results1);
		createPoll(con, TITLE2, MESSAGE2, results2);
	}

	/**
	 * Dohvaća podatke za default ankete.
	 * @param sce		{@link ServletContextEvent} stvaranja konteksta.
	 * @param pollFile	tekstualna datoteka s definicijom ankete.
	 * @param resultFile	tekstualna datoteka s rezultatima.
	 * 
	 * @return	lista rezultata.
	 */
	private List<PollOption> getResults(ServletContextEvent sce, String pollFile, String resultFile) {
		List<PollOption> results = new ArrayList<>();
		Map<Long, Integer> votes = new HashMap<>();
		try {
			List<String> lines = Files.readAllLines(Path.of(sce.getServletContext().getRealPath(resultFile)));
			for (String line : lines) {
				String[] parts = line.split("\t");
				votes.put(Long.parseLong(parts[0]), Integer.parseInt(parts[1]));
			}
		} catch (IOException e) {
			throw new DAOException(e);
		}

		try {
			List<String> lines = Files.readAllLines(Path.of(sce.getServletContext().getRealPath(pollFile)));
			for (String line : lines) {
				String[] parts = line.split("\t");
				PollOption r = new PollOption(Integer.parseInt(parts[0]), parts[1], parts[2], 1, 0);
				if (votes.containsKey(r.getId()))
					r.setVotesCount(votes.get(r.getId()));
				results.add(r);
			}
		} catch (IOException e) {
			throw new DAOException(e);
		}
		return results;
	}

	/**
	 * Stvara anketu u bazi podataka.
	 * 
	 * @param con		veza prema bazi podataka.
	 * @param title		naslov ankete.
	 * @param message	poruka ankete.
	 * @param results	rezultati ankete.
	 */
	private void createPoll(Connection con, String title, String message, List<PollOption> results) {
		try {
			PreparedStatement pst = con.prepareStatement("INSERT INTO Polls(title, message) VALUES (?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, title);
			pst.setString(2, message);
			pst.executeUpdate();
			ResultSet keys = pst.getGeneratedKeys();
			keys.next();
			long pollID = keys.getLong(1);
			pst = con.prepareStatement(
					"INSERT INTO PollOptions (optionTitle, optionLink, pollID, votesCount) VALUES (?, ?, ?, ?)");
			for (PollOption r : results) {
				pst.setString(1, r.getTitle());
				pst.setString(2, r.getLink());
				pst.setLong(3, pollID);
				pst.setInt(4, r.getVotesCount());
				pst.addBatch();
			}
			pst.executeBatch();
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource) sce.getServletContext()
				.getAttribute("hr.fer.zemris.dbpool");
		if (cpds != null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
