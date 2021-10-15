package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Sučelje modelira posjetitelja čvorova SmartScript skripte.
 * 
 * @author mskrabic
 *
 */
public interface INodeVisitor {
	/**
	 * Posjećuje tekstualni čvor {@link TextNode}.
	 * 
	 * @param node	čvor koji se posjećuje.
	 */
	public void visitTextNode(TextNode node);

	/**
	 * Posjećuje čvor for-petlje {@link ForLoopNode}.
	 * 
	 * @param node	čvor koji se posjećuje.
	 */
	public void visitForLoopNode(ForLoopNode node);

	/**
	 * Posjećuje echo čvor {@link EchoNode}.
	 * 
	 * @param node	čvor koji se posjećuje.
	 */
	public void visitEchoNode(EchoNode node);

	/**
	 * Posjećuje glavni (vršni) čvor {@link DocumentNode}.
	 * 
	 * @param node	čvor koji se posjećuje.
	 */
	public void visitDocumentNode(DocumentNode node);
}
