package oprpp2.hw05.servlets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import oprpp2.hw05.dao.DAOProvider;
import oprpp2.hw05.model.PollOption;

/**
 * Servlet za generiranje grafičkog prikaza (pie chart) rezultata glasanja.
 * 
 * @author mskrabic
 *
 */
@WebServlet("/servleti/glasanje-grafika")
public class GlasanjeGrafikaServlet extends HttpServlet {
	
	/**
	 * Serijski broj.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		long pollID = Long.parseLong(req.getParameter("pollID"));
		List<PollOption> results = DAOProvider.getDao().getPollOptions(pollID);

		resp.setContentType("image/png");

		OutputStream outputStream = resp.getOutputStream();

		JFreeChart chart = getChart(results);
		int width = 500;
		int height = 350;
		ChartUtils.writeChartAsPNG(outputStream, chart, width, height);
	}

	/**
	 * Pomoćna metoda za generiranje dijagrama.
	 * 
	 * @param results	rezultati glasanja.
	 * 
	 * @return	dijagram.
	 */
	private JFreeChart getChart(List<PollOption> results) {
		DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (PollOption r : results)
        	if (r.getVotesCount() > 0)
        		dataset.setValue(r.getTitle(), r.getVotesCount());
		boolean legend = true;
		boolean tooltips = false;
		boolean urls = false;

		JFreeChart chart = ChartFactory.createPieChart("Voting results", dataset, legend, tooltips, urls);

		chart.setBorderPaint(Color.BLUE);
		chart.setBorderStroke(new BasicStroke(5.0f));
		chart.setBorderVisible(true);

		return chart;
	}
}
