package hr.fer.zemris.java.custom.scripting.exec.functions;

import java.util.Stack;

import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Zamjenjuje mjesta prvih dviju stavki na stogu.
 * 
 * @author mskrabic
 *
 */
public class SwapFunction implements Function {

	@Override
	public void execute(Stack<Object> stack, RequestContext context) {
		Object a = stack.pop();
		Object b = stack.pop();
		stack.push(a);
		stack.push(b);	
	}
	

}
