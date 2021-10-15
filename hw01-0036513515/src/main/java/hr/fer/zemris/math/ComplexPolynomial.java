package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;

/**
 * Razred predstavlja kompleksni polinom oblika f(z)=z0+z1*z+...+zn*z^n.
 * 
 * @author mskrabic
 *
 */
public class ComplexPolynomial {
	/**
	 * Koeficijenti polinoma.
	 */
	private List<Complex> factors;
	
	/**
	 * Konstruktor koji inicijalizira polinom s predanim koeficijentima. (redoslijed je z0, z1...)
	 * 
	 * @param factors koeficijenti polinoma.
	 */
	public ComplexPolynomial(Complex ...factors) {
		this.factors = new ArrayList<>();
		
		for (Complex factor : factors)
			this.factors.add(factor);	
	}
	
	/**
	 * Metoda računa stupanj polinoma.
	 * 
	 * @return stupanj polinoma.
	 */
	public short order() {
		int index = factors.size()-1;
		
		while (index >= 0) {
			if (factors.get(index).getReal() != 0 || factors.get(index).getImaginary() != 0)
				return (short)index;
			index--;
		}
		
		return 0;
	}
	
	/**
	 * Metoda vraća novi polinom koji je rezultat množenja trenutnog i predanog polinoma.
	 * 
	 * @param p polinom kojim se želi pomnožiti trenutni.
	 * 
	 * @return umnožak trenutnog i predanog polinoma.
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		Complex[] newFactors = new Complex[factors.size()+p.factors.size()-1];
		
		for (int i = 0; i < newFactors.length; i++) {
			newFactors[i] = Complex.ZERO;
		}
		
		for (int i = 0; i < factors.size(); i++) {
			for (int j = 0; j < p.factors.size(); j++) {
				newFactors[i+j] = newFactors[i+j].add(factors.get(i).multiply(p.factors.get(j)));
			}
		}
		
		return new ComplexPolynomial(newFactors);
	}
	
	/**
	 * Metoda računa prvu derivaciju polinoma.
	 * 
	 * @return novi polinom koji je jednak prvoj derivaciji trenutnog polinoma.
	 */
	public ComplexPolynomial derive() {
		Complex[] newFactors = new Complex[factors.size()-1];
		
		for (int i = 0; i < newFactors.length; i++) {
			newFactors[i] = factors.get(i+1).multiply(new Complex(i+1, 0));
		}
		
		return new ComplexPolynomial(newFactors);
	}
	
	/**
	 * Metoda koja računa vrijednost polinoma za predani kompleksni broj.
	 * 
	 * @param z kompleksni broj za koji se želi izračunati vrijednost polinoma.
	 * 
	 * @return novi kompleksni broj koji predstavlja vrijednost polinoma za predani kompleksni broj.
	 */
	public Complex apply(Complex z) {
		Complex result = Complex.ZERO;
		
		for (int i = 0; i < factors.size(); i++) {
			result = result.add(z.power(i).multiply(factors.get(i)));
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (int i = factors.size()-1; i > 0; i--) {
			sb.append("(" + factors.get(i) + ")*z^" + i + "+");
		}
		sb.append("(" + factors.get(0) + ")");
		
		return sb.toString();
	}

}
