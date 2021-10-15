package oprpp2.hw06.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oprpp2.hw06.dao.DAOProvider;
import oprpp2.hw06.model.BlogUser;
import oprpp2.hw06.model.form.RegisterForm;

/**
 * Servlet za registraciju novih korisnika.
 *
 * @author mskrabic
 *
 */
@WebServlet("/servleti/register")
public class RegistrationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RegisterForm rf = new RegisterForm();
		req.setAttribute("form", rf);
		req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		validate(req, resp);
	}
	
	/**
	 * Provjerava jesu li uneseni ispravni korisnički podatci.
	 */
	private void validate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");

		RegisterForm f = new RegisterForm();
		f.fillFromRequest(req);
		f.validate();
		
		if (DAOProvider.getDAO().userExists(f.getNickname())) {
			f.setError("nickname", "Traženi nickname je zauzet!");
		}
		
		if(f.hasErrors()) {
			req.setAttribute("form", f);
			req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
			return;
		}
			
		BlogUser user = new BlogUser();
		f.fillUser(user);
		DAOProvider.getDAO().persistUser(user);

		resp.sendRedirect(req.getServletContext().getContextPath() + "/servleti/main");
	}
}
