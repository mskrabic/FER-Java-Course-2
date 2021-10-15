package oprpp2.hw06.web.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oprpp2.hw06.dao.DAOProvider;
import oprpp2.hw06.model.BlogComment;
import oprpp2.hw06.model.BlogEntry;
import oprpp2.hw06.model.BlogUser;
import oprpp2.hw06.model.form.EntryForm;

/**
 * Servlet za prikaz, dodavanje i uređivanje objava.
 * 
 * @author mskrabic
 *
 */
@WebServlet("/servleti/author/*")
public class AuthorServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String[] path = req.getPathInfo().substring(1).split("/");
		
		BlogUser author = DAOProvider.getDAO().getUser(path[0]);
		
		if (author == null || path.length > 2) {
			showError(req, resp, "Invalid request.");
			return;
		}
		
		req.setAttribute("author", author);

		if (path.length == 1) {
			List<BlogEntry> entries = DAOProvider.getDAO().getBlogEntriesByAuthor(author.getId());
			req.setAttribute("entries", entries);
			req.getRequestDispatcher("/WEB-INF/pages/entries.jsp").forward(req, resp);
			return;
		}
		
		req.setAttribute("flag", req.getPathInfo().endsWith("new") ? "new" : "edit");
		
		switch (path[1]) {
		case "new":
			if (validate(req, author))
				newEntry(req, resp, author);
			else
				showError(req, resp, "You're not allowed to post to this blog.");
			return;
		case "edit":
			if (validate(req, author))
				editEntry(req, resp, author);
			else
				showError(req, resp, "You're not allowed to modify this blog entry.");
			return;
		default:
			try {
				Long eID = Long.parseLong(path[1]);
				BlogEntry entry = DAOProvider.getDAO().getBlogEntry(eID);
				if (entry == null || Long.compare(entry.getAuthor().getId(), author.getId()) != 0) {
					showError(req, resp, "Requested entry (ID = " + eID + ") can't be found among this author's entries.");
					return;
				}
				List<BlogComment> comments = DAOProvider.getDAO().getComments(entry);
				req.setAttribute("entry", entry);
				req.setAttribute("comments", comments);
				req.getRequestDispatcher("/WEB-INF/pages/entry.jsp").forward(req, resp);
			} catch (Exception e) {
				showError(req, resp, "Invalid request.");
				return;
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BlogUser author = DAOProvider.getDAO().getUser(req.getPathInfo().substring(1).split("/")[0]);
		
		if (author == null) {
			showError(req, resp, "Invalid author nickname.");
			return;
		}

		if (req.getPathInfo().endsWith("newcomment")) {
			BlogComment comment = new BlogComment();
			comment.setMessage(req.getParameter("comment"));
			comment.setUsersEMail(req.getSession().getAttribute("current.user.email") == null ? "Anonymous" : req.getSession().getAttribute("current.user.email").toString());
			comment.setPostedOn(new Date());
			BlogEntry entry = DAOProvider.getDAO().getBlogEntry(Long.parseLong(req.getPathInfo().substring(1).split("/")[1]));
			comment.setBlogEntry(entry);
			DAOProvider.getDAO().persistComment(comment);
			
			resp.sendRedirect(req.getContextPath() + "/servleti/author/" + author.getNickname() + "/" + entry.getId());
			return;
		}
		EntryForm f = new EntryForm();
		f.fillFromRequest(req);
		f.validate();
		req.setAttribute("flag", req.getPathInfo().endsWith("new") ? "new" : "edit");
		if(f.hasErrors()) {
			req.setAttribute("form", f);
			req.setAttribute("author", author);
			req.getRequestDispatcher("/WEB-INF/pages/entryForm.jsp").forward(req, resp);
			return;
		}
		
		if (req.getPathInfo().endsWith("new")) {
			BlogEntry entry = new BlogEntry();
			f.fillEntry(entry);
			entry.setAuthor(author);
			DAOProvider.getDAO().persistEntry(entry);
		} else if (req.getPathInfo().endsWith("edit")){
			BlogEntry entry = DAOProvider.getDAO().getBlogEntry(Long.parseLong(f.getId()));
			f.fillEntry(entry);
		}
		
		resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/servleti/author/" + author.getNickname()));
	}

	/**
	 * Preusmjerava korisnika na uređivanje objave.
	 */
	private void editEntry(HttpServletRequest req, HttpServletResponse resp, BlogUser author) throws ServletException, IOException {
		BlogEntry entry = null;
		try {
			entry = DAOProvider.getDAO().getBlogEntry(Long.parseLong(req.getParameter("id")));
		} catch (Exception e) {
			showError(req, resp, "Invalid request.");
			return;
		}
		
		if (entry == null) {
			showError(req, resp, "Invalid request.");
			return;
		}
		EntryForm f = new EntryForm();
		f.fillFromEntry(entry);
		req.setAttribute("form", f);
		req.getRequestDispatcher("/WEB-INF/pages/entryForm.jsp").forward(req, resp);
	}

	/**
	 * Preusmjerava korisnika na dodavanje nove objave.
	 */
	private void newEntry(HttpServletRequest req, HttpServletResponse resp, BlogUser author) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/pages/entryForm.jsp").forward(req, resp);
	}

	/**
	 * Provjerava je li korisnik autoriziran mijenjati/dodavati objave na traženom blogu.
	 */
	private boolean validate(HttpServletRequest req, BlogUser author) {
		if (req.getSession().getAttribute("current.user.id") == null)
			return false;
		
		return Long.compare(author.getId(), (Long)req.getSession().getAttribute("current.user.id")) == 0;
	}
	
	/**
	 * Prikaz stranice s ispisom pogreške.
	 */
	private void showError(HttpServletRequest req, HttpServletResponse resp, String errorMessage) throws ServletException, IOException {
		req.setAttribute("error", errorMessage);
		req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
	}

}
