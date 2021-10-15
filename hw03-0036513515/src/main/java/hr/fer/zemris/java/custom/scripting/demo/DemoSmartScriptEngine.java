package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Razred demonstrira rad razreda {@link SmartScriptEngine}.
 * 
 * @author mskrabic
 *
 */
public class DemoSmartScriptEngine {
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("A single argument expected: path to SmartScript file.");
			return;
		}
		String documentBody;
		try {
			documentBody = Files.readString(Path.of(args[0]));
		} catch (IOException e) {
			System.out.println("Unable to read the file.");
			return;
		}
		Map<String,String> parameters = new HashMap<String, String>();
		Map<String,String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		// create engine and execute it
		new SmartScriptEngine(
		new SmartScriptParser(documentBody).getDocumentNode(),
		new RequestContext(System.out, parameters, persistentParameters, cookies)
		).execute();



	}
}
