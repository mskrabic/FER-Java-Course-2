package hr.fer.zemris.java.webserver.workers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Radnik za postavljanje boje pozadine.
 * 
 * @author mskrabic
 *
 */
public class BgColorWorker implements IWebWorker {
	
	/**
	 * Regularni izraz za HEX znamenke.
	 */
	private static final Pattern HEX_PATTERN = Pattern.compile("[0-9a-fA-F]+");

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String bgColor = context.getParameter("bgcolor");
		if (bgColor != null && isRGB(bgColor)) {
			context.setPersistentParameter("bgcolor", bgColor);
			context.setTemporaryParameter("success", "uspješno");
		} else {
			context.setTemporaryParameter("success", "neuspješno");
		}
		
		context.getDispatcher().dispatchRequest("private/pages/color.smscr");		
	}

	/**
	 * Provjerava je li predani String valjan RGB (6 HEX znamenki).
	 * 
	 * @param bgColor RGB zapis boje.
	 * 
	 * @return <code>true</code> ako je zapis ispravan, <code>false</code> inače.
	 */
	private boolean isRGB(String bgColor) {
		Matcher m = HEX_PATTERN.matcher(bgColor);
		return m.matches() && bgColor.length() == 6;
	}

}
