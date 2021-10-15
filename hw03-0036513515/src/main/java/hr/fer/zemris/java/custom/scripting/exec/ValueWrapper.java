package hr.fer.zemris.java.custom.scripting.exec;

import java.util.function.BiFunction;

/**
 * Razred modelira omotač za jednu općenitu vrijednost.
 * 
 * @author mskrabic
 *
 */
public class ValueWrapper {

	/**
	 * Vrijednost omotača.
	 */
	private Object value;
	
	/**
	 * Konstruktor.
	 * 
	 * @param value	vrijednost koja se želi postaviti.
	 */
	public ValueWrapper(Object value) {
		this.value = value;
	}
	
	/**
	 * Postavlja vrijednost omotača.
	 * 
	 * @param value	vrijednost koja se želi postaviti.
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
	/**
	 * Vraća vrijednost omotača.
	 * 
	 * @return	vrijednost omotača.
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Uvećava vrijednost omotača za predanu vrijednost.
	 * 
	 * @param incValue	vrijednost za koju se želi uvećati pohranjena vrijednost.
	 */
	public void add(Object incValue) {
		checkValue(value);
		checkValue(incValue);
		operate(incValue, (d1, d2) -> d1 + d2);
	}

	/**
	 * Smanjuje vrijednost omotača za predanu vrijednost.
	 * 
	 * @param decValue	vrijednost za koju se želi smanjiti pohranjena vrijednost.
	 */
	public void subtract(Object decValue) {
		checkValue(value);
		checkValue(decValue);
		
		operate(decValue, (d1, d2) -> d1 - d2);
		
	}
	
	/**
	 * Množi vrijednost omotača predanom vrijednosti.
	 * 
	 * @param mulValue	vrijednost s kojom se želi pomnožiti pohranjena vrijednost.
	 */
	public void multiply(Object mulValue) {
		checkValue(value);
		checkValue(mulValue);
		
		operate(mulValue, (d1, d2) -> d1 * d2);
	}
	
	/**
	 * Dijeli vrijednost omotača predanom vrijednosti.
	 * 
	 * @param divValue	vrijednost kojom se želi podijeliti pohranjena vrijednost.
	 */
	public void divide(Object divValue) {
		checkValue(value);
		checkValue(divValue);
		
		operate(divValue, (d1, d2) -> d1 / d2);
	}
	
	/**
	 * Uspoređuje pohranjenu vrijednost s predanom vrijednosti.
	 * 
	 * @param withValue	vrijednost s kojom se uspoređuje.
	 * 
	 * @return	<code>0</code> ako su vrijednosti jednake, broj manji od <code>0</code> ako je pohranjena vrijednost manja od predane,
	 * 			broj veći od <code>0</code> ako je pohranjena vrijednost veća od predane.		  
	 */
	public int numCompare(Object withValue) {
		checkValue(value);
		checkValue(withValue);
		
		if (value == null && withValue == null)
			return 0;
		
		value = prepareValue(value);
		withValue = prepareValue(value);
		
		return Double.compare(((Double)value).doubleValue(), ((Double)withValue).doubleValue());
		
	}
	
	/**
	 * Provjerava jesu li definirane operacije nad vrijednosti.
	 * 
	 * @param value				vrijednost koja se provjerava.
	 * 
	 * @throws RuntimeException	ako vrijednost nije <code>null</code>, Integer, Double ili String.
	 */
	private void checkValue(Object value) {
		if (!(value == null 
				|| value instanceof Integer 
				|| value instanceof Double 
				|| value instanceof String)) {
			throw new RuntimeException("Invalid value type! Operations are allowed with null, Integer, Double and String values.");
		}		
	}
	
	/**
	 * Priprema vrijednost za izvođenje operacije. Ako je <code>null</code>, pretvara ju u cijeli broj 0, ako je String parsira ga
	 * u broj, inače pretpostavlja da je broj (Integer ili Double) i vraća ju nepromjenjenu.
	 * 
	 * @param o	vrijednost.
	 * 
	 * @return	pripremljena vrijednost.
	 */
	private Object prepareValue(Object o) {
		if (o == null)
			return Integer.valueOf(0);
		if (o instanceof String) {
			String s = (String) o;
			try {
				if (s.contains(".") || s.contains("E"))
					return Double.parseDouble(s);
				return Integer.parseInt(s);
			} catch (NumberFormatException e) {
				throw new RuntimeException("Invalid value: " + s + " can't be parsed into a number!");
			}
		}
		return o;	
	}
	
	/**
	 * Provodi operaciju nad pohranjenom i predanom vrijednosti.
	 * 
	 * @param operand	predana vrijednost, operand.
	 * 
	 * @param operation	operacija koja se izvodi.
	 */
	private void operate(Object operand, BiFunction<Double, Double, Double> operation) {
		value = prepareValue(value);
		operand = prepareValue(operand);
		
		double d1 = value instanceof Double ? ((Double)value).doubleValue() : ((Integer)value).intValue();
		double d2 = operand instanceof Double ? ((Double)operand).doubleValue() : ((Integer)operand).intValue();
		Double result = operation.apply(d1, d2);
		
		
		if (value instanceof Double || operand instanceof Double) {
			this.value = result;
		} else {
			this.value = Integer.valueOf((int)result.doubleValue());
		}	
	}
}
