package hr.fer.zemris.java.custom.scripting.exec.functions;

import java.text.DecimalFormat;
import java.util.Stack;

import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Parsira vrijednost s vrha stoga u decimalni format i njime formatira sljedeću vrijednost na stogu.
 * 
 * @author mskrabic
 *
 */
public class DecimalFormatFunction implements Function{

	@Override
	public void execute(Stack<Object> stack, RequestContext context) {
		DecimalFormat f = new DecimalFormat(stack.pop().toString());
		Object x = stack.pop();
		stack.push(f.format(Double.parseDouble(x.toString())));
	}

}
