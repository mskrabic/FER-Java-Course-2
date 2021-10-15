package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Stanja u kojima se može naći <code>SmartScriptLexer</code>.
 * 
 * @author mskrabic
 *
 */
public enum SmartScriptLexerState {
	/**
	 * Stanje za rad izvan tagova.
	 */
	TEXT, 
	/**
	 * Stanje za rad unutar tagova.
	 */
	TAG
}
