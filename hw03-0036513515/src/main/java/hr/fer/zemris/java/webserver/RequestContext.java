package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Razred modelira kontekst jednog zahtjeva.
 * 
 * @author mskrabic
 *
 */
public class RequestContext {
	
	/**
	 * Izlazni tok.
	 */
	private OutputStream outputStream;
	
	/**
	 * Charset zahtjeva.
	 */
	private Charset charset;
	
	/**
	 * Kodna stranica zahtjeva.
	 */
	private String encoding = "UTF-8";
	
	/**
	 * Statusni kod.
	 */
	private int statusCode = 200;
	
	/**
	 * Statusna poruka.
	 */
	private String statusText = "OK";
	
	/**
	 * Mime-type zahtjeva.
	 */
	private String mimeType = "text/html";
	
	/**
	 * Duljina sadržaja u zahtjevu.
	 */
	private Long contentLength = null;
	
	/**
	 * Parametri zahtjeva.
	 */
	private Map<String, String> parameters;
	
	/**
	 * Privremeni parametri zahtjeva.
	 */
	private Map<String, String> temporaryParameters;
	
	/**
	 * Perzistentni (trajni) parametri zahtjeva.
	 */
	private Map<String, String> persistentParameters;
	
	/**
	 * Kolačići zahtjeva.
	 */
	private List<RCCookie> outputCookies;
	
	/**
	 * Zastavica koja se postavlja nakon što se generira zaglavlje.
	 */
	private boolean headerGenerated = false;
	
	/**
	 * Dispečer zahtjeva.
	 */
	private IDispatcher dispatcher;
	
	/**
	 * Identifikator sjednice.
	 */
	private String sessionID;
	
	/**
	 * Konstruktor.
	 * 
	 * @param outputStream			izlazni tok.
	 * @param parameters			parametri.
	 * @param persistentParameters	trajni parametri.
	 * @param outputCookies			kolačići.
	 */
	public RequestContext(
			OutputStream outputStream, // must not be null!
			Map<String,String> parameters, // if null, treat as empty
			Map<String,String> persistentParameters, // if null, treat as empty
			List<RCCookie> outputCookies) {
		this.outputStream = Objects.requireNonNull(outputStream);
		this.parameters = (parameters == null ? Map.of() : Collections.unmodifiableMap(parameters));
		this.persistentParameters = (persistentParameters ==  null ? new HashMap<>() : persistentParameters);
		this.temporaryParameters = new HashMap<>();
		this.outputCookies = (outputCookies == null ? new ArrayList<>() : outputCookies);		
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param outputStream			izlazni tok.
	 * @param parameters			parametri.
	 * @param temporaryParameters	privremeni parametri.
	 * @param persistentParameters	trajni parametri.
	 * @param outputCookies			kolačići.
	 * @param dispatcher			dispečer.
	 * @param sessionID				identifikator sjednice.
	 */
	public RequestContext(
			OutputStream outputStream, 
			Map<String, String> parameters,
			Map<String, String> temporaryParameters, 
			Map<String, String> persistentParameters,
			List<RCCookie> outputCookies, 
			IDispatcher dispatcher,
			String sessionID) {
		this(outputStream, parameters, persistentParameters, outputCookies);
		this.temporaryParameters = temporaryParameters;
		this.dispatcher = dispatcher;
		this.sessionID = sessionID;
	}

	/**
	 * Vraća dispečera zahtjeva.
	 * 
	 * @return dispečer zahtjeva.
	 */
	public IDispatcher getDispatcher() {
		return dispatcher;
	}

	/**
	 * Postavlja kodnu stranicu zahtjeva.
	 * 
	 * @param encoding			kodna stranica koja se želi postaviti.
	 * 
	 * @throws RuntimeException ako se pozove nakon što je zaglavlje generirano.
	 */
	public void setEncoding(String encoding) {
		if (headerGenerated)
			throw new RuntimeException("Header has already been generated!");
		
		this.encoding = encoding;
	}

	/**
	 * Postavlja statusni kod zahtjeva.
	 * 
	 * @param statusCode		statusni kod koji se želi postaviti.
	 * 
	 * @throws RuntimeException ako se pozove nakon što je zaglavlje generirano.
	 */
	public void setStatusCode(int statusCode) {
		if (headerGenerated)
			throw new RuntimeException("Header has already been generated!");
		
		this.statusCode = statusCode;
	}
	
	/**
	 * Postavlja statusni tekst zahtjeva.
	 * 
	 * @param statusText		statusni tekst koji se želi postaviti.
	 * 
	 * @throws RuntimeException ako se pozove nakon što je zaglavlje generirano.
	 */
	public void setStatusText(String statusText) {
		if (headerGenerated)
			throw new RuntimeException("Header has already been generated!");
		
		this.statusText = statusText;
	}

	/**
	 * Postavlja MIME tip zahtjeva.
	 * 
	 * @param mimeType			MIME tip koji se želi postaviti.
	 * 
	 * @throws RuntimeException ako se pozove nakon što je zaglavlje generirano.
	 */
	public void setMimeType(String mimeType) {
		if (headerGenerated)
			throw new RuntimeException("Header has already been generated!");
		
		this.mimeType = mimeType;
	}

	/**
	 * Postavlja duljinu zahtjeva.
	 * 
	 * @param contentLength		duljina zahtjeva.
	 * 
	 * @throws RuntimeException ako se pozove nakon što je zaglavlje generirano.
	 */
	public void setContentLength(Long contentLength) {
		if (headerGenerated)
			throw new RuntimeException("Header has already been generated!");
		
		this.contentLength = contentLength;
	}

	/**
	 * Dohvaća parametar s predanim imenom.
	 * 
	 * @param name	ime parametra.
	 * 
	 * @return		vrijednost traženog parametra.
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}
	
	/**
	 * Dohvaća imena svih parametara.
	 * 
	 * @return	skup imena parametara.
	 */
	public Set<String> getParameterNames() {
		return Collections.unmodifiableSet(parameters.keySet());
	}
	
	/**
	 * Dohvaća trajni parametar s predanim imenom.
	 * 
	 * @param name	ime trajnog parametra.
	 * 
	 * @return		vrijednost traženog trajnog parametra.
	 */
	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}
	
	/**
	 * Dohvaća imena svih trajnih parametara.
	 * 
	 * @return	skup imena trajnih parametara.
	 */
	public Set<String> getPersistentParameterNames() {
		return Collections.unmodifiableSet(persistentParameters.keySet());
	}
	
	/**
	 * Postavlja vrijednost trajnog parametra.
	 * 
	 * @param name	ime trajnog parametra.
	 * @param value	vrijednost koja se želi postaviti.
	 */
	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(name, value);
	}
	
	/**
	 * Briše trajni parametar.
	 * 
	 * @param name	ime trajnog parametra.
	 */
	public void removePersistentParameter(String name) {
		persistentParameters.remove(name);
	}
	
	/**
	 * Dohvaća privremeni parametar s predanim imenom.
	 * 
	 * @param name	ime privremenog parametra.
	 * 
	 * @return		vrijednost traženog	privremenog parametra.
	 */
	public String getTemporaryParameter(String name) {
		return temporaryParameters.get(name);
	}
	
	/**
	 * Dohvaća imena svih privremenih parametara.
	 * 
	 * @return	skup imena privremenih parametara.
	 */
	public Set<String> getTemporaryParameterNames() {
		return Collections.unmodifiableSet(temporaryParameters.keySet());
	}
	
	/**
	 * Dohvaća identifikator sjednice.
	 * 
	 * @return	identifikator sjednice.
	 */
	public String getSessionID() {
		return sessionID;
	}
	
	/**
	 * Postavlja vrijednost privremenog parametra.
	 * 
	 * @param name	ime privremenog parametra.
	 * @param value	vrijednost koja se želi postaviti.
	 */
	public void setTemporaryParameter(String name, String value) {
		temporaryParameters.put(name, value);
	}
	
	/**
	 * Briše privremeni parametar.
	 * 
	 * @param name	ime privremenog parametra.
	 */
	public void removeTemporaryParameter(String name) {
		temporaryParameters.remove(name);
	}
	
	/**
	 * Dodaje kolačić u zahtjev.
	 * 
	 * @param c	kolačić koji se želi dodati.
	 */
	public void addRCCookie(RCCookie c) {
		outputCookies.add(c);
	}
	
	/**
	 * Piše predane oktete na izlazni tok.
	 * 
	 * @param data			okteti.
	 * 
	 * @return				kontekst zahtjeva.
	 * @throws IOException	u slučaju greške pri pisanju.
	 */
	public RequestContext write(byte[] data) throws IOException {
		if (!headerGenerated) {
			generateHeader();
		}
		
		outputStream.write(data);
		
		return this;
	}

	/**
	 * Piše predane oktete na izlazni tok.
	 * 
	 * @param data			okteti.
	 * @param offset		odmak.
	 * @param len			duljina.
	 * 
	 * @return				kontekst zahtjeva.
	 * @throws IOException	u slučaju greške pri pisanju.
	 */
	public RequestContext write(byte[] data, int offset, int len) throws IOException {
		if (!headerGenerated) {
			generateHeader();
		}
		outputStream.write(data, offset, len);
		
		return this;
	}
	
	/**
	 * Piše predani String na izlazni tok, kodiran kodnom stranicom zahtjeva.
	 * 
	 * @param text			tekst.
	 * 
	 * @return				kontekst zahtjeva.
	 * @throws IOException	u slučaju greške pri pisanju.
	 */
	public RequestContext write(String text) throws IOException {
		if (!headerGenerated) {
			generateHeader();
		}
		
		ByteBuffer bb = charset.encode(text);
		byte[] data = new byte[bb.remaining()];
		System.arraycopy(bb.array(), 0, data, 0, data.length);

		outputStream.write(data);
		
		return this;
	}

	/**
	 * Generira zaglavlje zahtjeva.
	 */
	private void generateHeader() {
		charset = Charset.forName(encoding);
		StringBuilder sb = new StringBuilder();
		
		sb.append("HTTP/1.1 " + statusCode + " " + statusText + "\r\n");
		sb.append("Content-Type: " + mimeType);
		if (mimeType.startsWith("text/")) {
			sb.append("; charset=" + encoding);
		}
		sb.append("\r\n");
		if (contentLength != null) {
			sb.append("Content-Length:" + contentLength + "\r\n");
		}
		if (!outputCookies.isEmpty()) {
			outputCookies.forEach(c -> sb.append(getCookieString(c) + "\r\n"));
		}
		sb.append("\r\n");
		try {
			outputStream.write(sb.toString().getBytes(StandardCharsets.ISO_8859_1));
		} catch (IOException e) {
			System.out.println("Error while writing header.");
		}
		headerGenerated = true;	
	}
	
	/**
	 * Parsira kolačić u String.
	 * 
	 * @param c	kolačić.
	 * 
	 * @return	kolačić zapisan kao String.
	 */
	private String getCookieString(RCCookie c) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Set-Cookie: ");
		if (c.name != null && c.value != null) {
			sb.append(c.name + "=").append("\""+c.value+"\"");
		}
		if (c.domain != null) {
			sb.append("; Domain=" + c.domain);
		}
		if (c.path != null) {
			sb.append("; Path=" + c.path);
		}
		if (c.maxAge != null) {
			sb.append("; Max-Age=" + c.maxAge);
		}
		sb.append("; Http-Only");
		
		return sb.toString();
	}

	/**
	 * Razred modelira kolačić (engl. cookie).
	 * 
	 * @author mskrabic
	 *
	 */
	public static class RCCookie {
		/**
		 * Ime kolačića.
		 */
		private String name;
		
		/**
		 * Vrijednost kolačića.
		 */
		private String value;
		
		/**
		 * Domena kolačića.
		 */
		private String domain;
		
		/**
		 * Put kolačića.
		 */
		private String path;
		
		/**
		 * Maksimalno trajanje kolačića.
		 */
		private Integer maxAge;
		
		/**
		 * Konstruktor.
		 * 
		 * @param name		ime.
		 * @param value		vrijednost.
		 * @param maxAge	maksimalno trajanje.
		 * @param domain	domena.
		 * @param path		put.
		 */
		public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
		}
		
		/**
		 * Dohvaća ime kolačića.
		 * 
		 * @return ime kolačića.
		 */
		public String getName() {
			return name;
		}
		
		/**
		 * Dohvaća vrijednost kolačića.
		 * 
		 * @return vrijednost kolačića.
		 */
		public String getValue() {
			return value;
		}
		
		/**
		 * Dohvaća domenu kolačića.
		 * 
		 * @return	domena kolačića.
		 */
		public String getDomain() {
			return domain;
		}
		
		/**
		 * Dohvaća put kolačića.
		 * 
		 * @return put kolačića.
		 */
		public String getPath() {
			return path;
		}
		
		/**
		 * Dohvaća maksimalno trajanje kolačića.
		 * 
		 * @return	maksimalno trajanje kolačića.
		 */
		public Integer getMaxAge() {
			return maxAge;
		}
		
		
	}
}
