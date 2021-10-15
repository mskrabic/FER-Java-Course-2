package hr.fer.zemris.java.custom.scripting.exec.functions;

import java.util.Stack;

import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Postavlja vrijednost perzistentnog (trajnog) parametra.
 * 
 * @author mskrabic
 *
 */
public class PersistentParameterSetFunction implements Function {

	@Override
	public void execute(Stack<Object> stack, RequestContext context) {
		String name = stack.pop().toString();
		String value = stack.pop().toString();
		context.setPersistentParameter(name, value);
		
	}

}
