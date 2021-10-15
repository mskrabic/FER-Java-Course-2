package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Tipovi tokena koje generira <code>SmartScriptLexer</code>.
 * 
 * @author mskrabic
 *
 */
public enum TokenType {
	/**
	 * Double konstanta.
	 */
	DOUBLE,
	/**
	 * Integer konstanta.
	 */
	INTEGER,
	/**
	 * Funkcija (naziv funkcije).
	 */
	FUNCTION,
	/**
	 * Operator (+, -, *, / ili ^)
	 */
	OPERATOR,
	/**
	 * String unutar tagova ili općeniti tekst izvan tagova.
	 */
	STRING, 
	/**
	 * Varijabla (naziv varijable).
	 */
	VARIABLE,
	/**
	 * Oznaka početka taga "{$".
	 */
	BEGINTAG,
	/**
	 * Oznaka kraja taga "$}".
	 */
	ENDTAG,
	/**
	 * Oznaka kraja ulaznog teksta.
	 */
	EOF;
}
