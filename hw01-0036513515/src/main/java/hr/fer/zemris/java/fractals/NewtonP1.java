package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Program za prikaz fraktala temeljenog na Newton-Raphson iteraciji.
 * Fraktal se crta paralelno, koristeći zadani broj dretvi i poslova.
 * 
 * @author mskrabic
 *
 */
public class NewtonP1{
	
	/**
	 * Broj dretvi za crtanje fraktala.
	 * Pretpostavljena vrijednost je broj dostupnih procesora.
	 */
	private static int numberOfWorkers = Runtime.getRuntime().availableProcessors();
	
	/**
	 * Broj traka na koje se dijeli prozor za crtanje fraktala.
	 * Pretpostavljena vrijednost je 4 * broj dostupnih procesora.
	 */
	private static int numberOfTracks = Runtime.getRuntime().availableProcessors() * 4;
	
	/**
	 * Glavna metoda za pokretanje programa.
	 * @param args argumenti programa, definirani u metodi <code>parseArgs</code>.
	 */
	public static void main(String[] args) {
		if (args.length > 0)
			parseArgs(args);
				
		Scanner sc = new Scanner(System.in);
		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
		System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");
		
		int idx = 1;
		String line;
		List<Complex> rootList = new ArrayList<>();
		while (true) {
			System.out.print("Root " + idx + ">");
			line = sc.nextLine();
			if (line.equals("done"))
				break;			
			rootList.add(Complex.parse(line));
			idx++;
		}
		sc.close();
		System.out.println("Image of the fractal will appear shortly. Thank you.");
		
		Complex[] roots = new Complex[rootList.size()];
		for (int i = 0; i < roots.length; i++) {
			roots[i] = rootList.get(i);
		}
		
		ComplexRootedPolynomial crp = new ComplexRootedPolynomial(Complex.ONE, roots);
		FractalViewer.show(new NewtonParallelProducer(crp));	
	}

	/**
	 * Metoda za parsiranje argumenata, ako oni postoje.
	 * Sintaksa argumenata je --workers=N ili -w N za postavljanje broja radnika (dretvi), odnosno
	 * --tracks=N ili -t N za postavljanje broja traka.
	 * 
	 * @param args argumenti programa.
	 */
	private static void parseArgs(String[] args) {
		boolean workersFlag = false, tracksFlag = false;
		
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("--workers=")) {
				if (workersFlag)
					throw new IllegalArgumentException("Number of workers already set!");
				numberOfWorkers = Integer.parseInt(args[i].substring(10));
				workersFlag = true;
			} else if (args[i].equals("-w")) {
				if (workersFlag)
					throw new IllegalArgumentException("Number of workers already set!");
				if (i+1 == args.length)
					throw new IllegalArgumentException("Missing argument: Number of workers not given!");
				numberOfWorkers = Integer.parseInt(args[++i]);
				workersFlag = true;
			} else if (args[i].startsWith("--tracks=")) {
				if (tracksFlag)
					throw new IllegalArgumentException("Number of tracks already set!");
				
				int value = Integer.parseInt(args[i].substring(9));
				if (value < 1)
					throw new IllegalArgumentException("Minimal number of tracks is 1!");
				numberOfTracks = value;
				tracksFlag = true;
			} else if (args[i].equals("-t")) {
				if (tracksFlag)
					throw new IllegalArgumentException("Number of workers already set!");
				if (i+1 == args.length) 
					throw new IllegalArgumentException("Missing argument: Number of workers not given!");
				
				int value = Integer.parseInt(args[++i]);
				if (value < 1)
					throw new IllegalArgumentException("Minimal number of tracks is 1!");
				numberOfTracks = value;
				tracksFlag = true;
			}
		}	
	}
	
	/**
	 * Razred predstavlja implementaciju sučelja {@link IFractalProducer} za paralelno izvođenje
	 * Newton-Raphson iteracije.
	 * 
	 * @author mskrabic
	 *
	 */
	public static class NewtonParallelProducer implements IFractalProducer {
		
		/**
		 * Polinom za koji se crta fraktal u obliku f(z)=z0*(z-z1)*...*(z-zn).
		 */
		private ComplexRootedPolynomial crp;
		
		/**
		 * Polinom za koji se crta fraktal u obliku f(z)=z0+z1*z+..+zn*z^n.
		 */
		private ComplexPolynomial cp;
		
		/**
		 * Bazen dretvi.
		 */
		private ExecutorService pool;
		
		/**
		 * Konstruktor koji inicijalizira {@link NewtonParallelProducer}-a za predani polinom.
		 * 
		 * @param crp {@link ComplexRootedPolynomial} za koji se želi nacrtati fraktal.
		 */
		public NewtonParallelProducer(ComplexRootedPolynomial crp) {
			this.crp = crp;
			this.cp = crp.toComplexPolynom();
		}
		
		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax,
				int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
			System.out.println("Zapocinjem izracun...");
			if (numberOfTracks > height) {
				numberOfTracks = height;
			}
			System.out.println("Broj radnika: " + numberOfWorkers + "\nBroj traka: " + numberOfTracks);
		
			short[] data = new short[width * height];
			int numberOfYPerTrack = height / numberOfTracks;
			
			List<Future<?>> results = new ArrayList<>();			
			
			for(int i = 0; i < numberOfTracks; i++) {
				int yMin = i*numberOfYPerTrack;
				int yMax = (i+1)*numberOfYPerTrack-1;
				if(i == numberOfTracks-1) {
					yMax = height-1;
				}
				NewtonTask task = new NewtonTask(reMin, reMax, imMin, imMax, width, height, yMin, yMax, data, cancel, crp, cp);
				results.add(pool.submit(task));
			}
			
			for(Future<?> f : results) {
				while(true) {
					try {
						f.get();
						break;
					} catch (InterruptedException e) {
					} catch (ExecutionException e) {
						throw new RuntimeException(e);
					}
				}
			}
			
			
			System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
			observer.acceptResult(data, (short)(cp.order()+1), requestNo);
		}

		@Override
		public void close() {
			pool.shutdown();
			
		}

		@Override
		public void setup() {
			pool = Executors.newFixedThreadPool(numberOfWorkers);	
		}
	}
	
	/**
	 * Posao izračuna Newton-Raphson iteracije za zadane granice.
	 * 
	 * @author mskrabic
	 *
	 */
	public static class NewtonTask implements Runnable {
		/**
		 * Minimalna vrijednost apcise.
		 */
		double reMin;
		/**
		 * Maksimalna vrijednost apcise.
		 */
		double reMax;
		/**
		 * Minimalna vrijednost ordinate.
		 */
		double imMin;
		/**
		 * Maksimalna vrijednost ordinate.
		 * 
		 */
		double imMax;
		
		/**
		 * Širina prozora.
		 */
		int width;
		
		/**
		 * Visina prozora.
		 */
		int height;
		
		/**
		 * Početni y za koji se računa Newton-Raphson iteracija.
		 */
		int yMin;
		
		/**
		 * Završni y za koji se računa Newton-Raphson iteracija.
		 */
		int yMax;
		
		/**
		 * Polje indeksa najbližih korijena polinoma za pojedinu točku na prozoru.
		 */
		short[] data;
		
		/**
		 * Zastavica zaustavljanja.
		 */
		AtomicBoolean cancel;
		
		/**
		 * Polinom za koji se crta fraktal u obliku f(z)=z0*(z-z1)*...*(z-zn).
		 */
		private ComplexRootedPolynomial crp;
		
		/**
		 * Polinom za koji se crta fraktal u obliku f(z)=z0+z1*z+...+zn*z^n.
		 */
		private ComplexPolynomial cp;
		
		/**
		 * Granica konvergencije.
		 */
		private static final double CONVERGENCE_TRESHOLD = 0.001;
		
		/**
		 * Granica udaljenosti od korijena polinoma.
		 */
		private static final double ROOT_TRESHOLD = 0.002;
		
		/**
		 * Konstruktor koji inicijalizira {@link NewtonTask} s predanim vrijednostima.
		 * 
		 * @param reMin minimalna vrijednost apcise.
		 * @param reMax maksimalna vrijednost apcise.
		 * @param imMin minimalna vrijednost ordinate.
		 * @param imMax maksimalna vrijednost ordinate.
		 * @param width širina prozora.
		 * @param height visina prozora.
		 * @param yMin početni y za koji se provodi posao.
		 * @param yMax završni y za koji se provodi posao.
		 * @param data polje indeksa najbližih korijena polinoma.
		 * @param cancel zastavica zaustavljanja
		 * @param crp polinom za koji se crta fraktal u obliku f(z)=z0*(z-z1)*...*(z-zn).
		 * @param cp polinom za koji se crta fraktal u obliku f(z)=z0+z1*z+...+zn*z^n.
		 */
		public NewtonTask(double reMin, double reMax, double imMin,
				double imMax, int width, int height, int yMin, int yMax,
				short[] data, AtomicBoolean cancel, ComplexRootedPolynomial crp, ComplexPolynomial cp) {
			super();
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.data = data;
			this.cancel = cancel;
			this.crp = crp;
			this.cp = cp;
		}
		
		@Override
		public void run() {
			int offset = yMin*width;
			
			for(int y = yMin; y <= yMax; y++) {
				if (cancel.get()) break;
				for (int x = 0; x < width; x++) {
					 Complex zn = mapToComplexPlain(x, y, width, height, reMin, reMax, imMin, imMax);
					 double module;
					 int iter = 0;
					 do {
						 Complex numerator = crp.apply(zn);
						 Complex denominator = cp.derive().apply(zn);
						 Complex fraction = numerator.divide(denominator);
						 Complex znold = zn;	 
						 zn = zn.sub(fraction);
						 module = zn.sub(znold).module();
					 } while(module > CONVERGENCE_TRESHOLD && iter < cp.order());
					 int index = crp.indexOfClosestRootFor(zn, ROOT_TRESHOLD);
					 data[offset++]= (short)(index+1);
				 }
			}			
		}
		
		/**
		 * Metoda računa kompleksni broj koji se nalazi na koordinatama (x,y) prozora za prikaz fraktala.
		 * 
		 * @param x x-koordinata točke na prozoru za prikaz fraktala. 
		 * @param y y-koordinata točke na prozoru za prikaz fraktala.
		 * @param width širina prozora.
		 * @param height visina prozora.
		 * @param reMin minimalna vrijednost apcise.
		 * @param reMax maksimalna vrijednost apcise.
		 * @param imMin minimalna vrijednost ordinate.
		 * @param imMax maksimalna vrijednost ordinate.
		 * 
		 * @return odgovarajući kompleksni broj.
		 */
		private Complex mapToComplexPlain(int x, int y, int width, int height, double reMin, double reMax,
				double imMin, double imMax) {
			
			double re = ((double)x/(width-1))*(reMax-reMin) + reMin;
			double im = ((double)(height-1-y)/(height-1))*(imMax-imMin) + imMin;
			
			return new Complex(re, im);
		}
	}
}
