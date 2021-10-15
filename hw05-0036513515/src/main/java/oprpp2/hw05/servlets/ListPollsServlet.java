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

/**
 * Servlet za prikaz dostupnih anketa.
 * 
 * @author mskrabic
 *
 */
@WebServlet("/servleti/index.html")
public class ListPollsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		List<Poll> polls = DAOProvider.getDao().getPolls();
		req.setAttribute("polls", polls);
		
		req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req, resp);
	}

}
