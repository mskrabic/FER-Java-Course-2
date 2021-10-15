package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;

/**
 * Čvor koji predstavlja cijeli dokument.
 * 
 * @author mskrabic
 *
 */
public class DocumentNode extends Node {

	/**
	 * Kolekcija za pohranjivanje čvorova djece.
	 */
	private ArrayIndexedCollection col;
	
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
	
	@Override
	public String toString() {
		StringBuilder document = new StringBuilder();
		for (int i = 0; i < col.size(); i++) {
			if (col.get(i) instanceof TextNode) {
				TextNode node = (TextNode) col.get(i);
				document.append(node.toString());
			} else if (col.get(i) instanceof ForLoopNode) {
				ForLoopNode node = (ForLoopNode) col.get(i);
				document.append(node.toString());
			} else if (col.get(i) instanceof EchoNode) {
				EchoNode node = (EchoNode) col.get(i);
				document.append(node.toString());		
			}
		}
		return document.toString();
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof DocumentNode)) {
			return false;
		}
		DocumentNode otherDocument = (DocumentNode) other;
		
		if (col.size() != otherDocument.col.size()) {
			return false;
		}
		Object[] arr1 = col.toArray();
		Object[] arr2 = otherDocument.col.toArray();
		
		for (int i = 0; i < col.size(); i++) {
			if (!arr1[i].equals(arr2[i]))
				return false;
		}
		return true;
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitDocumentNode(this);
		
	}

}
