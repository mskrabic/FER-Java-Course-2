package oprpp2.hw04.servlets;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oprpp2.hw04.util.Utils;

/**
 * Servlet za prikaz početne stranice aplikacije za glasanje - popis bendova.
 * 
 * @author mskrabic
 *
 */
public class GlasanjeServlet extends HttpServlet {
	
	/**
	 * Serijski broj.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");	
		List<Band> bands = Utils.readBands(fileName);
		
		Collections.sort(bands, (b1, b2) -> b1.id.compareTo(b2.id));
		
		req.setAttribute("bands", bands);
		
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
	}
	
	/**
	 * Razred modelira jedan bend.
	 * 
	 * @author mskrabic
	 *
	 */
	public static class Band {
		
		/**
		 * ID benda.
		 */
		private String id;
		
		/**
		 * Ime benda.
		 */
		private String name;
		
		/**
		 * URL jedne pjesme benda.
		 */
		private String url;
		
		/**
		 * Konstruktor.
		 * 
		 * @param id	ID benda.
		 * @param name	ime benda.
		 * @param url	URL jedne pjesme.
		 */
		public Band(String id, String name, String url) {
			this.id = id;
			this.name = name;
			this.url = url;
		}

		/**
		 * Getter za ID.
		 * 
		 * @return ID benda.
		 */
		public String getId() {
			return id;
		}

		/**
		 * Getter za ime.
		 * 
		 * @return ime benda.
		 */
		public String getName() {
			return name;
		}

		/**
		 * Getter za URL.
		 * 
		 * @return URL pjesme.
		 */
		public String getUrl() {
			return url;
		}
	}

}
