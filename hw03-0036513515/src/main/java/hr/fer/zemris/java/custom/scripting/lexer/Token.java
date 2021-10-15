package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Razred predstavlja token, tj. leksičku jedinicu.
 * 
 * @author mskrabic
 *
 */
public class Token {
	
	/**
	 * Tip tokena.
	 */
	private TokenType type;
	
	/**
	 * Vrijednost tokena.
	 */
	private Object value;
	
	/**
	 * Konstruktor koji incijalizira vrijednosti tokena na predane vrijednosti.
	 * 
	 * @param type vrsta tokena.
	 * @param value vrijednost tokena.
	 */
	public Token(TokenType type, Object value) {
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Metoda vraća vrijednost tokena.
	 * 
	 * @return vrijednost tokena.
	 */
	public Object getValue() {
		return this.value;
	}
	
	/**
	 * Metoda vraća tip (vrstu) tokena.
	 * 
	 * @return tip tokena.
	 */
	public TokenType getType() {
		return this.type;
	}
}
