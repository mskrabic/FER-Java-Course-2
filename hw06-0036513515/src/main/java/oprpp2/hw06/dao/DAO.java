package oprpp2.hw06.dao;

import java.util.List;

import oprpp2.hw06.model.BlogComment;
import oprpp2.hw06.model.BlogEntry;
import oprpp2.hw06.model.BlogUser;

/**
 * Sučelje prema podsustavu za perzistenciju podataka.
 * 
 * @author mskrabic
 *
 */
public interface DAO {

	/**
	 * Dohvaća entry za zadani ID. Ako takav entry ne postoji,
	 * vraća <code>null</code>.
	 * 
	 * @param id ključ zapisa.
	 * 
	 * @return entry ili <code>null</code> ako entry ne postoji.
	 * 
	 * @throws DAOException ako dođe do pogreške pri dohvatu podataka.
	 */
	public BlogEntry getBlogEntry(Long id) throws DAOException;
	
	/**
	 * Dohvaća sve {@BlogEntry}-e za autora s predanim ID-em.
	 * 
	 * @param authorId	ID autora.
	 * 
	 * @return lista objava traženog autora.
	 */
	public List<BlogEntry> getBlogEntriesByAuthor(Long authorId);
	
	/**
	 * Provjerava postoji li već korisnik sa zadanim nicknameom.
	 * 
	 * @param nickname	nickname za koji se radi provjera.
	 * 
	 * @return <code>true</code> ako korisnik postoji, <code>false</code> inače.
	 */
	public boolean userExists(String nickname);
	
	/**
	 * Vraća listu svih korisnika.
	 * 
	 * @return	lista svih korisnika.
	 */
	public List<BlogUser> listAllUsers();

	/**
	 * Pohranjuje novog korisnika.
	 * 
	 * @param user	korisnik kojeg treba pohraniti.
	 */
	public void persistUser(BlogUser user);
	
	/**
	 * Dohvaća korisnika sa zadanim nickname-om.
	 * 
	 * @param nickname	nickname korisnika.
	 * 
	 * @return	korisnik.
	 */
	public BlogUser getUser(String nickname);

	/**
	 * Pohranjuje novi {@link BlogEntry}.
	 * 
	 * @param entry	entry koji treba pohraniti.
	 */
	public void persistEntry(BlogEntry entry);

	/**
	 * Pohranjuje novi {@link BlogComment}.
	 * 
	 * @param comment	komentar koji treba pohraniti.
	 */
	public void persistComment(BlogComment comment);

	/**
	 * Dohvaća sve komentare za zadani entry.
	 * 
	 * @param entry	entry čije komentare treba dohvatiti.
	 * 
	 * @return	lista komentara zadanog entry-a.
	 */
	public List<BlogComment> getComments(BlogEntry entry);

}