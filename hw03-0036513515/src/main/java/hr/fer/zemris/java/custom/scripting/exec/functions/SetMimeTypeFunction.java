package hr.fer.zemris.java.custom.scripting.exec.functions;

import java.util.Stack;

import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Postavlje MIME tip zahtjeva.
 * 
 * @author mskrabic
 *
 */
public class SetMimeTypeFunction implements Function {

	@Override
	public void execute(Stack<Object> stack, RequestContext context) {
		context.setMimeType(stack.pop().toString());
		
	}

}
