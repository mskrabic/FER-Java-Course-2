package hr.fer.zemris.java.custom.scripting.exec.functions;

import java.util.Stack;

import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Kopira vrijednost s vrha stoga i dodaje tu kopiju na vrh stoga.
 * 
 * @author mskrabic
 *
 */
public class DuplicateFunction implements Function{

	@Override
	public void execute(Stack<Object> stack, RequestContext context) {
		Object x = stack.pop();
		stack.push(x);
		stack.push(x);
		
	}

}
