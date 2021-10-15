package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Radnik koji računa sumu predanih brojeva i postavlja potrebne parametre za dodatnu obradu.
 * 
 * @author mskrabic
 *
 */
public class SumWorker implements IWebWorker{
	private final String image1 = "images/image1.jpg";
	private final String image2 = "images/image2.png";

	@Override
	public void processRequest(RequestContext context) throws Exception {
		int a, b;
		try {
			a = Integer.parseInt(context.getParameter("a"));
		} catch(NumberFormatException e) {
			a = 1;
		}
		try {
			b = Integer.parseInt(context.getParameter("b"));
		} catch (NumberFormatException e) {
			b = 2;
		}
		int sum = a+b;
		String imgName = (sum % 2 == 0) ? image1 : image2;
		context.setTemporaryParameter("varA",String.valueOf(a));
		context.setTemporaryParameter("varB", String.valueOf(b));
		context.setTemporaryParameter("zbroj", String.valueOf(sum));
		context.setTemporaryParameter("imgName", imgName);
		
		context.getDispatcher().dispatchRequest("private/pages/calc.smscr");
		
	}

}
