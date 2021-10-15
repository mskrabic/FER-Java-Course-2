package oprpp2.hw04.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;  
import org.apache.poi.hssf.usermodel.HSSFWorkbook; 
import org.apache.poi.hssf.usermodel.HSSFRow; 

/**
 * Servlet za generiranje MS Excel datoteke s potencijama zadanih brojeva.
 * 
 * @author mskrabic
 *
 */
public class PowerServlet extends HttpServlet {
	
	/**
	 * Serijski broj.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Pretpostavljena vrijednost - služi za otkrivanje greške.
	 */
	private static final int DEFAULT_VALUE = -101;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int a = DEFAULT_VALUE;
		int b = DEFAULT_VALUE;
		int n = DEFAULT_VALUE;
		
		try {
			a = Integer.parseInt(req.getParameter("a"));
			b = Integer.parseInt(req.getParameter("b"));
			n = Integer.parseInt(req.getParameter("n"));
		} catch (NumberFormatException e) {}
		
		if (a < -100 || a > 100 || b < -100 || b > 100 || n < 1 || n > 5) {
			req.setAttribute("MSG", "You've passed invalid parameters.");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		HSSFWorkbook hwb = new HSSFWorkbook();

		for (int i = 1; i <= n; i++) {
			HSSFSheet sheet =  hwb.createSheet("Sheet " + i);
			HSSFRow rowhead =  sheet.createRow((short)0);
			rowhead.createCell((short) 0).setCellValue("x");
			rowhead.createCell((short) 1).setCellValue("x^" + i);
			
			short rowCount = 1;
			for (int j = a; j <= b; j++) {
				HSSFRow row = sheet.createRow((short)rowCount++);
				row.createCell((short) 0).setCellValue(j);
				row.createCell((short) 1).setCellValue(Math.pow(j, i));		
			}
		}
		resp.setContentType("application/vnd.ms-excel");
		resp.setHeader("Content-Disposition", "attachment; filename=\"tablica.xls\"");
		
		hwb.write(resp.getOutputStream());
		hwb.close();
		
	}
}