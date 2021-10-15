package oprpp2.hw06.web.init;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import oprpp2.hw06.dao.jpa.JPAEMFProvider;

/**
 * Listener koji inicijalizira tvornicu EntityManager-a pri pokretanju aplikacije, te je gasi pri gašenju aplikacije.
 * 
 * @author mskrabic
 *
 */
@WebListener
public class InitializationListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("baza.podataka.za.blog");  
		sce.getServletContext().setAttribute("my.application.emf", emf);
		JPAEMFProvider.setEmf(emf);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		JPAEMFProvider.setEmf(null);
		EntityManagerFactory emf = (EntityManagerFactory)sce.getServletContext().getAttribute("my.application.emf");
		if(emf!=null) {
			emf.close();
		}
	}
}