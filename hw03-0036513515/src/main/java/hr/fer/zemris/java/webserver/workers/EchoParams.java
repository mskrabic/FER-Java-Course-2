package hr.fer.zemris.java.webserver.workers;

import java.io.IOException;
import java.util.stream.Collectors;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Radnik koji ispisuje predane parametre u obliku HTML tablice.
 * 
 * @author mskrabic
 *
 */
public class EchoParams implements IWebWorker{

	@Override
	public void processRequest(RequestContext context) throws Exception {
		context.setMimeType("text/html");
		try {
			context.write("<html><head><style>table {font-family: arial, sans-serif;border-collapse: collapse;width: 100%;}");
			context.write("td, th {border: 1px solid #dddddd;text-align: left;padding: 8px;}");
			context.write("tr:nth-child(even) {background-color: #dddddd;}</style></head>");
			context.write("<body>");
			context.write("<h1>You've passed the following parameters:</h1>");
			context.write("<table><tr><th>Name</th><th>Value</th></tr>");
			for (var param : context.getParameterNames().stream().sorted().collect(Collectors.toList()))
				context.write("<tr><td>"+param+"</td><td>"+context.getParameter(param)+"</td></tr>");
			context.write("</table>");
			context.write("</body></html>");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
	}

}
