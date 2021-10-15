package hr.fer.zemris.java.custom.scripting.exec.functions;

import java.util.Stack;

import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Briše perzistentni (trajni)  parametar.
 * 
 * @author mskrabic
 *
 */
public class PersistentParameterDeleteFunction implements Function {

	@Override
	public void execute(Stack<Object> stack, RequestContext context) {
		context.removePersistentParameter(stack.pop().toString());
		
	}

}
