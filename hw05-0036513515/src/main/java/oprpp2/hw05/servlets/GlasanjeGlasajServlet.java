package oprpp2.hw05.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oprpp2.hw05.dao.DAOProvider;

/**
 * Servlet za glasanje.
 * 
 * @author mskrabic
 *
 */
@WebServlet("/servleti/glasanje-glasaj")
public class GlasanjeGlasajServlet extends HttpServlet {
	
	/**
	 * Serijski broj.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String id = req.getParameter("id");
		String pollID = req.getParameter("pollID");
		
		DAOProvider.getDao().incrementVotes(Long.parseLong(pollID), Long.parseLong(id));
		
	
		resp.sendRedirect(req.getContextPath() + "/servleti/glasanje-rezultati?pollID="+ pollID);
	}
}
