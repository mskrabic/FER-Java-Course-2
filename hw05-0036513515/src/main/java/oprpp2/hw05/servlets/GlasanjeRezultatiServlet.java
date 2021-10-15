package oprpp2.hw05.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oprpp2.hw05.dao.DAOProvider;
import oprpp2.hw05.model.PollOption;

/**
 * Servlet za prikaz rezultata glasanja.
 * 
 * @author mskrabic
 *
 */
@WebServlet("/servleti/glasanje-rezultati")
public class GlasanjeRezultatiServlet extends HttpServlet {
	
	/**
	 * Serijski broj.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		long pollID = Long.parseLong(req.getParameter("pollID"));
		
		List<PollOption> results = DAOProvider.getDao().getPollOptions(pollID);
		results.sort((a, b) -> a.getVotesCount() < b.getVotesCount() ? 1 : -1);
		List<PollOption> winners = new ArrayList<>();
		winners.addAll(results.stream()
				.filter(r -> r.getVotesCount() == results.get(0).getVotesCount())
				.collect(Collectors.toList()));

		req.setAttribute("pollID", pollID);
		req.setAttribute("results", results);
		req.setAttribute("winners", winners);
		req.getRequestDispatcher("/WEB-INF/pages/glasanje-rezultati.jsp").forward(req, resp);
	}
}
