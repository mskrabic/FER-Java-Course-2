package oprpp2.hw05.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet koji preusmjerava prvi zahtjev korisnika na popis anketa.
 * 
 * @author mskrabic
 *
 */
@WebServlet("/index.html")
public class IndexServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/servleti/index.html"));
	}

}
