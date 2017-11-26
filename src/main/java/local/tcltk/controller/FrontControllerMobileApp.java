package local.tcltk.controller;

import local.tcltk.model.Action;
import local.tcltk.model.ActionFactory;
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

@WebServlet(name = "FrontControllerMobileApp", urlPatterns = {"/m/*"})
public class FrontControllerMobileApp extends HttpServlet {
    private static final Logger logger = Logger.getLogger(FrontControllerMobileApp.class);

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

            action = ActionFactory.getAction(request);
            view = action.execute(request, response);

            if (view.equals(ActionFactory.getActionPart(request))) {
                logger.info(String.format("[fc] %s Forwarding to %s", sid, view));
                request.getRequestDispatcher("/WEB-INF/m/" + view + ".jsp").forward(request, response);
            } else {
                logger.info(String.format("[fc] %s Redirecting to %s", sid, response.encodeRedirectURL(view)));
                response.sendRedirect(response.encodeRedirectURL(MOBILE_APP_ROOT_URL + view + "/")); // We'd like to fire redirect in case of a view change as result of the action (PRG pattern).
            }
        } catch (VerifyException e) {
            logger.error(String.format("[fc] %s Error: %s, %s", sid, e.getClass().getSimpleName(), e.getMessage()));
            logger.info(String.format("[fc] %s Redirecting to the error page", sid));
            response.sendRedirect(response.encodeRedirectURL(MOBILE_APP_ROOT_URL + "auth-error"));
        } catch (ProfileException | ViewException | AuthException e) {
            logger.error(String.format("[fc] %s Error: %s, %s", sid, e.getClass().getSimpleName(), e.getMessage()));
            logger.info(String.format("[fc] %s Redirecting to the error page", sid));
            response.sendRedirect(response.encodeRedirectURL(MOBILE_APP_ROOT_URL + "error"));
        } catch (Exception e) {
            logger.error(String.format("[fc] %s Execution action failed: %s, %s, remote address: %s, requestURI: %s, action: %s, view: %s", sid, e.getClass().getSimpleName(), e.getMessage(), request.getRemoteAddr(), request.getRequestURI(), action, view));
            logger.info(String.format("[fc] %s Redirecting to the error page", sid));
            response.sendRedirect(response.encodeRedirectURL(MOBILE_APP_ROOT_URL + "error"));
        }
    }
}
