package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Osnovni razred koji predstavlja čvor. Svi ostali čvorovi nasljeđuju ovaj razred.
 * 
 * @author mskrabic
 *
 */
public abstract class Node {
	
	/**
	 * Metoda dodaje dijete trenutnom čvoru. Pri prvom dodavanju djeteta inicijalizira se interna kolekcija.
	 * 
	 * @param child dijete koje se želi dodati.
	 * 
	 * @throws NullPointerException ako se pokuša dodati <code>null</code> umjesto djeteta.
	 */
	public abstract void addChildNode(Node child);
	
	/**
	 * Metoda vraća broj djece trenutnog čvora.
	 * 
	 * @return broj djece čvora.
	 */
	public abstract int numberOfChildren();
	
	/**
	 * Metoda dohvaća dijete s tražene pozicije.
	 * 
	 * @param index pozicija traženog djeteta.
	 * 
	 * @return dijete s tražene pozicije.
	 * 
	 * @throws IndexOutOfBoundsException ako se preda neispravan index.
	 */
	public abstract Node getChild(int index);
	
	/**
	 * Metoda prihvaća posjetitelja definiranog sučeljem {@link INodeVisitor}.
	 * 
	 * @param visitor	posjetitelj.
	 */
	public abstract void accept(INodeVisitor visitor);

}
