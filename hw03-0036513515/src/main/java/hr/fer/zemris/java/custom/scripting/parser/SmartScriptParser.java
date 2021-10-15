package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.elems.*;
import hr.fer.zemris.java.custom.scripting.lexer.*;
import hr.fer.zemris.java.custom.scripting.nodes.*;

/**
 * Razred predstavlja parser koji iz zadanog teksta gradi stablo koristeći <code>Node</code> hijerarhiju.
 * 
 * @author mskrabic
 *
 */
public class SmartScriptParser {
	/**
	 * Vršni čvor dokumenta.
	 */
	private DocumentNode document;
	
	/**
	 * Leksički analizator koji tokenizira ulazni tekst.
	 */
	private SmartScriptLexer lexer;
	
	/**
	 * Stog koji se koristi pri izgradnji strukture dokumenta.
	 */
	private ObjectStack stack;
	
	/**
	 * Konstruktor koji inicijalizira stog, leksički analizator i započinje parsiranje.
	 * 
	 * @param input ulazni tekst.
	 * 
	 * @throws SmartScriptParserException u slučaju bilo koje pogreške u radu.
	 */
	public SmartScriptParser(String input) {
		this.document = new DocumentNode();
		this.stack = new ObjectStack();
		stack.push(document);
		this.lexer = new SmartScriptLexer(input);
		try {
			parseText();
		} catch (Exception e) {
			throw new SmartScriptParserException(e.getMessage());
		}
	}
	
	/**
	 * Metoda vraća vršni čvor u dokumentu.
	 * 
	 * @return DocumentNode koji sadrži cijeli dokument.
	 */
	public DocumentNode getDocumentNode() {
		return this.document;
	}

										/* ************************************************
										 * Privatne metode koje doprinose čitljivosti koda.
										 * ************************************************/
	/**
	 * Metoda za parsiranje teksta.
	 * 
	 * @throws SmartScriptParserException ako tekst nije ispravno tokeniziran.
	 */
	private void parseText() {
		while (true) {
			Token t = lexer.nextToken();
			if (t.getType() == TokenType.STRING) {
				TextNode node = new TextNode(t.getValue().toString());
				Node lastNode = (Node)stack.peek();
				lastNode.addChildNode(node);
			} else if (t.getType() == TokenType.BEGINTAG) {
				lexer.setState(SmartScriptLexerState.TAG);
				parseTag();
			} else if (t.getType() == TokenType.EOF) {
				break;
			} else {
				throw new SmartScriptParserException("Illegal token type outside of tags!");
			}
		}
		if (stack.size() != 1)
			throw new SmartScriptParserException("Too many nodes still on the stack, parse failed!");
	}
	
	/**
	 * Metoda za parsiranje tagova.
	 * 
	 * @throws SmartScriptParserException ako je tag neispravan.
	 */
	private void parseTag() {
		Token t = lexer.nextToken();
		if (t.getType() == TokenType.VARIABLE) {
			if (((String)t.getValue()).equalsIgnoreCase("for")) {
				parseFor();
			} else if(((String)t.getValue()).equals("=")) {
				parseEcho();
			} else if (((String)t.getValue()).equalsIgnoreCase("end")) {
				parseEnd();
			} else {
				throw new SmartScriptParserException("Invalid tag name!");
			}
		} else {
			throw new SmartScriptParserException("Invalid tag!");
		}
	}
	
	/**
	 * Metoda za parsiranje FOR-taga.
	 * 
	 * @throws SmartScriptParserException ako je u tagu neispravna varijabla ili neispravan broj argumenata.
	 */
	private void parseFor() {
		Token temp = lexer.nextToken();
		
		if (temp.getType() != TokenType.VARIABLE)
			throw new SmartScriptParserException("Invalid variable in for-tag!");
		
		ElementVariable variable = new ElementVariable((String)temp.getValue());
		Element[] elements = new Element[3];
		for (int i = 0; i < 3; i++) {
			temp = lexer.nextToken();	
			if (temp.getType() == TokenType.ENDTAG) {
				if (i == 2) {
					break;
				} else {
					throw new SmartScriptParserException("Too few elements in for-tag!");
				}
			} else if (temp.getType() == TokenType.VARIABLE || temp.getType() == TokenType.STRING
						|| temp.getType() == TokenType.DOUBLE || temp.getType() == TokenType.INTEGER) {
				elements[i] = createElement(temp);
			} else {
				throw new SmartScriptParserException("Invalid element in for-tag! VARIABLE, STRING, DOUBLE"
						+ " or INTEGER allowed, but was : " + temp.getType() + ".");
			}
		}
		if (elements[2] != null)
			temp = lexer.nextToken();
		if (temp.getType() != TokenType.ENDTAG) {
			throw new SmartScriptParserException("Too many elements in for-tag!");
		}
		ForLoopNode node = new ForLoopNode(variable, elements[0], elements[1], elements[2]);
		Node lastNode = (Node)stack.peek();
		lastNode.addChildNode(node);
		stack.push(node);
		lexer.setState(SmartScriptLexerState.TEXT);	
	}
	
	/**
	 * Metoda za parsiranje ECHO-taga.
	 * 
	 * @throws SmartScriptParserException ako EchoNode nema elemenata.
	 */
	private void parseEcho() {
		ArrayIndexedCollection col = new ArrayIndexedCollection();
		
		while (true) {
			Token temp = lexer.nextToken();
			if (temp.getType() == TokenType.ENDTAG) {
				break;
			}
			col.add(createElement(temp));
		}
		Element[] elements = new Element[col.size()];
		for (int i = 0; i < col.size(); i++) {
			elements[i] = (Element) col.get(i);
		}
		EchoNode node = new EchoNode(elements);
		Node lastNode = (Node)stack.peek();
		lastNode.addChildNode(node);
		lexer.setState(SmartScriptLexerState.TEXT);
	}
	
	/**
	 * Metoda za parsiranje END-taga.
	 * 
	 * @throws SmartScriptParserException ako END-tag nije prazan ili nema pripadnog FOR-taga.
	 */
	private void parseEnd() {
		Token temp = lexer.nextToken();
		if (temp.getType() != TokenType.ENDTAG)
			throw new SmartScriptParserException("End tag must be empty!");
		if (stack.pop() instanceof ForLoopNode) {
			lexer.setState(SmartScriptLexerState.TEXT);	
		} else {
			throw new SmartScriptParserException("There are no opened FOR-tags!");
		}
	}
	
	/**
	 * Metoda stvara Element iz predanog tokena.
	 * 
	 * @param t token koji se želi parsirati u Element.
	 * 
	 * @return Element nastao iz tokena
	 * 
	 * @throws SmartScriptParserException u slučaju bilo kakve pogreške.
	 */
	private Element createElement(Token t) {
		String value = (String)t.getValue();
		if (t.getType() == TokenType.VARIABLE) {
			return new ElementVariable(value);						
		} else if (t.getType() == TokenType.INTEGER) {
			return new ElementConstantInteger(Integer.parseInt(value));
		} else if (t.getType() == TokenType.DOUBLE) {
			return new ElementConstantDouble(Double.parseDouble(value));
		} else if (t.getType() == TokenType.FUNCTION) {
			return new ElementFunction(value);						
		} else if (t.getType() == TokenType.OPERATOR) {
			return new ElementOperator(value);						
		} else if (t.getType() == TokenType.STRING) {
			return new ElementString(value);	
		} else {
			throw new SmartScriptParserException("Invalid tag element!");
		}		
	}
}