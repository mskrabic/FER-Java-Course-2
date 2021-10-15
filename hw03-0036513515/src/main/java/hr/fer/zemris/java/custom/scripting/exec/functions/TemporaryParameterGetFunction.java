package hr.fer.zemris.java.custom.scripting.exec.functions;

import java.util.Stack;

import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Dohvaća privremeni parametar.
 * 
 * @author mskrabic
 *
 */
public class TemporaryParameterGetFunction implements Function {

	@Override
	public void execute(Stack<Object> stack, RequestContext context) {
		Object dv = stack.pop();
		String name = stack.pop().toString();
		String value = context.getTemporaryParameter(name);
		stack.push(value == null ? dv : value);
	}
}