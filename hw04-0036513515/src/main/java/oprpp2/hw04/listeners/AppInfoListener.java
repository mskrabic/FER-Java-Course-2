package oprpp2.hw04.listeners;

import java.time.Instant;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Listener koji spremi vrijeme početka rada aplikacije.
 * 
 * @author mskrabic
 *
 */
public class AppInfoListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		sce.getServletContext().setAttribute("START", Instant.now());

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {}

}
