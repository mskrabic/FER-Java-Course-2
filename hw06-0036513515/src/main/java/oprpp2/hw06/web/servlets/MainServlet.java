package oprpp2.hw06.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oprpp2.hw06.dao.DAOProvider;
import oprpp2.hw06.model.BlogUser;
import oprpp2.hw06.util.Util;

/**
 * Servlet za prikaz početne stranice bloga.
 * 
 * @author mskrabic
 */
@WebServlet("/servleti/main")
public class MainServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("users", DAOProvider.getDAO().listAllUsers());
		req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("users", DAOProvider.getDAO().listAllUsers());
		validate(req, resp);
	}

	/**
	 * Provjerava je li se korisnik uspješno ulogirao.
	 */
	private void validate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		
		String nickname = req.getParameter("nickname");
		String password = req.getParameter("password");
		String pwHash = Util.byteToHex(Util.hash(password));
		
		BlogUser user = DAOProvider.getDAO().getUser(nickname);
		if (user == null || user.getPasswordHash().compareTo(pwHash) != 0) {
			req.setAttribute("nickname", nickname);
			req.setAttribute("error", "Nickname or password incorrect.");
			req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
			return;
		}
		
		req.getSession().setAttribute("current.user.id", user.getId());
		req.getSession().setAttribute("current.user.fn", user.getFirstName());
		req.getSession().setAttribute("current.user.ln", user.getLastName());
		req.getSession().setAttribute("current.user.nick", user.getNickname());
		req.getSession().setAttribute("current.user.email", user.getEmail());
		
		resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/servleti/main"));
	}

}