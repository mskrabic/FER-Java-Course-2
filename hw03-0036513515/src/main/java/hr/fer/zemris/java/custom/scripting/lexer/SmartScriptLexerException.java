package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Razred predstavlja iznimku koju leksički analizator baca u slučaju pogreške.
 * 
 * @author mskrabic
 *
 */
public class SmartScriptLexerException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public SmartScriptLexerException(String message){
		super(message);
	}

	public SmartScriptLexerException() {
		super("Lexer error!");
	}

}
