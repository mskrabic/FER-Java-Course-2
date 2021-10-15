package oprpp2.hw04.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oprpp2.hw04.servlets.GlasanjeServlet.Band;
import oprpp2.hw04.util.Utils;

/**
 * Servlet za prikaz rezultata glasanja.
 * 
 * @author mskrabic
 *
 */
public class GlasanjeRezultatiServlet extends HttpServlet {
	
	/**
	 * Serijski broj.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
		List<Band> bands = Utils.readBands(fileName);
		
		fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		List<Result> results = Utils.getResults(fileName, bands);
		results.sort((r1, r2) -> r2.getVotes().compareTo(r1.getVotes()));
		
		List<Result> winners = new ArrayList<>();
		winners.addAll(results.stream().filter(r -> r.getVotes().equals(results.get(0).getVotes())).collect(Collectors.toList()));

		req.setAttribute("results", results);
		req.setAttribute("winners", winners);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}

	/**
	 * Razred modelira jedan rezultat - podatke o bendu i broj glasova.
	 * 
	 * @author mskrabic
	 */
	public static class Result {
		
		/**
		 * Bend.
		 */
		private Band band;
		
		/**
		 * Broj glasova.
		 */
		private String votes;
		
		
		/**
		 * Konstruktor.
		 * 
		 * @param band	bend.
		 * @param votes	broj glasova.
		 */
		public Result(Band band, String votes) {
			this.band = band;
			this.votes = votes;
		}
		
		/**
		 * Getter za bend.
		 * 
		 * @return bend.
		 */
		public Band getBand() {
			return band;
		}

		/**
		 * Getter za broj glasova.
		 * 
		 * @return	broj glasova.
		 */
		public String getVotes() {
			return votes;
		}
		
		
	}
}
