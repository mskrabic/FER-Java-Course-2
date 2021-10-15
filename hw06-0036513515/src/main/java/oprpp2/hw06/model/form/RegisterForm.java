package oprpp2.hw06.model.form;

import javax.servlet.http.HttpServletRequest;

import oprpp2.hw06.model.BlogUser;
import oprpp2.hw06.util.Util;

public class RegisterForm extends AbstractForm {

	/**
	 * Ime korisnika.
	 */
	private String firstName;
	/**
	 * Prezime korisnika.
	 */
	private String lastName;
	
	/**
	 * E-mail korisnika.
	 */
	private String email;
	
	/**
	 * Nickname korisnika.
	 */
	private String nickname;
	
	/**
	 * Lozinka korisnika.
	 */
	private String password;
		
	@Override
	public void fillFromRequest(HttpServletRequest req) {
		this.firstName = Util.prepareString(req.getParameter("firstName"));
		this.lastName = Util.prepareString(req.getParameter("lastName"));
		this.email = Util.prepareString(req.getParameter("email"));
		this.nickname = Util.prepareString(req.getParameter("nickname"));
		this.password = Util.prepareString(req.getParameter("password"));
	}

	/**
	 * Na temelju predanog {@link BlogUser}-a popunjava ovaj formular.
	 * 
	 * @param user objekt koji čuva originalne podatke.
	 */
	public void fillFromUser(BlogUser user) {
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.nickname = user.getNickname();
	}

	/**
	 * Temeljem sadržaja ovog formulara puni svojstva predanog korisnika. 
	 * Metodu ne bi trebalo pozivati ako formular prethodno nije validiran i ako nije utvrđeno da nema pogrešaka.
	 * 
	 * @param user korisnik kojeg treba napuniti.
	 */
	public void fillUser(BlogUser user) {
		
		user.setFirstName(this.firstName);
		user.setLastName(this.lastName);
		user.setEmail(this.email);
		user.setNickname(this.nickname);
		
		user.setPasswordHash(Util.byteToHex(Util.hash(this.password)));
	}
	
	@Override
	public void validate() {
		errors.clear();
		
		if(this.firstName.isEmpty()) {
			errors.put("firstName", "Ime je obavezno!");
		}
		
		if(this.lastName.isEmpty()) {
			errors.put("lastName", "Prezime je obavezno!");
		}

		if(this.email.isEmpty()) {
			errors.put("email", "E-mail je obavezan!");
		} else {
			int l = email.length();
			int p = email.indexOf('@');
			if(l<3 || p==-1 || p==0 || p==l-1) {
				errors.put("email", "E-mail nije ispravnog formata.");
			}
		}
		
		if (this.nickname.isEmpty()) {
			errors.put("nickname", "Nickname je obavezan!");
		}
		
		if (this.password.length() < 8) {
			errors.put("password", "Lozinka mora biti barem 8 znakova!");
		}
		
	}


	/**
	 * Getter za  prezime.
	 * 
	 * @return prezime korisnika.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Setter za prezime. 
	 * 
	 * @param lastName vrijednost na koju ga treba postaviti.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Getter za ime.
	 * 
	 * @return ime korisnika.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Setter za ime. 
	 * 
	 * @param firstName vrijednost na koju ga treba postaviti.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Getter za email.
	 * 
	 * @return email korisnika.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Setter za email. 
	 * 
	 * @param email vrijednost na koju ga treba postaviti.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Getter za nickname.
	 * 
	 * @return	nickname korisnika.
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * Setter za nickname.
	 * 
	 * @param nickname	vrijednost na koju ga treba postaviti.
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * Getter za lozinku.
	 * 
	 * @return	lozinka.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Setter za lozinku.
	 * 
	 * @param password	vrijednost na koju je treba postaviti.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
