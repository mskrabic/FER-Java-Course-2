package hr.fer.zemris.math;

/**
 * Razred predstavlja kompleksni polinom oblika f(z)=z0*(z-z1)*...*(z-zn).
 * 
 * @author Swift3 mskrabic
 *
 */
public class ComplexRootedPolynomial {
	
	/**
	 * Konstanta polinoma.
	 */
	private Complex z0;
	
	/**
	 * Korijeni polinoma.
	 */
	private Complex[] roots;
	
	/**
	 * Konstruktor koji inicijalizira polinom s predanim vrijednostima.
	 * 
	 * @param z0 konstanta polinoma.
	 * @param roots korijeni polinoma (redoslijed je z1,z2...)
	 */
	public ComplexRootedPolynomial(Complex z0, Complex ... roots) {
		this.z0 = z0;
		this.roots = roots;
	}
	
	/**
	 * Metoda koja računa vrijednost polinoma za predani kompleksni broj.
	 * 
	 * @param z kompleksni broj za koji se želi izračunati vrijednost polinoma.
	 * 
	 * @return novi kompleksni broj koji predstavlja vrijednost polinoma za predani kompleksni broj.
	 */
	public Complex apply(Complex z) {
		Complex result = Complex.ONE;
		
		result = result.multiply(z0);
		for (int i = 0; i < roots.length; i++) {
			result = result.multiply(z.sub(roots[i]));
		}
		
		return result;	
	}
	
	/**
	 * Metoda koja pretvara trenutni polinom u oblik f(z)=z0+z1*z+...+zn*z^n.
	 * 
	 * @return {@link ComplexPolynomial} koji je jednak trenutnom polinomu.
	 */
	public ComplexPolynomial toComplexPolynom() {
		ComplexPolynomial result = new ComplexPolynomial(z0);
		
		for (Complex root: roots) {
			ComplexPolynomial cp = new ComplexPolynomial(root.negate(), Complex.ONE);
			result = result.multiply(cp);
		}
		
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(z0.toString() + "*");
		for (Complex root : roots) {
			sb.append("(z-(" + root.toString() + "))*");
		}
		
		return sb.substring(0, sb.length()-1).toString();	
	}
	/**
	 * Metoda pronalazi indeks korijena polinoma koji je najbliže predanom kompleksnom broju
	 * unutar predane granice. Prvi korijen ima indeks 0.
	 * 
	 * @param z kompleksni broj za koji se traži najbliži korijen.
	 * @param treshold granica udaljenosti.
	 * 
	 * @return indeks najbližeg korijena, ako takav postoji, inače -1.
	 */
	public int indexOfClosestRootFor(Complex z, double treshold) {
		int minIndex = -1;
		double minDistance = 0;
		
		double distance;
		for (int i = 0; i < roots.length; i++) {
			distance = z.sub(roots[i]).module();
			if (distance < treshold && (minIndex == -1 || distance < minDistance)) {
				minDistance = distance;
				minIndex = i;
			}
		}
		
		return minIndex;
	}
}