package local.tcltk.model;

import local.tcltk.model.dao.UserDAO;
import local.tcltk.model.dao.VkDAO;
import local.tcltk.model.domain.User;
import local.tcltk.exceptions.AuthException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.List;

import static local.tcltk.Constants.*;

public class ViewAction implements Action {
    private static final Logger logger = Logger.getLogger(ViewAction.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = null;
        String result = null;
        String sid = String.format(SID_PATTERN, request.getSession().getId().substring(request.getSession().getId().length() - SID_SIZE));

//        logger.info("Request URI: " + request.getRequestURI());
//        logger.info("Query string: " + request.getQueryString());
//        logger.info("SID: " + request.getSession().getId());
//        logger.info("Plane URL: " );
//        logger.info("encodeURL: " + response.encodeURL(SITE_ROOT + "z/page4"));
//        logger.info("encodeRedirectURL: " + response.encodeRedirectURL(SITE_ROOT + "z/page4"));
//
//        Enumeration<String> names = request.getHeaderNames();
//        logger.info("");
//        while (names.hasMoreElements()) {
//            String name = names.nextElement();
//            logger.info(name + ": " + request.getHeader(name));
//        }
//        logger.info("");
//        for (String name : response.getHeaderNames()) {
//            logger.info(name + ": " + response.getHeader(name));
//        }
//        logger.info("");

        // get current session
        HttpSession session = request.getSession();
        user = (User) session.getAttribute("user");

        if (user == null) {
            // Mustn't be here without user object in the session. Have to login again - redirect to /auth/

//            throw new ViewException(String.format("[view] %s no user object. Request: %s?%s, remote address: %s", sid, request.getRequestURI(), request.getQueryString(), request.getRemoteAddr()));
            logger.error(String.format("[ViewAction] %s no user object. Request: %s?%s, remote address: %s", sid, request.getRequestURI(), request.getQueryString(), request.getRemoteAddr()));
            throw new AuthException("No user object");
//            result = "auth";
//            return result;
        }

        UserDAO userDAO = (UserDAO) session.getAttribute("userDAO");
        VkDAO vkDAO = (VkDAO) session.getAttribute("vkDAO");

        if (userDAO == null) {
            // should never happens, but just in case..
            userDAO = new UserDAO();
            logger.error(String.format("[ViewAction] missing userDAO session object. Create a new one.", sid));
        }
        if (vkDAO == null) {
            // should never happens, but just in case..
            vkDAO = new VkDAO();
            logger.error(String.format("[ViewAction] missing vkDAO session object. Create a new one.", sid));
        }


        if (user.isValid()) {
            // valid user, fill his neighbours lists
            logger.info(String.format("[ViewAction] %s got user object: %s, preparing neighbours", sid, user));

            // top
            List<User> topNeighbours = userDAO.getTopNeighbours(user);
            vkDAO.fillNeighboursVKData(topNeighbours);
            request.setAttribute("topNeighbours", topNeighbours);

            // floor
            List<User> floorNeighbours = userDAO.getFloorNeighbours(user);
            vkDAO.fillNeighboursVKData(floorNeighbours);
            request.setAttribute("floorNeighbours", floorNeighbours);

            // bottom
            List<User> bottomNeighbours = userDAO.getBottomNeighbours(user);
            vkDAO.fillNeighboursVKData(bottomNeighbours);
            request.setAttribute("bottomNeighbours", bottomNeighbours);

        } else {
            // a new user - show random neighbours
            logger.info(String.format("[ViewAction] %s got a new user object: %s, preparing random neighbours", sid, user));

            int count = 2;
            if (user.getAppVersion() == MOBILE_APP_USER) {
                count = 3;
            }
            if (user.getAppVersion() == EMBEDDED_APP_USER || user.getAppVersion() == WEB_SITE_USER) {
                count = 7;
            }

            List<User> randomNeighbours = userDAO.getRandomNeighbours(count);
            vkDAO.fillNeighboursVKData(randomNeighbours);
            request.setAttribute("randomNeighbours", randomNeighbours);
        }


        String use_flat = request.getParameter("f");

        if ("1".equals(use_flat)) {
            user.setUseFlat(true);
        } else {
            user.setUseFlat(false);
        }

//        logger.info(String.format("[view] %s got user object: %s, show neighbours", sid, user));

        result = "view";
        return result;
    }
}
