package oprpp2.hw05.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oprpp2.hw05.dao.DAOProvider;
import oprpp2.hw05.model.Poll;
import oprpp2.hw05.model.PollOption;

/**
 * Servlet za prikaz početne stranice ankete.
 * 
 * @author mskrabic
 *
 */
@WebServlet("/servleti/glasanje")
public class GlasanjeServlet extends HttpServlet {
	
	/**
	 * Serijski broj.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pollID = req.getParameter("pollID");
		
		Poll p = DAOProvider.getDao().getPoll(Long.parseLong(pollID));
		List<PollOption> options = DAOProvider.getDao().getPollOptions(Long.parseLong(pollID));
		
		req.setAttribute("poll", p);
		req.setAttribute("options", options);
		
		req.getRequestDispatcher("/WEB-INF/pages/glasanje-index.jsp").forward(req, resp);
	}
	
}