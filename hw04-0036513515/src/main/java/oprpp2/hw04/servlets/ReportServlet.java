package oprpp2.hw04.servlets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 * Servlet za generiranje kružnog dijagrama (pie chart).
 * 
 * @author mskrabic
 *
 */
public class ReportServlet extends HttpServlet {

	/**
	 * Serijski broj.
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		response.setContentType("image/png");

		OutputStream outputStream = response.getOutputStream();

		JFreeChart chart = getChart();
		int width = 500;
		int height = 350;
		ChartUtils.writeChartAsPNG(outputStream, chart, width, height);
	}

	/**
	 * Pomoćna metoda za stvaranje dijagrama.
	 * 
	 * @return dijagram.
	 */
	private JFreeChart getChart() {
		DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("Linux", 29);
        dataset.setValue("Mac", 19);
        dataset.setValue("Windows", 51);
        dataset.setValue("Temple OS", 1);

		boolean legend = true;
		boolean tooltips = false;
		boolean urls = false;

		JFreeChart chart = ChartFactory.createPieChart("Which OS are you using?", dataset, legend, tooltips, urls);

		chart.setBorderPaint(Color.BLUE);
		chart.setBorderStroke(new BasicStroke(5.0f));
		chart.setBorderVisible(true);

		return chart;
	}

}