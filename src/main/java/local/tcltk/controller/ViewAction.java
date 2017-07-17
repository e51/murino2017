package local.tcltk.controller;

import local.tcltk.User;
import local.tcltk.exceptions.AuthException;
import local.tcltk.exceptions.ViewException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static local.tcltk.Constants.SID_PATTERN;

public class ViewAction implements Action {
    private static final Logger logger = Logger.getLogger(ViewAction.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = null;
        String use_flat = null;
        String result = null;

        // get current session
        HttpSession session = request.getSession();
        user = (User) session.getAttribute("user");
        use_flat = request.getParameter("f");

        String sid = String.format(SID_PATTERN, request.getSession().getId().substring(request.getSession().getId().length() - 3));

        if (user == null) {
            // Mustn't be here without user object in session. Have to login again.
//            throw new ViewException("[view] " + sid + " no user object. Redirecting to index. Request: " + request.getRequestURI() + "?" + request.getQueryString() + " remote address: " + request.getRemoteAddr());
//            throw new ViewException(String.format("[view] %s no user object. Request: %s?%s, remote address: %s", sid, request.getRequestURI(), request.getQueryString(), request.getRemoteAddr()));

//            throw new AuthException(String.format("[view] %s no user object. Request: %s?%s, remote address: %s", sid, request.getRequestURI(), request.getQueryString(), request.getRemoteAddr()));

            logger.error(String.format("[view] %s no user object. Request: %s?%s, remote address: %s", sid, request.getRequestURI(), request.getQueryString(), request.getRemoteAddr()));
            result = "auth";
            return result;

//            logger.error("[view] " + sid + " no user object. Redirecting to index. Request: " + request.getRequestURI() + "?" + request.getQueryString() + " IP: " + request.getRemoteAddr());
//            response.sendRedirect(response.encodeRedirectURL(SITE_URL));
//            return;
        }

        if ("1".equals(use_flat)) {
            user.setUseFlat(true);
        } else {
            user.setUseFlat(false);
        }

//        logger.info("[view] " + sid + " got user object: " + user + ", show neighbours");
        logger.info(String.format("[view] %s got user object: %s, show neighbours", sid, user));

        result = "view";
        return result;
    }
}
