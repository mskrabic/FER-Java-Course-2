package hr.fer.zemris.java.custom.scripting.exec.functions;

import java.util.Stack;

import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Dohvaća perzistentni (trajni) parametar.
 * 
 * @author mskrabic
 *
 */
public class PersistentParameterGetFunction implements Function {

	@Override
	public void execute(Stack<Object> stack, RequestContext context) {
		Object dv = stack.pop();
		String name = stack.pop().toString();
		String value = context.getPersistentParameter(name);
		stack.push(value == null ? dv : value);
	}
}