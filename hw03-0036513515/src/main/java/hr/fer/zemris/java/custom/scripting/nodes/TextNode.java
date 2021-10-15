package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Razred predstavlja tekstualni čvor.
 * 
 * @author mskrabic
 *
 */
public class TextNode extends Node {
	
	/**
	 * Tekstualni sadržaj čvora.
	 */
	private String text;
	
	/**
	 * Konstruktor koji inicijalizira tekstualni sadržaj.
	 * 
	 * @param tekstualni sadržaj.
	 */
	public TextNode(String text) {
		this.text = text;
	}
	
	/**
	 * Metoda vraća tekstualni sadržaj čvora.
	 * 
	 * @return tekstualni sadržaj čvora.
	 */
	public String getText() {
		return text;
	}

	@Override
	public void addChildNode(Node child) {
		throw new RuntimeException("TextNode is a leaf node! It can't have children!");
		
	}

	@Override
	public int numberOfChildren() {
		return 0;
	}

	@Override
	public Node getChild(int index) {
		throw new RuntimeException("TextNode is a leaf node! It can't have children!");
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof TextNode))
			return false;
		
		TextNode otherNode = (TextNode) other;
		
		return getText().equals(otherNode.getText());
	}
	
	/**
	 * Nadjačanu metodu toString() koristimo pri rekonstrukciji dokumenta radi pravilnog "escapanja" specijalnih znakova.
	 */
	@Override 
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (char c : this.getText().toCharArray()) {
			if (c == '{' || c == '\\') {
				sb.append("\\" + c);
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitTextNode(this);
	}

}
