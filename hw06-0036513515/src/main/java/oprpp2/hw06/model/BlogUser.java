package oprpp2.hw06.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Razred modelira jednog korisnika.
 * 
 * @author mskrabic
 *
 */
@NamedQueries({
	@NamedQuery(name = "userExists", query = "SELECT COUNT(u) FROM BlogUser AS u WHERE u.nickname=:nn"),
	@NamedQuery(name = "listAllUsers", query = "SELECT u FROM BlogUser AS u"),
	@NamedQuery(name = "getUser", query = "SELECT u FROM BlogUser AS u WHERE u.nickname=:nn")
	})
@Entity
@Table(name="blog_users")
public class BlogUser {
	/**
	 * ID korisnika.
	 */
	private Long id;
	
	/**
	 * Ime korisnika.
	 */
	private String firstName;
	
	/**
	 * Prezime korisnika.
	 */
	private String lastName;
	
	/**
	 * Korisničko ime (nickname) korisnika.
	 */
	private String nickname;
	
	/**
	 * E-mail korisnika.
	 */
	private String email;
	
	/**
	 * SHA-1 hash korisnikove lozinke.
	 */
	private String passwordHash;
	
	/**
	 * Korisnikove objave.
	 */
	private List<BlogEntry> entries;
	
	/**
	 * Getter za ID.
	 * 
	 * @return	ID korisnika.
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	
	/**
	 * Setter za ID.
	 * 
	 * @param id	ID koji se želi postaviti.
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Getter za ime.
	 * 
	 * @return	ime korisnika.
	 */
	@Column(length=100, nullable=false)
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Setter za ime.
	 * 
	 * @param firstName ime koje se želi postaviti.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * Getter za prezime.
	 * 
	 * @return	prezime korisnika.
	 */
	@Column(length = 100, nullable=false)
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Setter za prezime.
	 * 
	 * @param lastName prezime koje se želi postaviti.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * Getter za korisničko ime.
	 * 
	 * @return	korisničko ime korisnika.
	 */
	@Column(length = 50, nullable = false, unique = true)
	public String getNickname() {
		return nickname;
	}
	
	/**
	 * Setter za korisničko ime.
	 * 
	 * @param nickname korisničko ime koje se želi postaviti.
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	/**
	 * Getter za e-mail.
	 * 
	 * @return e-mail korisnika.
	 */
	@Column(length = 100, nullable = false)
	public String getEmail() {
		return email;
	}
	
	/**
	 * Setter za e-mail.
	 * 
	 * @param email email koji se želi postaviti.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Getter za hash vrijednost lozinke.
	 * 
	 * @return	hash vrijednost korisnikove lozinke.
	 */
	@Column(length = 200, nullable = false)
	public String getPasswordHash() {
		return passwordHash;
	}
	
	/**
	 * Setter za hash vrijednost lozinke.
	 * 
	 * @param passwordHash hash vrijednost koja se želi postaviti.
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	/**
	 * Getter za objave.
	 * 
	 * @return korisnikove objave.
	 */
	@OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	public List<BlogEntry> getEntries() {
		return entries;
	}
	
	/**
	 * Setter za objave.
	 * 
	 * @param entries objave koje se žele postaviti.
	 */
	public void setEntries(List<BlogEntry> entries) {
		this.entries = entries;
	}
}
