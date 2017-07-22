package local.tcltk.controller;

import local.tcltk.Building;
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
import java.util.*;

import static local.tcltk.Constants.*;

@WebServlet(name = "FrontController", urlPatterns = {"/verify", "/profile", "/view", "/auth", "/profile/", "/view/", "/auth/"})
public class FrontController extends HttpServlet {
    private static final Logger logger = Logger.getLogger(FrontController.class);

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
//            logger.info("Request URI: " + request.getRequestURI());
//            logger.info("Query string: " + request.getQueryString());
//            logger.info("SID: " + request.getSession().getId());
//            logger.info("encodeURL: " + response.encodeURL("any.jsp"));
//            logger.info("encodeRedirectURL: " + response.encodeRedirectURL("any.jsp"));
//
//            Enumeration<String> names = request.getHeaderNames();
//            logger.info("");
//            while (names.hasMoreElements()) {
//                String name = names.nextElement();
//                logger.info(name + ": " + request.getHeader(name));
//            }
//            logger.info("");
//            for (String name : response.getHeaderNames()) {
//                logger.info(name + ": " + response.getHeader(name));
//            }
//            logger.info("");

//            int status = response.getStatus();
//            response.reset();
//            response.setStatus(status);
//
//            for (String name : response.getHeaderNames()) {
//                logger.info(name + ": " + response.getHeader(name));
//            }

            action = ActionFactory.getAction(request);
            view = action.execute(request, response);

            if (view.equals(ActionFactory.getActionPart(request))) {
                logger.info(String.format("[fc] %s Forwarding to %s.jsp", sid, view));
                request.getRequestDispatcher("/WEB-INF/" + view + ".jsp").forward(request, response);
            } else {
                logger.info(String.format("[fc] %s Redirecting to %s", sid, response.encodeRedirectURL(view)));
//                response.sendRedirect(SITE_ROOT + view + "/"); // We'd like to fire redirect in case of a view change as result of the action (PRG pattern).
//                response.sendRedirect(response.encodeRedirectURL(SITE_ROOT + view + "/")); // We'd like to fire redirect in case of a view change as result of the action (PRG pattern).
                response.sendRedirect(response.encodeRedirectURL(WEB_APP_ROOT_URL + view + "/")); // We'd like to fire redirect in case of a view change as result of the action (PRG pattern).
            }
        } catch (VerifyException | ProfileException | ViewException e) {
            logger.error(e.getMessage());
            logger.info(String.format("[fc] %s Redirecting to index: %s", sid, response.encodeRedirectURL(WEB_APP_ROOT_URL)));
            response.sendRedirect(response.encodeRedirectURL(WEB_APP_ROOT_URL));
        } catch (AuthException e) {
            logger.error(e.getMessage());
            logger.info(String.format("[fc] %s Redirecting to auth: %s", sid, response.encodeRedirectURL(WEB_APP_AUTH_URL)));
            response.sendRedirect(response.encodeRedirectURL(WEB_APP_AUTH_URL));
        } catch (Exception e) {
            logger.error(String.format("[fc] %s Execution action failed. %s, remote address: %s, requestURI: %s, action: %s, view: %s", sid, e, request.getRemoteAddr(), request.getRequestURI(), action, view));
            logger.info(String.format("[fc] %s Redirecting to index: %s", sid, response.encodeRedirectURL(WEB_APP_ROOT_URL)));
            response.sendRedirect(response.encodeRedirectURL(WEB_APP_ROOT_URL));
        }
    }
}
