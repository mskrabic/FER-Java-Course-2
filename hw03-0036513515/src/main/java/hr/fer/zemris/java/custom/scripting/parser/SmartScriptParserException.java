package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Razred predstavlja iznimku koju <code>SmartScriptParser</code> baca u slučaju bilo kakve pogreške.
 * 
 * @author mskrabic
 *
 */
public class SmartScriptParserException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public SmartScriptParserException(String msg) {
		super(msg);
	}
	
	public SmartScriptParserException() {
		super("Invalid input!");
	}

}
