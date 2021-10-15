package oprpp2.hw04.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import oprpp2.hw04.servlets.GlasanjeRezultatiServlet.Result;
import oprpp2.hw04.servlets.GlasanjeServlet.Band;
import oprpp2.hw04.util.Utils;

/**
 * Servlet za generiranje rezultata glasanja u XLS formatu.
 * 
 * @author mskrabic
 *
 */
public class GlasanjeXlsServlet extends HttpServlet {
	
	/**
	 * Serijski broj.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
		
		List<String> lines = Utils.readFrom(fileName);
		List<Band> bands = new ArrayList<>();
		for (String line : lines) {
			String[] parts = line.split("\t");
			if (parts.length != 3) continue;
			
			bands.add(new Band(parts[0], parts[1], parts[2]));
		}
		
		fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		lines = Utils.readFrom(fileName);	
		List<Result> results = new ArrayList<>();
		for (String line : lines) {
			String[] parts = line.split("\t");
			if (parts.length != 2) continue;
			for (Band band : bands) {
				if (band.getId().equals(parts[0])) {
					results.add(new Result(band, parts[1]));					
				}
			}
		}
		results.sort((r1, r2) -> r2.getVotes().compareTo(r1.getVotes()));
		
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet =  hwb.createSheet("Voting results");
		HSSFRow rowhead =  sheet.createRow((short)0);
		rowhead.createCell((short) 0).setCellValue("ID");
		rowhead.createCell((short) 1).setCellValue("Name");
		rowhead.createCell((short) 2).setCellValue("Votes");
		for (int i = 0; i < results.size(); i++) {
				HSSFRow row = sheet.createRow((short)(i+1));
				row.createCell((short) 0).setCellValue(Integer.parseInt(results.get(i).getBand().getId()));
				row.createCell((short) 1).setCellValue(results.get(i).getBand().getName());	
				row.createCell((short) 2).setCellValue(Integer.parseInt(results.get(i).getVotes()));		
		}
		resp.setContentType("application/vnd.ms-excel");
		resp.setHeader("Content-Disposition", "attachment; filename=\"votes.xls\"");
		
		hwb.write(resp.getOutputStream());
		hwb.close();
	}

}
