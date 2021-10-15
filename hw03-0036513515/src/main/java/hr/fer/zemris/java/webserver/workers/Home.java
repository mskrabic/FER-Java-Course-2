package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Radnik koji otvara početnu stranicu.
 * 
 * @author mskrabic
 *
 */
public class Home implements IWebWorker {
	private static final String DEFAULT_COLOR = "7F7F7F";

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String bgcolor = context.getPersistentParameter("bgcolor");
		if (bgcolor == null)
			bgcolor = DEFAULT_COLOR;
		
		context.setTemporaryParameter("background", bgcolor);
		
		context.getDispatcher().dispatchRequest("private/pages/home.smscr");
	}

}
