package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.scripting.elems.*;

/**
 * Razred je model čvora koji predstavlja for-petlju.
 * 
 * @author mskrabic
 */
public class ForLoopNode extends Node {
	
	/**
	 * Kolekcija za pohranjivanje čvorova djece.
	 */
	private ArrayIndexedCollection col;
	
	/**
	 * Prvi član for-petlje, varijabla.
	 */
	private ElementVariable variable;
	
	/**
	 * Početni izraz for-petlje.
	 */
	private Element startExpression;
	
	/**
	 * Uvjet zaustavljanja for-petlje.
	 */
	private Element endExpression;
	
	/**
	 * Korak for-petlje, može biti izostavljen.
	 */
	private Element stepExpression;
	
	/**
	 * Konstruktor koji inicijalizira sve dijelove for-petlje.
	 * 
	 * @param variable varijabla for-petlje.
	 * @param startExpression početni izraz for-petlje.
	 * @param endExpression završni izraz for-petlje.
	 * @param stepExpression korak for-petlje.
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression, Element stepExpression) {
		if (variable == null || startExpression == null || endExpression == null)
			throw new NullPointerException("Invalid ForLoopNode!");
		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
	}
	
	/**
	 * Konstruktor koji se koristi u slučaju da je korak for-petlje izostavljen.
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression) {
		this(variable, startExpression, endExpression, null);
	}

	@Override
	public void addChildNode(Node child) {
		if (col == null)
			col = new ArrayIndexedCollection();
		col.add(child);
	}

	@Override
	public int numberOfChildren() {
		return col.size();
	}

	@Override
	public Node getChild(int index) {
		return (Node) col.get(index);
	}
	
	public Element[] getElements() {
		return new Element[] {variable, startExpression, endExpression, stepExpression};
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{$for ");
		sb.append(variable.asText() + " ");
		if (startExpression instanceof ElementString) {
			sb.append('"' + startExpression.toString() + "\" ");
		} else {
			sb.append(startExpression.asText() + " ");
		}
		if (endExpression instanceof ElementString) {
			sb.append('"' + endExpression.toString() + "\" ");
		} else {
			sb.append(endExpression.asText() + " ");	
		}
		if (stepExpression instanceof ElementString) {
			sb.append('"' + stepExpression.toString() + "\" ");
		} else {
			sb.append(((stepExpression == null) ? "" : stepExpression.asText()));
		}
		sb.append("$}");
		if (col != null) {
			for (int i = 0; i < col.size(); i++) {
				sb.append(getChild(i));
			}
		}
		sb.append("{$END$}");
		return sb.toString();			
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof ForLoopNode))
			return false;
		
		ForLoopNode otherNode = (ForLoopNode) other;
		
		return (variable.equals(otherNode.variable)
				&& startExpression.equals(otherNode.startExpression)
				&& endExpression.equals(otherNode.endExpression)
				&& ((stepExpression == null) ? (otherNode.stepExpression == null) : stepExpression.equals(otherNode.stepExpression)));
	}
	
	public ElementVariable getVariable() {
		return variable;
	}
	
	public Element getStartExpression() {
		return startExpression;
	}
	
	public Element getStepExpression() {
		return stepExpression;
	}

	
	public Element getEndExpression() {
		return endExpression;
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitForLoopNode(this);
		
	}
}
