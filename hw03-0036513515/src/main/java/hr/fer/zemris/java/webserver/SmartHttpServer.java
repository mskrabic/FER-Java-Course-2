package hr.fer.zemris.java.webserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Razred modelira HTTP poslužitelj.
 * 
 * @author mskrabic
 *
 */
public class SmartHttpServer {
	/**
	 * Adresa poslužitelja.
	 */
	private String address;
	
	/**
	 * Ime domene.
	 */
	private String domainName;
	
	/**
	 * Port na kojem poslužitelj sluša.
	 */
	private int port;
	
	/**
	 * Broj dretvi radnika.
	 */
	private int workerThreads;
	
	/**
	 * Duljina sjednice.
	 */
	private int sessionTimeout;
	
	/**
	 * MIME-tipovi.
	 */
	private Map<String, String> mimeTypes = new HashMap<String, String>();
	
	/**
	 * Glavna dretva poslužitelja.
	 */
	private ServerThread serverThread;
	
	/**
	 * Bazen dretvi za obradu klijenata.
	 */
	private ExecutorService threadPool;
	
	/**
	 * Put do korjenskog direktorija za resurse.
	 */
	private Path documentRoot;
	
	/**
	 * Zastavica za zaustavljanje.
	 */
	private final AtomicBoolean stop = new AtomicBoolean(false);
	
	/**
	 * Radnici za obradu podataka.
	 */
	private Map<String,IWebWorker> workersMap;
	
	/**
	 * Aktivne sjednice.
	 */
	private Map<String, SessionMapEntry> sessions = new HashMap<String, SmartHttpServer.SessionMapEntry>();
	
	/**
	 * Generator slučajnih brojeva.
	 */
	private Random sessionRandom = new Random();

	
	/**
	 * Konstruktor.
	 * 
	 * @param configFileName put do konfiguracijske datoteke.
	 */
	public SmartHttpServer(String configFileName) {
		Properties serverProperties = new Properties();
		try {
			serverProperties.load(Files.newInputStream(Path.of(configFileName).toAbsolutePath()));
		} catch (IOException e) {
			System.out.println("Error while loading server properties.");
		}
		this.address = serverProperties.getProperty("server.address");
		this.domainName = serverProperties.getProperty("server.domainName");
		this.port = Integer.parseInt(serverProperties.getProperty("server.port"));
		this.workerThreads = Integer.parseInt(serverProperties.getProperty("server.workerThreads"));
		this.sessionTimeout = Integer.parseInt(serverProperties.getProperty("session.timeout"));
		this.documentRoot = Path.of(serverProperties.getProperty("server.documentRoot")).toAbsolutePath();
		this.serverThread = new ServerThread();
		Properties mimeProperties = new Properties();
		try {
			mimeProperties.load(Files.newInputStream(Path.of(serverProperties.getProperty("server.mimeConfig"))));
		} catch (IOException e) {
			System.out.println("Error while loading mime properties.");
		}
		for (var mimeType : mimeProperties.entrySet()) {
			mimeTypes.put((String) mimeType.getKey(), (String) mimeType.getValue());
		}
		
		workersMap = new HashMap<>();
		try {
			List<String> workers = Files.readAllLines(Path.of(serverProperties.getProperty("server.workers")));
			for (String line : workers) {
				String[] pair = line.split("=");
				String path = pair[0].strip().substring(1);
				String fqcn = pair[1].strip();
	
				Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
				Object newObject;
				newObject = referenceToClass.getDeclaredConstructor().newInstance();
				IWebWorker iww = (IWebWorker)newObject;
				
				workersMap.put(path, iww);
			}
		} catch (IOException e) {
			System.out.println("Error while loading worker properties.");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			System.out.println("Error while creating workers!");
		}

	}

	/**
	 * Pokreće poslužitelj.
	 */
	protected synchronized void start() {
		if (!serverThread.isAlive()) 
			serverThread.start();		
		threadPool = Executors.newFixedThreadPool(workerThreads);
		
		Thread sessionCleanupThread = new Thread(() -> {
			while (true) {
				for (SessionMapEntry session : sessions.values()) {
					if (session.validUntil < System.currentTimeMillis()/1000)
						sessions.remove(session.sid);
				}
				try {
					Thread.sleep(1000 * 60 * 5);
				} catch (InterruptedException ex) {}
			}
		});
		
		sessionCleanupThread.setDaemon(true);
		sessionCleanupThread.start();
	}

	/**
	 * Zaustavlja poslužitelj.
	 */
	protected synchronized void stop() {
		stop.set(true);
		threadPool.shutdown();
	}

	/**
	 * Razred modelira glavnu dretvu poslužitelja.
	 * 
	 * @author mskrabic
	 *
	 */
	protected class ServerThread extends Thread {
		@Override
		public void run() {
		
			ServerSocket serverSocket;
			try {
				serverSocket = new ServerSocket();
				serverSocket.bind(new InetSocketAddress(InetAddress.getByName(address), port));
			} catch (IOException e) {
				System.out.println("Error while opening the socket.");
				return;
			}
			while (!stop.get()) {
				Socket client;
				try {
					client = serverSocket.accept();
				} catch (IOException e) {
					System.out.println("Error while accepting client.");
					return;
				}
				ClientWorker cw = new ClientWorker(client);

				threadPool.submit(cw);
			}
			try {
				serverSocket.close();
			} catch (IOException e) {
				System.out.println("Error while closing the socket.");
			}
		}
	}
	
	/**
	 * Razred modelira stavku u mapi sjednica.
	 * 
	 * @author mskrabic
	 *
	 */
	private static class SessionMapEntry {
		
		/**
		 * Identifikator sjednice.
		 */
		String sid;
		
		/**
		 * Domena sjednice.
		 */
		String host;
		
		/**
		 * Vrijeme isteka sjednice.
		 */
		long validUntil;
		
		/**
		 * Parametri sjednice.
		 */
		Map<String,String> map;
		
		/**
		 * Konstruktor.
		 * @param sid			identifikator sjednice.
		 * @param host			domena sjednice.
		 * @param validUntil	vrijeme isteka sjednice.
		 */
		public SessionMapEntry(String sid, String host, long validUntil) {
			this.sid = sid;
			this.host = host;
			this.validUntil = validUntil;
			this.map = new ConcurrentHashMap<String, String>();
		}
	}

	/**
	 * Razred modelira radnika za obradu korisničkih zahtjeva.
	 * 
	 * @author mskrabic
	 *
	 */
	private class ClientWorker implements Runnable, IDispatcher {
		/**
		 * Putanja do konfiguracijske datoteke radnika.
		 */
		private static final String PATH_TO_WORKERS = "hr.fer.zemris.java.webserver.workers.";
		
		/**
		 * Priljučna točka klijenta.
		 */
		private Socket csocket;
		
		/**
		 * Ulazni tok podataka.
		 */
		private InputStream istream;
		
		/**
		 * Izlazni tok podataka.
		 */
		private OutputStream ostream;
		
		/**
		 * Verzija HTTP protokola.
		 */
		private String version;
		
		/**
		 * Metoda zahtjeva.
		 */
		private String method;
		
		/**
		 * Domena zahtjeva.
		 */
		private String host;
		
		/**
		 * Parametri.
		 */
		private Map<String, String> params = new HashMap<String, String>();
		
		/**
		 * Privremeni parametri.
		 */
		private Map<String, String> tempParams = new HashMap<String, String>();
		
		/**
		 * Trajni parametri.
		 */
		private Map<String, String> permParams = new HashMap<String, String>();
		
		/**
		 * Kolačići.
		 */
		private List<RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
		
		/**
		 * Identifikator sjednice.
		 */
		private String SID;
		
		/**
		 * Kontekst zahtjeva.
		 */
		private RequestContext context  = null;

		/**
		 * Konstruktor.
		 * 
		 * @param csocket	priključna točka.
		 */
		public ClientWorker(Socket csocket) {
			super();
			this.csocket = csocket;
		}

		@Override
		public void run() {
			try {
				istream = new BufferedInputStream(csocket.getInputStream());
				ostream = new BufferedOutputStream(csocket.getOutputStream());
			} catch (IOException e) {
				System.out.println("Error while opening client streams.");
				closeSocket();
				return;
			}
			
			byte[] requestData;
			try {
				requestData = readRequest(istream).get();
			} catch (NoSuchElementException e) {
				System.out.println("No data received.");
				sendEmptyResponse(400, "Bad request");
				closeSocket();
				return;
			}
			String requestString = new String(requestData, StandardCharsets.US_ASCII);
			List<String> request = Stream.of(requestString.split("\n")).collect(Collectors.toList());
			if (request.size() < 1) {
				sendEmptyResponse(400, "Bad request");
				closeSocket();
				return;
			}
			
			String firstLine = request.get(0);
			String[] parts = firstLine.split(" ");
			if (parts.length != 3) {
				sendEmptyResponse(400, "Bad request");
				closeSocket();
				return;
			}
			
			method = parts[0];
			String requestedPath = parts[1];
			version = parts[2];
			if (!method.equalsIgnoreCase("GET")
					|| !(version.equalsIgnoreCase("HTTP/1.0") || version.equalsIgnoreCase("HTTP/1.1"))) {
				sendEmptyResponse(400, "Bad request");
				closeSocket();
				return;
			}
			
			for (String header : request) {
				if (header.startsWith("Host: ")) {
					host = header.split(" ")[1].strip();
					int idx = host.lastIndexOf(":");
					host = host.substring(0, idx == -1 ? host.length() : idx);
					break;
				}
			}
			if (host == null)
				host = domainName;
			
			checkSession(request);
			permParams = sessions.get(SID).map;
			
			String path;
			String paramString = null;
			parts = requestedPath.split("[?]");
			path = parts[0];
			if (parts.length == 2)
				paramString = parts[1];
			parseParameters(paramString);
			
			try {
				internalDispatchRequest(path.substring(1), true);
			} catch (Exception e) {
				System.out.println("Dispatch error.");
				closeSocket();
				return;
			}
			
			closeSocket();
		}

		/**
		 * Ispisuje sadržaj izlaznog buffera (flush) i zatvara priključnu točku.
		 */
		private void closeSocket() {
			try {
				if (ostream != null)
					ostream.flush();
				csocket.close();
			} catch (IOException e) {
				System.out.println("Error while closing the client socket.");
			}
		}

		/**
		 * Provjerava postoji li sjednica za ovog klijenta. Ako ne postoji, stvara novu sjednicu.
		 * 
		 * @param request	zaglavlja zahtjeva.
		 */
		private synchronized void checkSession(List<String> request) {
			String sidCandidate = null;
			for (String line : request) {
				if (!line.startsWith("Cookie:")) continue;
				String[] cookies = line.split(" ")[1].split(";");
				for (String cookie : cookies) {
					if (cookie.startsWith("sid"))
						sidCandidate = cookie.split("=")[1].replaceAll("\"", "");
				}
			}
			if (sidCandidate != null) {
				SessionMapEntry session = sessions.get(sidCandidate);
				if (session != null) {
					if (session.host.equals(host)) {
						if (session.validUntil > System.currentTimeMillis() / 1000) {
							session.validUntil = System.currentTimeMillis() / 1000 + sessionTimeout;
							permParams = session.map;
							SID = session.sid;
							return;
						} else {
							sessions.remove(sidCandidate);
						}
					}
				}
			} else {
				sidCandidate = generateSID();
			}
            SessionMapEntry newSession = new SessionMapEntry(sidCandidate, host, System.currentTimeMillis() / 1000 + sessionTimeout);
            sessions.put(sidCandidate, newSession);
            outputCookies.add(new RCCookie("sid", sidCandidate, null, host, "/"));
            permParams = newSession.map;
            SID = sidCandidate;
		}

		/**
		 * Stvara novi SID koji se sastoji od 20 "uppercase" slova.
		 * 
		 * @return	novi identifikator sjednice.
		 */
		private String generateSID() {
			int leftLimit = 65; 
		    int rightLimit = 90;
		    int targetStringLength = 20;
		    String sid = sessionRandom.ints(leftLimit, rightLimit+1)
		    				.limit(targetStringLength)
		    				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
		    				.toString();		    
		    return sid;	
		}

		/**
		 * Određuje MIME tip na osnovu ekstenzije.
		 * 
		 * @param extension	ekstenzija.
		 * 
		 * @return	MIME tip.
		 */
		private String determineMimeType(String extension) {
			String mt = mimeTypes.get(extension);

			return mt == null ? "application/octet-stream" : mt;
		}
		
		private void sendEmptyResponse(int statusCode, String statusText) {		
			context = new RequestContext(ostream, params, params, outputCookies);
			context.setStatusCode(statusCode);
			context.setStatusText(statusText);
			try {
				context.write(new byte[0]);
			} catch (IOException e) {
				System.out.println("Error while sending empty response.");
			}
		}

		/**
		 * Parsira parametre zahtjeva.
		 * 
		 * @param paramString String s parametrima.
		 */
		private void parseParameters(String paramString) {
			if (paramString == null)
				return;

			String[] pairs = paramString.split("[&]");
			for (String pair : pairs) {
				String[] parts = pair.split("[=]", 2);
				if (parts.length == 2) {
					params.put(parts[0], parts[1]);
				} else {
					params.put(parts[0], null);
				}
			}
		}

		/**
		 * Vraća ekstenziju iz imena datoteke.
		 * 
		 * @param fileName	ime datoteke.
		 * 
		 * @return	ekstenzija.
		 */
		private String extractExtension(String fileName) {
			int p = fileName.lastIndexOf('.');

			if (p < 1)
				return "";

			return fileName.substring(p + 1).toLowerCase();
		}

		/**
		 * Čita zaglavlje zahtjeva.
		 * 
		 * @param is ulazni tok.
		 * 
		 * @return	pročitani okteti, ako postoje.
		 */
		private Optional<byte[]> readRequest(InputStream is) {

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int state = 0;
			try {
				l: while (true) {
					int b = is.read();
					if (b == -1) {
						if (bos.size() != 0) {
							throw new IOException("Incomplete header received.");
						}
						return Optional.empty();
					}
					if (b != 13) {
						bos.write(b);
					}
					switch (state) {
					case 0:
						if (b == 13) {
							state = 1;
						} else if (b == 10)
							state = 4;
						break;
					case 1:
						if (b == 10) {
							state = 2;
						} else
							state = 0;
						break;
					case 2:
						if (b == 13) {
							state = 3;
						} else
							state = 0;
						break;
					case 3:
						if (b == 10) {
							break l;
						} else
							state = 0;
						break;
					case 4:
						if (b == 10) {
							break l;
						} else
							state = 0;
						break;
					}
				}
				return Optional.of(bos.toByteArray());

			} catch (IOException e) {
				System.out.println("Error while reading request.");
				return Optional.empty();
			}

		}

		@Override
		public void dispatchRequest(String urlPath) throws Exception {
			internalDispatchRequest(urlPath, false);
		}
		
		/**
		 * Unutarnja metoda za slanje zahtjeva. Štiti privatne resurse poslužitelja.
		 * 
		 * @param urlPath		zatraženi put.
		 * @param directCall	zastavica koja određuje je li put direktno zatražen od strane klijenta.
		 * 
		 * @throws Exception	u slučaju pogreške.
		 */
		private void internalDispatchRequest(String urlPath, boolean directCall) throws Exception {
			if (context == null)
				context = new RequestContext(ostream, params, tempParams, permParams, outputCookies, this, SID);
			if (urlPath.startsWith("private") && directCall == true) {
				sendEmptyResponse(404, "Not found");
				return;
			}
			if (urlPath.startsWith("ext/")) {
				String fqcn = PATH_TO_WORKERS + urlPath.substring(4);
				Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
				Object newObject;
				newObject = referenceToClass.getDeclaredConstructor().newInstance();
				((IWebWorker)newObject).processRequest(context);
				return;
				
			}
			if (workersMap.containsKey(urlPath)) {
				workersMap.get(urlPath).processRequest(context);
				return;
			}
			Path requestedFilePath = documentRoot.resolve(urlPath).toAbsolutePath().normalize();
			if (!requestedFilePath.toString().startsWith(documentRoot.toString())) {
				sendEmptyResponse(403, "Forbidden");
				return;
			}
			if (!(requestedFilePath.toFile().exists() && requestedFilePath.toFile().isFile()
					&& requestedFilePath.toFile().canRead())) {
				sendEmptyResponse(404, "Not found");
				return;
			}
			String extension = extractExtension(requestedFilePath.getFileName().toString());
			if (extension.equals("smscr")) {
				executeScript(urlPath);
				return;
			}
			String mt = determineMimeType(extension);
			context.setMimeType(mt);
			context.setStatusCode(200);
			context.setStatusText("OK");
			try {
				context.write(Files.readAllBytes(requestedFilePath));
			} catch (IOException e) {
				System.out.println("Error while reading from the file.");
			}
		}

		/**
		 * Izvršava skriptu definiranu predanim putem.
		 * 
		 * @param urlPath	put do SmartScript skripte.
		 */
		private void executeScript(String urlPath) {
			String documentBody;
			try {
				documentBody = Files.readString(documentRoot.resolve(Path.of(urlPath)));
			} catch (IOException e) {
				System.out.println("Error while reading the script file.");
				return;
			}
			SmartScriptParser p = new SmartScriptParser(documentBody);
			new SmartScriptEngine(p.getDocumentNode(), context).execute();			
		}

	}

	/**
	 * Pokreće program.
	 * 
	 * @param args prima jedan argument: put do konfiguracijske datoteke.
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Invalid arguments: Expected a path to the config file.");
			return;
		}
		new SmartHttpServer(args[0]).start();
	}
}