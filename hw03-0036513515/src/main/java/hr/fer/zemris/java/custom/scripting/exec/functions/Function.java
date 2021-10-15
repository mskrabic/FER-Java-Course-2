package hr.fer.zemris.java.custom.scripting.exec.functions;

import java.util.Stack;

import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Sučelje modelira funkciju koju {@link SmartScriptEngine} zna izvršiti.
 * 
 * @author mskrabic
 *
 */
public interface Function {
	
	/**
	 * Izvršava funkciju.
	 * 
	 * @param stack		stog koji se koristi.
	 * @param context	kontekst zahtjeva.
	 */
	void execute(Stack<Object> stack, RequestContext context);

}
