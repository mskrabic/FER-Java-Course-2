package oprpp2.hw04.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet koji postavlja boju pozadine za sve stranice web-aplikacije.
 * 
 * @author mskrabic
 *
 */
public class ColorServlet extends HttpServlet {
	
	/**
	 * Serijski broj.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		
		String color = req.getParameter("color");
		
		if (color != null) 
			session.setAttribute("pickedBgCol", color);
			
		req.getRequestDispatcher("/WEB-INF/pages/colors.jsp").forward(req, resp);
	}
}
