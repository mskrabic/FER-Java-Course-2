package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.java.custom.scripting.elems.*;
import hr.fer.zemris.java.custom.scripting.exec.functions.DecimalFormatFunction;
import hr.fer.zemris.java.custom.scripting.exec.functions.DuplicateFunction;
import hr.fer.zemris.java.custom.scripting.exec.functions.Function;
import hr.fer.zemris.java.custom.scripting.exec.functions.ParameterGetFunction;
import hr.fer.zemris.java.custom.scripting.exec.functions.PersistentParameterDeleteFunction;
import hr.fer.zemris.java.custom.scripting.exec.functions.PersistentParameterGetFunction;
import hr.fer.zemris.java.custom.scripting.exec.functions.PersistentParameterSetFunction;
import hr.fer.zemris.java.custom.scripting.exec.functions.SetMimeTypeFunction;
import hr.fer.zemris.java.custom.scripting.exec.functions.SinFunction;
import hr.fer.zemris.java.custom.scripting.exec.functions.SwapFunction;
import hr.fer.zemris.java.custom.scripting.exec.functions.TemporaryParameterDeleteFunction;
import hr.fer.zemris.java.custom.scripting.exec.functions.TemporaryParameterGetFunction;
import hr.fer.zemris.java.custom.scripting.exec.functions.TemporaryParameterSetFunction;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Razred pokreće predanu SmartScript skriptu.
 * 
 * @author mskrabic
 *
 */
public class SmartScriptEngine {
	
	/**
	 * Glavni čvor skripte.
	 */
	private DocumentNode documentNode;
	
	/**
	 * Kontekst zahtjeva.
	 */
	private RequestContext requestContext;
	
	/**
	 * Višestruki stog koji se koristi pri provedbi operacija.
	 */
	private ObjectMultistack multistack = new ObjectMultistack();
	
	private static final HashMap<String, Function> FUNCTIONS = new HashMap<>();
	
	/**
	 * Posjetitelj koji obrađuje čvorove skripte.
	 */
	private INodeVisitor visitor = new INodeVisitor() {

		/**
		 * Ispisuje sadržaj tekstualnog čvora na izlazni tok konteksta.
		 */
		@Override
		public void visitTextNode(TextNode node) {
			try {
				requestContext.write(node.getText());
			} catch (IOException e) {
				System.out.println("Unable to write text from text node.");
			}
			
		}

		/**
		 * Za svaku iteraciju definiranu čvorom {@link ForLoopNode} posjećuje svako dijete tog čvora.
		 */
		@Override
		public void visitForLoopNode(ForLoopNode node) {
			String variable = node.getVariable().asText();
			int step = Integer.parseInt(node.getStepExpression().asText());
			int end = Integer.parseInt(node.getEndExpression().asText());
			multistack.push(variable, new ValueWrapper(node.getStartExpression().asText()));
			while (Integer.parseInt(multistack.peek(variable).getValue().toString()) <= end) {
				for (int i = 0; i < node.numberOfChildren(); i++) {
					node.getChild(i).accept(this);
				}
				ValueWrapper v = multistack.pop(variable);
				v.setValue(Integer.parseInt(v.getValue().toString()) + step);
				multistack.push(variable, v);
			}
			
		}

		/**
		 * Provodi operaciju definiranu čvorom {@link EchoNode}
		 */
		@Override
		public void visitEchoNode(EchoNode node) {
			Stack<Object> tempStack = new Stack<>();
			Element[] elements = node.getElements();
			for (Element e : elements) {
				if (e instanceof ElementString || e instanceof ElementConstantInteger || e instanceof ElementConstantDouble)
					pushConstant(e, tempStack);
				if (e instanceof ElementVariable) {
					tempStack.push(multistack.peek(e.asText()).getValue());
				}
				if (e instanceof ElementOperator) {
					tempStack.push(doOperation(tempStack.pop(), tempStack.pop(), e.asText()));

				}
				if (e instanceof ElementFunction) {
					FUNCTIONS.get(e.asText().substring(1)).execute(tempStack, requestContext);
				}
			}
			List<Object> left = new ArrayList<>();
			while (!tempStack.isEmpty())
				left.add(tempStack.pop());
			Collections.reverse(left);
			left.forEach(s -> {
				try {
					requestContext.write(s.toString());
				} catch (IOException e1) {
					System.out.println("Error while writing EchoNode stack contents.");
				}
			});
		}

		private Object doOperation(Object v1, Object v2, String operation) {
			boolean asDoubles = false;
			try {
				v1 = Integer.parseInt(v1.toString());
			} catch (NumberFormatException e) {
				asDoubles = true;
			}
			
			try {
				v2 = Integer.parseInt(v2.toString());
			} catch (NumberFormatException e) {
				asDoubles = true;
			}
			
			switch (operation) {
			case "+":
				if (asDoubles)
					return Double.valueOf(v1.toString()) +  Double.valueOf(v2.toString());
				return Integer.valueOf(v1.toString()) + Integer.valueOf(v2.toString());
			case "-":
				if (asDoubles)
					return Double.valueOf(v1.toString()) -  Double.valueOf(v2.toString());
				return Integer.valueOf(v1.toString()) - Integer.valueOf(v2.toString());
			case "*":
				if (asDoubles)
					return Double.valueOf(v1.toString()) *  Double.valueOf(v2.toString());
				return Integer.valueOf(v1.toString()) * Integer.valueOf(v2.toString());
			case "/":
				if (asDoubles)
					return Double.valueOf(v1.toString()) /  Double.valueOf(v2.toString());
				return Integer.valueOf(v1.toString()) / Integer.valueOf(v2.toString());
			default:
				throw new IllegalArgumentException("Invalid operation: " + operation + ". Supported operations are +, -, * and /.");
			}
			
		}

		/**
		 * Stavlja konstantu na vrh stoga.
		 * 
		 * @param e		konstanta.
		 * @param stack	stog.
		 */
		private void pushConstant(Element e, Stack<Object> stack) {
			if (e instanceof ElementString) {
				stack.push(e.asText());
				return;
			}
			if (e instanceof ElementConstantInteger) {
				stack.push(((ElementConstantInteger)e).getValue());
				return;
			}
			stack.push(((ElementConstantDouble)e).getValue());	
		}

		/**
		 * Obrađuje glavni čvor.
		 */
		@Override
		public void visitDocumentNode(DocumentNode node) {
			for (int i = 0; i < node.numberOfChildren(); i++)
				node.getChild(i).accept(this);
			
		}
	};

	/**
	 * Konstruktor.
	 * 
	 * @param documentNode		glavni čvor.
	 * @param requestContext	kontekst.
	 */
	public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
		this.documentNode = documentNode;
		this.requestContext = requestContext;
		initializeFunctions();
	}

	private void initializeFunctions() {
		FUNCTIONS.put("sin", new SinFunction());
		FUNCTIONS.put("decfmt", new DecimalFormatFunction());
		FUNCTIONS.put("dup", new DuplicateFunction());
		FUNCTIONS.put("swap", new SwapFunction());
		FUNCTIONS.put("setMimeType", new SetMimeTypeFunction());
		FUNCTIONS.put("paramGet", new ParameterGetFunction());
		FUNCTIONS.put("pparamGet", new PersistentParameterGetFunction());
		FUNCTIONS.put("tparamGet", new TemporaryParameterGetFunction());
		FUNCTIONS.put("pparamSet", new PersistentParameterSetFunction());
		FUNCTIONS.put("tparamSet", new TemporaryParameterSetFunction());
		FUNCTIONS.put("pparamDelete", new PersistentParameterDeleteFunction());
		FUNCTIONS.put("tparamDelete", new TemporaryParameterDeleteFunction());
		
	}

	/**
	 * Pokreće obradu skripte.
	 */
	public void execute() {
		documentNode.accept(visitor);
	}
}