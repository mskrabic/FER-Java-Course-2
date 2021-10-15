package hr.fer.zemris.java.custom.scripting.exec.functions;

import java.util.Stack;

import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Briše privremeni parametar.
 * 
 * @author mskrabic
 *
 */
public class TemporaryParameterDeleteFunction implements Function {

	@Override
	public void execute(Stack<Object> stack, RequestContext context) {
		context.removeTemporaryParameter(stack.pop().toString());
		
	}

}
