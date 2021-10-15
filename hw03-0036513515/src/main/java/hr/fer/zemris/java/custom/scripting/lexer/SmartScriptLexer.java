package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Razred predstavlja leksički analizator koji zna tokenizirati ulaz koji se sastoji od teksta i
 * tagova ograničenih s {$ i $}. Leksički analizator je "lijen", tj. generira tokene tek kada ih vanjski klijent zatraži.
 * 
 * @author mskrabic
 *
 */
public class SmartScriptLexer {
	/**
	 * Ulazni tekst.
	 */
	private char[] data;
	
	/**
	 * Trenutna pozicija leksičkog analizatora.
	 */
	private int currentIndex;
	
	/**
	 * Zadnji pročitani token.
	 */
	private Token token;
	
	/**
	 * Trenutno stanje leksičkog analizatora.
	 */
	private SmartScriptLexerState state;
	
	/**
	 * Konstruktor koji prima ulazni tekst i inicijalizira leksički analizator.
	 * 
	 * @param input ulazni tekst.
	 */
	public SmartScriptLexer(String input) {
		if (input == null) 
			throw new NullPointerException("Input can't be null!");
		
		this.data = input.toCharArray();
		this.currentIndex = 0;
		this.state = SmartScriptLexerState.TEXT;
	}
	
	/**
	 * Metoda generira sljedeći token.
	 * 
	 * @return sljedeći token.
	 * 
	 * @throws SmartScriptLexerException ukoliko se lexer nalazi u nedefiniranom stanju.
	 */
	public Token nextToken() {
		switch (state) {
			case TEXT:
				return text();
			case TAG:
				return tag();
		}
		throw new SmartScriptLexerException("Invalid lexer state!");
	}
	
	/**
	 * Metoda vraća zadnji generirani token.
	 *  
	 * @return zadnji generirani token.
	 * 
	 * @throws NullPointerException ako prije poziva nije generiran ni jedan token.
	 */
	public Token getToken() {
		if (this.token == null)
			throw new NullPointerException("No tokens have been generated!");
		
		return this.token;
	}
	
	/**
	 * Metoda za postavljanje stanja lexera.
	 * 
	 * @param state stanje koje se želi postaviti.
	 */
	public void setState(SmartScriptLexerState state) {
		this.state = state;
	}
		
									/* ************************************************
									 * Privatne metode koje doprinose čitljivosti koda.
									 * ************************************************/
	/**
	 * Metoda koju leksički analizator koristi za tokeniziranje teksta.
	 */
	private Token text() {
		if (this.token != null && this.token.getType() == TokenType.EOF)
			throw new SmartScriptLexerException("Already generated EOF token!");
		
		StringBuilder sb = new StringBuilder();
		
		while (true) {
			if (currentIndex == data.length) {
				if (sb.isEmpty())
					this.token = new Token(TokenType.EOF, null);
				else 
					this.token = new Token(TokenType.STRING, sb.toString());
				break;
			}
			if (data[currentIndex] != '{') {
				if (data[currentIndex] == '\\') {
					currentIndex++;
					if (data[currentIndex] == '{' || data[currentIndex] == '\\') {
						sb.append(data[currentIndex++]);
					} else {
						throw new SmartScriptLexerException("Invalid escape!");
					}
				} else {
					sb.append(data[currentIndex++]);
				}	
			} else {
				if (currentIndex+1 < data.length && data[currentIndex+1] != '$') {
					sb.append(data[currentIndex++]);
				} else if (sb.length() > 0) {
					this.token = new Token(TokenType.STRING, sb.toString());
					break;
				} else {
					if (data[currentIndex+1] != '$')
						throw new SmartScriptLexerException("Invalid input!");
					currentIndex += 2;
					this.token = new Token(TokenType.BEGINTAG, "{$");
					return token;
				}
			}
		}
		return this.token;
	}
	
	/**
	 * Metoda koju leksički analizator koristi za tokeniziranje tagova.
	 */
	private Token tag() {
		if (this.token != null && this.token.getType() == TokenType.EOF) {
			throw new SmartScriptLexerException("Already generated EOF token!");
		}
		
		StringBuilder sb = new StringBuilder();
		skipBlankSpaces();
		
		if (data[currentIndex] == '=') {
			currentIndex++;
			if (getToken().getType() != TokenType.BEGINTAG)
				throw new SmartScriptLexerException("= is not a valid variable name!");
			this.token = new Token(TokenType.VARIABLE, "=");
			return this.token;
		}
		
		if (Character.isLetter(data[currentIndex])) {
			sb.append(data[currentIndex++]);
			
			while (Character.isLetter(data[currentIndex]) || Character.isDigit(data[currentIndex]) || data[currentIndex] == '_') {
				sb.append(data[currentIndex++]);
			}
			
			this.token = new Token(TokenType.VARIABLE, sb.toString());
			return this.token;
		}
		
		if (data[currentIndex] == '\"') {
			currentIndex++;
			
			while (currentIndex < data.length && data[currentIndex] != '$') {
				if (data[currentIndex] == '\\') {
					currentIndex++;
					if (data[currentIndex] == '\\' || data[currentIndex] == '\"') {
						sb.append(data[currentIndex++]);
					} else if (data[currentIndex] == 'n') {
						sb.append('\n');
						currentIndex++;
					} else if (data[currentIndex] == 'r') {
						sb.append('\r');
						currentIndex++;
					} else if (data[currentIndex] == 't') {
						sb.append('\t');
						currentIndex++;
					}	
				} else {
					if (data[currentIndex] == '"') {
						currentIndex++;
						break;
					}
					sb.append(data[currentIndex++]);
				}
			}
			this.token = new Token(TokenType.STRING, sb.toString());
			return this.token;	
		}
		
		if ((data[currentIndex] == '-' && Character.isDigit(data[currentIndex+1])) || Character.isDigit(data[currentIndex])) {
			sb.append(data[currentIndex++]);
			while (Character.isDigit(data[currentIndex])) {
				sb.append(data[currentIndex++]);
			}
			if (data[currentIndex] == '.') {
				sb.append(data[currentIndex++]);
				while (Character.isDigit(data[currentIndex])) {
					sb.append(data[currentIndex++]);
				}
				this.token = new Token(TokenType.DOUBLE, sb.toString());
			} else {
				this.token = new Token(TokenType.INTEGER, sb.toString());
			}
			return this.token;
		}
		
		if (isValidOperator(data[currentIndex])) {
			this.token = new Token(TokenType.OPERATOR, "" + data[currentIndex++]);
			
			return this.token;	
		}
		
		if (data[currentIndex] == '@') {
			sb.append(data[currentIndex++]);
			
			while (Character.isLetter(data[currentIndex]) || Character.isDigit(data[currentIndex]) || data[currentIndex] == '_') {
				sb.append(data[currentIndex++]);
			}
			
			this.token = new Token(TokenType.FUNCTION, sb.toString());
			return this.token;
		}
		if (data[currentIndex] == '$' && data[currentIndex+1] == '}') {
			currentIndex += 2;
			this.token = new Token(TokenType.ENDTAG, "$}");
			return this.token;
		}
		
		throw new SmartScriptLexerException("Invalid input!");
	}
		
	/**
	 * Metoda preskače uzastopne praznine u ulaznom tekstu, dok ne dođe do kraja teksta ili nepraznog znaka.
	 */
	private void skipBlankSpaces() {
		while (currentIndex < data.length) {
			if (isSpace())
				currentIndex++;
			else
				break;
		}
	}
	
	/**
	 * Metoda provjerava je li znak na trenutnom indeksu praznina.
	 * 
	 * @return <code>true</code> ako je praznina, <code>false</code> inače.
	 */
	private boolean isSpace() {
		return (data[currentIndex] == '\n' || data[currentIndex] == '\t' || data[currentIndex] == '\r' || data[currentIndex] == ' ');
	}
	
	/**
	 * Metoda provjerava je li predani string jedan od dozvoljenih operatora.
	 * 
	 * @param v string koji se želi provjeriti.
	 * 
	 * @return <code>true</code> ako je dozvoljeni operator, <code>false</code> inače.
	 */
	private boolean isValidOperator(char v) {
		return (v == '+' || v == '-' || v == '*' || v == '/' || v == '^');
	}

}
