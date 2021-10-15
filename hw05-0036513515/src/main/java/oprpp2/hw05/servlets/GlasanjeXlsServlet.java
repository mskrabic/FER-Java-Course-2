package oprpp2.hw05.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import oprpp2.hw05.dao.DAOProvider;
import oprpp2.hw05.model.PollOption;

/**
 * Servlet za generiranje rezultata glasanja u XLS formatu.
 * 
 * @author mskrabic
 *
 */
@WebServlet("/servleti/glasanje-xls")
public class GlasanjeXlsServlet extends HttpServlet {
	
	/**
	 * Serijski broj.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		long pollID = Long.parseLong(req.getParameter("pollID"));
		List<PollOption> results = DAOProvider.getDao().getPollOptions(pollID);

		results.sort((r1, r2) -> Integer.compare(r2.getVotesCount(), r1.getVotesCount()));
		
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet =  hwb.createSheet("Voting results");
		HSSFRow rowhead =  sheet.createRow((short)0);
		rowhead.createCell((short) 0).setCellValue("ID");
		rowhead.createCell((short) 1).setCellValue("Name");
		rowhead.createCell((short) 2).setCellValue("Votes");
		for (int i = 0; i < results.size(); i++) {
				HSSFRow row = sheet.createRow((short)(i+1));
				row.createCell((short) 0).setCellValue(results.get(i).getId());
				row.createCell((short) 1).setCellValue(results.get(i).getTitle());	
				row.createCell((short) 2).setCellValue(results.get(i).getVotesCount());		
		}
		resp.setContentType("application/vnd.ms-excel");
		resp.setHeader("Content-Disposition", "attachment; filename=\"votes.xls\"");
		
		hwb.write(resp.getOutputStream());
		hwb.close();
	}

}
