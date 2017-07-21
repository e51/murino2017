package local.tcltk.controller;

import local.tcltk.Building;
import local.tcltk.exceptions.ProfileException;
import local.tcltk.exceptions.VerifyException;
import local.tcltk.exceptions.ViewException;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static local.tcltk.Constants.*;

@WebServlet(name = "FrontControllerEmbeddedApp", urlPatterns = {"/e/*", "/m/*"})
public class FrontControllerEmbeddedApp extends HttpServlet {
    private static final Logger logger = Logger.getLogger(FrontControllerEmbeddedApp.class);

    static {
        if (STRUCTURE.isEmpty()) {
            STRUCTURE.put(new Integer(1), new Building(1, 7, new Integer[]{12, 12, 12, 12, 12, 12, 12}, 15));
            STRUCTURE.put(new Integer(2), new Building(2, 8, new Integer[]{12, 12, 12, 12, 12, 12, 12, 12}, 15));
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Action action = null;
        String view = null;
        String sid = String.format(SID_PATTERN, request.getSession().getId().substring(request.getSession().getId().length() - SID_SIZE));

        try {
            action = ActionFactory.getAction(request);
            view = action.execute(request, response);

            if (view.equals(ActionFactory.getActionPart(request))) {
                request.getRequestDispatcher("/WEB-INF/e/" + view + ".jsp").forward(request, response);
            } else {
                logger.info(String.format("[fc] %s Redirecting to %s", sid, view));
                response.sendRedirect(EMBEDDED_APP_SITE_ROOT + view + "/"); // We'd like to fire redirect in case of a view change as result of the action (PRG pattern).
            }
        } catch (VerifyException | ProfileException | ViewException e) {
            logger.error(String.format("[fc] %s Error: %s, %s", sid, e.getClass().getSimpleName(), e.getMessage()));
            logger.info(String.format("[fc] %s Redirecting to the error page", sid));
            response.sendRedirect(response.encodeRedirectURL(EMBEDDED_APP_SITE_ROOT + "error"));
        } catch (Exception e) {
            logger.error(String.format("[fc] %s Execution action failed: %s, %s, remote address: %s, requestURI: %s, action: %s, view: %s", sid, e.getClass().getSimpleName(), e.getMessage(), request.getRemoteAddr(), request.getRequestURI(), action, view));
            logger.info(String.format("[fc] %s Redirecting to the error page", sid));
            response.sendRedirect(response.encodeRedirectURL(EMBEDDED_APP_SITE_ROOT + "error"));
        }
    }
}
