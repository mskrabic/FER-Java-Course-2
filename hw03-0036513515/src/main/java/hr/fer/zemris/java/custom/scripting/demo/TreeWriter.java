package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

/**
 * Posjećuje čvorove, parsira ih u stablo i ispisuje na standardni izlaz.
 * 
 * @author mskrabic
 *
 */
public class TreeWriter {
	
	/**
	 * Glavna metoda za pokretanje programa.
	 * 
	 * @param args prima jedan argument: put do SmartScript datoteke.
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Potreban je jedan argument: put do datoteke");
			return;
		}
		String docBody;
		try {
			docBody = Files.readString(Path.of(args[0]));
		} catch (IOException e) {
			System.out.println("Greška pri čitanju iz datoteke.");
			return;
		}
		SmartScriptParser p = new SmartScriptParser(docBody);
		WriterVisitor visitor = new WriterVisitor();
		p.getDocumentNode().accept(visitor);
	}
	
	/**
	 * Implementacija posjetitelja koja ispisuje čvorove na standardni izlaz.
	 * 
	 * @author mskrabic
	 *
	 */
	public static class WriterVisitor implements INodeVisitor {

		@Override
		public void visitTextNode(TextNode node) {
			System.out.print(node);
			
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			System.out.print(node);
			
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			System.out.print(node);
			
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			for (int i = 0; i < node.numberOfChildren(); i++) {
				node.getChild(i).accept(this);
			}
		}
	}

}
