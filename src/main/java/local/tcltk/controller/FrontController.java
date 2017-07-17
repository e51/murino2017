package local.tcltk.controller;

import local.tcltk.exceptions.AuthException;
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

@WebServlet(name = "FrontController", urlPatterns = {"/verify", "/profile", "/view", "/auth"})
public class FrontController extends HttpServlet {
    private static final Logger logger = Logger.getLogger(FrontController.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Action action = null;
        String view = null;
        String sid = String.format(SID_PATTERN, request.getSession().getId().substring(request.getSession().getId().length() - 3));

        try {
            action = ActionFactory.getAction(request);
            view = action.execute(request, response);

            if (view.equals(request.getRequestURI().substring(PROJECT_NAME.length() + 2))) {
                request.getRequestDispatcher("/WEB-INF/" + view + ".jsp").forward(request, response);
            } else {
                logger.info(String.format("[fc] %s Redirecting to %s", sid, view));
                response.sendRedirect(view); // We'd like to fire redirect in case of a view change as result of the action (PRG pattern).
            }
        } catch (VerifyException | ProfileException | ViewException e) {
            logger.error(e.getMessage());
            logger.info(String.format("[fc] %s Redirecting to index", sid));
            response.sendRedirect(response.encodeRedirectURL(SITE_URL));
//        } catch (AuthException e) {
//            logger.error(e.getMessage());
//            logger.info(String.format("[fc] %s Redirecting to auth", sid));
//            response.sendRedirect(response.encodeRedirectURL(AUTH_URL));
        } catch (Exception e) {
            logger.error(String.format("[fc] %s Execution action failed. %s, remote address: %s, requestURI: %s, action: %s, view: %s", sid, e, request.getRemoteAddr(), request.getRequestURI(), action, view));
            logger.info(String.format("[fc] %s Redirecting to index", sid));
            response.sendRedirect(response.encodeRedirectURL(SITE_URL));
        }
    }
}