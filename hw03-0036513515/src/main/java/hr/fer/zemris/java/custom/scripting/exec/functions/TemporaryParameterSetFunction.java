package hr.fer.zemris.java.custom.scripting.exec.functions;

import java.util.Stack;

import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Postavlja vrijednost privremenog parametra.
 * 
 * @author mskrabic
 *
 */
public class TemporaryParameterSetFunction implements Function {

	@Override
	public void execute(Stack<Object> stack, RequestContext context) {
		String name = stack.pop().toString();
		String value = stack.pop().toString();
		context.setTemporaryParameter(name, value);
		
	}

}
