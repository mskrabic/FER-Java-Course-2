package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;

/**
 * Razred predstavlja prazni čvor (ne može imati djecu), EchoNode.
 * 
 * @author mskrabic
 *
 */
public class EchoNode extends Node {
	/**
	 * Sadržaj čvora.
	 */
	private Element[] elements;
	
	/**
	 * Konstruktor koji postavlja vrijednosti elementima u čvoru.
	 * @param elements
	 */
	public EchoNode(Element[] elements) {
		this.elements = elements;
	}
	
	/**
	 * Metoda vraća elemente čvora.
	 * 
	 * @return elementi čvora.
	 */
	public Element[] getElements() {
		return elements;
	}

	@Override
	public void addChildNode(Node child) {
		throw new RuntimeException("EchoNode is a leaf node! It can't have children!");
		
	}

	@Override
	public int numberOfChildren() {
		return 0;
	}

	@Override
	public Node getChild(int index) {
		throw new RuntimeException("EchoNode is a leaf node! It can't have children!");
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{$=");
		for (Element e : getElements()) {
			if (e instanceof ElementString) {
				sb.append('"' + e.toString() + "\" ");
			} else {
				sb.append(e.asText() + " ");
			}
		}
		sb.append("$}");
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof EchoNode))
			return false;
		
		EchoNode otherNode = (EchoNode) other;
		
		Element[] elements1 = this.getElements();
		Element[] elements2 = otherNode.getElements();
		
		if (elements1.length != elements2.length)
			return false;
		
		for (int i = 0; i < elements1.length; i++) {
			if (!(elements1[i].equals(elements2[i])))
				return false;
		}
		return true;
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitEchoNode(this);
		
	}		
}
