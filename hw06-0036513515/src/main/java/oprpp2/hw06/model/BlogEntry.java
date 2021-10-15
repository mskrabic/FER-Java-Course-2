package oprpp2.hw06.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Razred modelira objavu u blogu, tj. entry.
 * 
 * @author mskrabic
 *
 */
@NamedQueries({
		@NamedQuery(name = "commentsByEntry", query = "select b from BlogComment as b where b.blogEntry=:be"),
		@NamedQuery(name = "entriesByAuthor", query = "select b from BlogEntry as b where b.author.id=:id") 
		})
@Entity
@Table(name = "blog_entries")
@Cacheable(true)
public class BlogEntry {

	/**
	 * ID objave.
	 */
	private Long id;

	/**
	 * Komentari.
	 */
	private List<BlogComment> comments = new ArrayList<>();

	/**
	 * Datum kreiranja.
	 */
	private Date createdAt;

	/**
	 * Datum zadnje modifikacije.
	 */
	private Date lastModifiedAt;

	/**
	 * Naslov.
	 */
	private String title;

	/**
	 * Tekst.
	 */
	private String text;

	/**
	 * Autor.
	 */
	private BlogUser author;

	/**
	 * Getter za ID.
	 * 
	 * @return ID objave.
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	/**
	 * Setter za ID.
	 * 
	 * @param id id koji se želi postaviti.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter za komentare.
	 * 
	 * @return lista komentara.
	 */
	@OneToMany(mappedBy = "blogEntry", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	@OrderBy("postedOn")
	public List<BlogComment> getComments() {
		return comments;
	}

	/**
	 * Setter za komentare.
	 * 
	 * @param comments komentari koji se žele postaviti.
	 */
	public void setComments(List<BlogComment> comments) {
		this.comments = comments;
	}

	/**
	 * Getter za datum kreiranja.
	 * 
	 * @return datum kreiranja.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * Setter za datum kreiranja.
	 * 
	 * @param createdAt datum kreiranja koji se želi postaviti.
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Getter za datum zadnje modifikacije.
	 * 
	 * @return datum zadnje modifikacije.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true)
	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	/**
	 * Setter za datum zadnje modifikacije.
	 * 
	 * @param createdAt datum zadnje modifikacije koji se želi postaviti.
	 */
	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	/**
	 * Getter za naslov.
	 * 
	 * @return naslov.
	 */
	@Column(length = 200, nullable = false)
	public String getTitle() {
		return title;
	}

	/**
	 * Setter za naslov.
	 * 
	 * @param title naslov koji se želi postaviti.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Getter za tekst.
	 * 
	 * @return tekst objave.
	 */
	@Column(length = 4096, nullable = false)
	public String getText() {
		return text;
	}

	/**
	 * Setter za tekst.
	 * 
	 * @param text tekst koji se želi postaviti.
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Getter za autora.
	 * 
	 * @return autor objave.
	 */
	@ManyToOne
	@JoinColumn(nullable = false)
	public BlogUser getAuthor() {
		return author;
	}

	/**
	 * Setter za autora.
	 * 
	 * @param author autor koji se želi postaviti.
	 */
	public void setAuthor(BlogUser author) {
		this.author = author;
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
		BlogEntry other = (BlogEntry) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}