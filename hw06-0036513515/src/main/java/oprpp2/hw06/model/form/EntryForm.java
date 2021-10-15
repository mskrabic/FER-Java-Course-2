package oprpp2.hw06.model.form;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import oprpp2.hw06.model.BlogEntry;
import oprpp2.hw06.util.Util;

/**
 * Formular za objavu {@link BlogEntry}.
 * 
 * @author mskrabic
 *
 */
public class EntryForm extends AbstractForm {
	/**
	 * ID objave.
	 */
	String id;
	
	/**
	 * Naslov objave.
	 */
	String title;
	
	/**
	 * Tekst objave.
	 */
	String text;
	
	/**
	 * Zadnja promjena objave.
	 */
	Date lastModified;
	
	/**
	 * Getter za ID.
	 * 
	 * @return	ID objave.
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Setter za ID.
	 * 
	 * @param id ID koji se želi postaviti.
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Getter za naslov.
	 * 
	 * @return	naslov objave.
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Setter za naslov.
	 * 
	 * @param title	naslov koji se želi postaviti.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Getter za tekst.
	 * 
	 * @return	tekst objave.
	 */
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
	 * Getter za datum zadnje promjene.
	 * 
	 * @return	datum zadnje promjene.
	 */
	public Date getLastModified() {
		return lastModified;
	}
	
	/**
	 * Setter za datum zadnje promjene.
	 * 
	 * @param lastModified datum koji se želi postaviti.
	 */
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	
	@Override
	public void fillFromRequest(HttpServletRequest req) {	
		this.id = Util.prepareString(req.getParameter("id"));
		this.title = Util.prepareString(req.getParameter("title"));
		this.text = Util.prepareString(req.getParameter("text"));
		this.lastModified = new Date();
	}
	
	/**
	 * Popunjava formular iz predanog {@link BlogEntry}-a.
	 * 
	 * @param entry	objava iz koje treba popuniti formular.
	 */
	public void fillFromEntry(BlogEntry entry) {
		this.id = entry.getId().toString();
		this.title = entry.getTitle();
		this.text = entry.getText();
		this.lastModified = entry.getLastModifiedAt();
	}
	
	/**
	 * Popunjava {@link BlogEntry} na osnovu formulara.
	 * 
	 * @param entry	objava u koju treba zapisati vrijednosti iz formulara.
	 */
	public void fillEntry(BlogEntry entry) {
		entry.setId(id.isEmpty() ? null : Long.parseLong(id));
		entry.setTitle(title);
		entry.setText(text);
		entry.setLastModifiedAt(lastModified);
		if (id.isEmpty())
			entry.setCreatedAt(lastModified);
	}
	
	@Override
	public void validate() {
		if (title.isEmpty()) {
			setError("title", "Title is required!");
		}
		if (text.isEmpty()) {
			setError("text", "Text is required!");
		}
	}
}
