package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;

/**
 * Razred predstavlja model kompleksnog broja. Svi objekti ovog razreda su neizmjenjivi (read-only).
 * 
 * @author mskrabic
 *
 */
public class Complex {
	/**
	 * Realni dio kompleksnog broja.
	 */
	private double real;
	
	/**
	 * Imaginarni dio kompleksnog broja.
	 */
	private double imaginary;
	
	/**
	 * Konstanta koja predstavlja kompleksni broj 0.
	 */
	public static final Complex ZERO = new Complex(0,0);
	
	/**
	 * Konstanta koja predstavlja kompleksni broj 1.
	 */
	public static final Complex ONE = new Complex(1,0);
	
	/**
	 * Konstanta koja predstavlja kompleksni broj -1.
	 */
	public static final Complex ONE_NEG = new Complex(-1,0);
	
	/**
	 * Konstanta koja predstavlja kompleksni broj i.
	 */
	public static final Complex IM = new Complex(0,1);
	
	/**
	 * Konstanta koja predstavlja kompleksni broj -i.
	 */
	public static final Complex IM_NEG = new Complex(0,-1);
	
	/**
	 * Pretpostavljeni konstruktor - postavlja realni i imaginarni dio na 0.
	 */
	public Complex() {
		this.real = 0;
		this.imaginary = 0;
	}
	
	/**
	 * Konstruktor koji inicijalizira kompleksni broj s predanim vrijednostima.
	 */
	public Complex(double re, double im) {
		real = re;
		imaginary = im;
	}
	
	/**
	 * Metoda vraća realni dio kompleksnog broja.
	 * 
	 * @return realni dio kompleksnog broja.
	 */
	public double getReal() {
		return real;
	}
	
	/**
	 * Metoda vraća imaginarni dio kompleksnog broja.
	 * 
	 * @return imaginarni dio kompleksnog broja.
	 */
	public double getImaginary() {
		return imaginary;
	}
	
	/**
	 * Metoda vraća modul kompleksnog broja.
	 * 
	 * @return modul kompleksnog broja.
	 */
	public double module() {
		return Math.sqrt(real * real + imaginary * imaginary);
	}
	
	/**
	 * Metoda vraća novi kompleksni broj koji je rezultat množenja trenutnog kompleksnog broja s predanim.
	 * 
	 * @param c kompleksni broj kojim se želi pomnožiti trenutni.
	 * 
	 * @return umnožak trenutnog i predanog kompleksnog broja.
	 */
	public Complex multiply(Complex c) {
		return new Complex(
				this.real * c.real - this.imaginary * c.imaginary,
				this.real * c.imaginary + c.real * this.imaginary);
	}
	
	/**
	 * Metoda vraća novi kompleksni broj koji je rezultat dijeljenja trenutnog kompleksnog broja s predanim.
	 * 
	 * @param c kompleksni broj kojim se želi podijeliti trenutni.
	 * 
	 * @throws IllegalArgumentException ako se preda 0.
	 * 
	 * @return umnožak trenutnog i predanog kompleksnog broja.
	 */
	public Complex divide(Complex c) {
		if (c.real == 0 && c.imaginary == 0)
			throw new IllegalArgumentException("Dividing with 0 is not allowed!");
		
		Complex conjugate = new Complex(c.real, -c.imaginary);
		Complex numerator = multiply(conjugate);
		double denominator = c.module() * c.module();
		
		return new Complex(numerator.real / denominator, numerator.imaginary / denominator);
	}
	
	/**
	 * Metoda vraća novi kompleksni broj koji je rezultat zbrajanja trenutnog kompleksnog broja s predanim.
	 * 
	 * @param c kompleksni broj koji se želi pribrojiti trenutnom.
	 * 
	 * @return zbroj trenutnog i predanog kompleksnog broja.
	 */
	public Complex add(Complex c) {
		return new Complex(this.real + c.real, this.imaginary + c.imaginary);
	}
	
	/**
	 * Metoda vraća novi kompleksni broj koji je rezultat oduzimanja predanog kompleksnog broja od trenutnog. 
	 * 
	 * @param c kompleksni broj koji se želi oduzeti trenutnom.
	 * 
	 * @return razlika trenutnog i predanog kompleksnog broja.
	 */
	public Complex sub(Complex c) {
		return new Complex(this.real - c.real, this.imaginary - c.imaginary);
	}
	
	/**
	 * Metoda vraća novi kompleksni broj koji je jednak negiranom trenutnom.
	 * 
	 * @return negirani trenutni kompleksni broj.
	 */
	public Complex negate() {
		return new Complex(-this.real , -this.imaginary);
	}
	
	/**
	 * Metoda vraća rezultat potenciranja trenutnog kompleksnog broja.
	 * 
	 * @param n željena potencija.
	 * 
	 * @throws IllegalArgumentException ako se preda broj manji od 0.
	 * 
	 * @return novi kompleksni broj koji je n-ta potencija trenutnog broja.
	 */
	public Complex power(int n) {
		if (n < 0) 
			throw new IllegalArgumentException("Power should be >=0, and it was " + n + ".");
		
		double newMagnitude = Math.pow(module(), n);
		double newAngle = n * getAngle();		
		
		return Complex.fromMagnitudeAndAngle(newMagnitude, newAngle);
		
	}
	/**
	 * Metoda vraća listu koja sadrži n-te korijene trenutnog kompleksnog broja.
	 * 
	 * @param n korijen koji se želi izračunati.
	 * 
	 * @throws IllegalArgumentException ako se preda broj manji ili jednak 0.
	 * 
	 * @return lista n-tih korijena trenutnog kompleksnog broja.
	 */
	public List<Complex> root(int n) {
		if (n <= 0)
			throw new IllegalArgumentException("The argument should be >0, and it was " + n + ".");
		
		double newMagnitude = Math.pow(module(), 1.0/n);
		List<Complex> roots= new ArrayList<>();
		for (int i = 0; i < n; i++) {
			roots.add(Complex.fromMagnitudeAndAngle(newMagnitude, (getAngle()+2*i*Math.PI)/n));
		}
		
		return roots;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(this.real);
		sb.append(this.imaginary >= 0 ? "+" : "-");
		sb.append("i" + Math.abs(this.imaginary));
		
		return sb.toString();
	}
	
	/**
	 * Metoda za parsiranje kompleksnog broja iz predanog stringa.
	 * 
	 * @param line kompleksni broj zapisan u obliku stringa.
	 * 
	 * @return novi kompleksni broj koji odgovara predanom zapisu.
	 */
	public static Complex parse(String line) {	
		line = line.replaceAll(" ", "");
		
		if (line.contains("i")) {
			if (line.equals("i") || line.equals("+i"))
				return new Complex(0, 1);
			if (line.equals("-i"))
				return new Complex(0, -1);
			
			StringBuilder sb = new StringBuilder();
			int index = 0;
			if (line.charAt(index) == '+' || line.charAt(index) == '-') {
				sb.append(line.charAt(index++));
			}
			while (index < line.length() && Character.isDigit(line.charAt(index))) {
				sb.append(line.charAt(index++));
			}
			if (line.charAt(index) == '.') {
				sb.append(line.charAt(index++));
				while (index < line.length() && Character.isDigit(line.charAt(index))) {
					sb.append(line.charAt(index++));
				}
			}
			
			if (line.charAt(index) == 'i') {
				if (index == line.length()-1)
					return new Complex(0, Double.parseDouble(sb.toString()));
				if (sb.length() == 0) {
					line = "0+"+line;
					return Complex.parse(line);	
				}				
				throw new NumberFormatException("Given string " + line + " is not a valid complex number!");
			}
			
			//dosli do + ili -
			double real = Double.parseDouble(sb.toString());
			sb.delete(0, index);
			if (line.charAt(index) == '+' || line.charAt(index) == '-') {
				sb.append(line.charAt(index++));
				
				if (line.charAt(index) == 'i') {
					index++;
				} else {
					throw new NumberFormatException("Given string " + line + "is not a valid complex number!");
				}
				if (index == line.length())
					return new Complex(real, sb.toString().equals("+") ? 1 : -1);
				while (index < line.length() && Character.isDigit(line.charAt(index))) {
					sb.append(line.charAt(index++));
				}
				if (index < line.length() && line.charAt(index) == '.') {
					sb.append(line.charAt(index++));
					while (index < line.length() && Character.isDigit(line.charAt(index))) {
						sb.append(line.charAt(index++));
					}
				}
				if (index == line.length())
					return new Complex(real, Double.parseDouble(sb.toString()));
				throw new NumberFormatException("Given string " + line + "is not a valid complex number!");
			}
			throw new NumberFormatException("Given string " + line + " is not a valid complex number!");
			
		}
		try {
			return new Complex(Double.parseDouble(line), 0);
		} catch (NumberFormatException e) {
			throw new NumberFormatException("Given string " + line + " is not a valid complex number!");
		}
	}
	
	/**
	 * Pomoćna metoda koja računa kut kompleksnog broja.
	 * 
	 * @return kut kompleksnog broja.
	 */
	private double getAngle() {
		double angle = Math.atan2(imaginary, real);
		return (angle < 0) ? angle+2*Math.PI : angle;
	}
	
	/**
	 * Pomoćna metoda koja stvara kompleksni broj iz predanih polarnih koordinata.
	 * 
	 * @param magnitude modul kompleksnog broja.
	 * @param angle kut kompleksnog broja.
	 * 
	 * @return novi kompleksni broj sa zadanim koordinatama.
	 */
	private static Complex fromMagnitudeAndAngle(double magnitude, double angle) {	
		double re = magnitude*Math.cos(angle);
		double im = magnitude*Math.sin(angle);
		return new Complex(Math.abs(re) < 1E-10 ? 0 : re, Math.abs(im) < 1E-10 ? 0 : im);
	}

}
