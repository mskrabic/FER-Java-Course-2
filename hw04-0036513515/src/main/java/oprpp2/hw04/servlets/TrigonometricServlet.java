package oprpp2.hw04.servlets;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet za izračun trigonometrijskih vrijednosti zadanih parametara.
 * 
 * @author mskrabic
 *
 */
public class TrigonometricServlet  extends HttpServlet {
	
	/**
	 * Serijski broj.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Format zapisa rezultata.
	 */
	private static final DecimalFormat FORMAT = new DecimalFormat("0.00000");

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int a, b;
		try {
			a = Integer.parseInt(req.getParameter("a"));
		} catch (NumberFormatException e) {
			a = 0;
		}
		try {
			b = Integer.parseInt(req.getParameter("b"));
		} catch (NumberFormatException e) {
			b = 360;
		}
		if (a > b) {
			int temp = a;
			a = b;
			b = temp;
		}
		if (b > a + 720)
			b = a + 720;
		
		Table t = new Table();
		double angle;
		for (int i = a; i <= b; i++) {
			angle = (double)i/360 * 2 * Math.PI;
			t.angleList.add(i);			
			t.sinList.add(FORMAT.format(Math.sin(angle)));
			t.cosList.add(FORMAT.format(Math.cos(angle)));
		}
		req.setAttribute("table", t);
		req.setAttribute("a", a);
		req.setAttribute("b", b);
		req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(req, resp);
	}
	
	/**
	 * Pomoćni razred za spremanje izračunatih podataka.
	 * 
	 * @author mskrabic
	 *
	 */
	public static class Table {
		/**
		 * Kutevi za koje su se računale trigonometrijske vrijednosti.
		 */
		private List<Integer> angleList = new ArrayList<>();
		
		/**
		 * Vrijednosti sinusa.
		 */
		private List<String> sinList = new ArrayList<>();
		
		/**
		 * Vrijednosti kosinusa.
		 */
		private List<String> cosList = new ArrayList<>();
		
		/**
		 * Getter za kuteve.
		 * 
		 * @return lista kuteva.
		 */
		public List<Integer> getAngleList() {
			return angleList;
		}
		
		/**
		 * Getter za sinuse.
		 * 
		 * @return lista sinusa.
		 */
		public List<String> getSinList() {
			return sinList;
		}
		
		/**
		 * Getter za kosinuse.
		 * 
		 * @return lista kosinusa.
		 */
		public List<String> getCosList() {
			return cosList;
		}	
	}
}
