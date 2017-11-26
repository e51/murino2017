package local.tcltk;

import local.tcltk.model.dao.DatabaseManager;
import local.tcltk.model.domain.Building;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import static local.tcltk.Constants.STRUCTURE;

@WebListener
public class Config implements ServletContextListener {
    private static final Logger logger = Logger.getLogger(Config.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Do stuff during webapp's startup.

        logger.info("Config init");

        if (STRUCTURE.isEmpty()) {
            STRUCTURE.put(new Integer(1), new Building(1, 7, new Integer[]{12, 12, 12, 12, 12, 12, 12}, 15));
            STRUCTURE.put(new Integer(2), new Building(2, 8, new Integer[]{12, 12, 12, 12, 12, 12, 12, 12}, 15));
        }

        // set application variable - DataSource ds
        sce.getServletContext().setAttribute("ds", DatabaseManager.getDataSource());

        logger.info("STRUCTURE: " + STRUCTURE);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Do stuff during webapp's shutdown.
    }
}
