package hr.fer.zemris.java.custom.collections;

/**
 * Razred predstavlja iznimku koja se baca pri pokušaju pristupa elementima praznog stoga.
 * .
 * @author mskrabic
 *
 */
public class EmptyStackException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public EmptyStackException(String message){
		super(message);
	}

	public EmptyStackException() {
		super("Stack is empty!");
	}
}
