package oprpp2.hw06.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet za logout.
 * 
 * @author mskrabic
 *
 */
@WebServlet("/servleti/logout")
public class LogoutServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getSession().invalidate();
		resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/servleti/main"));
	}
}
