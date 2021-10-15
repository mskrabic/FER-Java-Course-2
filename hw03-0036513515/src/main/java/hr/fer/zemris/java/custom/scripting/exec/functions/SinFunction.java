package hr.fer.zemris.java.custom.scripting.exec.functions;

import java.util.Stack;

import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Računa sinus vrijednosti s vrha stoga i stavlja rezultat na stog.
 * 
 * @author mskrabic
 *
 */
public class SinFunction implements Function {

	@Override
	public void execute(Stack<Object> stack, RequestContext context) {
		Double x = Double.parseDouble(stack.pop().toString());
		stack.push(Math.sin(x/(360)*Math.PI*2));
	}

}
