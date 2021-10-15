package oprpp2.hw06.dao.jpa;

import java.util.List;

import javax.persistence.NoResultException;

import oprpp2.hw06.dao.DAO;
import oprpp2.hw06.dao.DAOException;
import oprpp2.hw06.model.BlogComment;
import oprpp2.hw06.model.BlogEntry;
import oprpp2.hw06.model.BlogUser;

/**
 * Implementacija DAO podsustava za JPA.
 * 
 * @author mskrabic
 */
public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		BlogEntry blogEntry = JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
		
		return blogEntry;
	}

	@Override
	public List<BlogEntry> getBlogEntriesByAuthor(Long authorId) {
		List<BlogEntry> entries = JPAEMProvider.getEntityManager().createNamedQuery("entriesByAuthor", BlogEntry.class)
				.setParameter("id", authorId)
				.getResultList();
		
		return entries;
	}

	@Override
	public boolean userExists(String nickname) {
		long count = JPAEMProvider.getEntityManager().createNamedQuery("userExists", Long.class)
				.setParameter("nn", nickname)
				.getSingleResult();
		
		return count == 1;
	}

	@Override
	public List<BlogUser> listAllUsers() {
		List<BlogUser> users = JPAEMProvider.getEntityManager().createNamedQuery("listAllUsers", BlogUser.class)
				.getResultList();
		
		return users;
	}

	@Override
	public void persistUser(BlogUser user) {
		JPAEMProvider.getEntityManager().persist(user);
		
	}

	@Override
	public BlogUser getUser(String nickname) {
		try {
			BlogUser user = JPAEMProvider.getEntityManager().createNamedQuery("getUser", BlogUser.class)
				.setParameter("nn", nickname)
				.getSingleResult();
			
			return user;
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public void persistEntry(BlogEntry entry) {
		JPAEMProvider.getEntityManager().persist(entry);
	}

	@Override
	public void persistComment(BlogComment comment) {
		JPAEMProvider.getEntityManager().persist(comment);
		
	}

	@Override
	public List<BlogComment> getComments(BlogEntry entry) {
		List<BlogComment> comments = JPAEMProvider.getEntityManager().createNamedQuery("commentsByEntry", BlogComment.class)
			.setParameter("be", entry)
			.getResultList();
		
		return comments;
	}

}