package oprpp2.hw06.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Razred modelira komentar na objavi u blogu.
 * 
 * @author mskrabic
 *
 */
@Entity
@Table(name = "blog_comments")
public class BlogComment {

	/**
	 * ID komentara.
	 */
	private Long id;

	/**
	 * Objava na kojoj je komentar.
	 */
	private BlogEntry blogEntry;

	/**
	 * E-mail korisnika.
	 */
	private String usersEMail;

	/**
	 * Tekst komentara.
	 */
	private String message;

	/**
	 * Datum objave.
	 */
	private Date postedOn;

	/**
	 * Getter za ID.
	 * 
	 * @return ID komentara.
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	/**
	 * Setter za ID.
	 * 
	 * @param id ID koji se želi postaviti.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter za objavu.
	 * 
	 * @return {@link BlogEntry} kojoj komentar pripada.
	 */
	@ManyToOne
	@JoinColumn(nullable = false)
	public BlogEntry getBlogEntry() {
		return blogEntry;
	}

	/**
	 * Setter za objavu.
	 * 
	 * @param blogEntry objava koja se želi postaviti.
	 */
	public void setBlogEntry(BlogEntry blogEntry) {
		this.blogEntry = blogEntry;
	}

	/**
	 * Getter za e-mail korisnika.
	 * 
	 * @return e-mail korisnika.
	 */
	@Column(length = 100, nullable = false)
	public String getUsersEMail() {
		return usersEMail;
	}

	/**
	 * Setter za e-mail korisnika.
	 * 
	 * @param usersEMail e-mail koji se želi postaviti.
	 */
	public void setUsersEMail(String usersEMail) {
		this.usersEMail = usersEMail;
	}

	/**
	 * Getter za tekst komentara.
	 * 
	 * @return tekst komentara.
	 */
	@Column(length = 4096, nullable = false)
	public String getMessage() {
		return message;
	}

	/**
	 * Setter za tekst komentara.
	 * 
	 * @param message tekst koji se želi postaviti.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Getter za datum objave.
	 * 
	 * @return datum objave komentara.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getPostedOn() {
		return postedOn;
	}

	/**
	 * Setter za datum objave.
	 * 
	 * @param postedOn datum objave koji se želi postaviti.
	 */
	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlogComment other = (BlogComment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}