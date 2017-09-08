package local.tcltk.model;

import local.tcltk.HTMLHelper;
import local.tcltk.model.dao.VkDAO;
import local.tcltk.model.domain.User;
import local.tcltk.exceptions.DAOException;
import local.tcltk.exceptions.ProfileException;
import local.tcltk.model.dao.DatabaseManager;
import local.tcltk.model.dao.UserDAO;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static local.tcltk.Constants.*;

public class ProfileAction implements Action {
    private static final Logger logger = Logger.getLogger(ProfileAction.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = null;
        String action = null;       // update or not
        String result = null;
        String sid = String.format(SID_PATTERN, request.getSession().getId().substring(request.getSession().getId().length() - SID_SIZE));

//        logger.info("Request URI: " + request.getRequestURI());
//        logger.info("Query string: " + request.getQueryString());
//        logger.info("SID: " + request.getSession().getId());
//        logger.info("encodeURL: " + response.encodeURL("any"));
//        logger.info("encodeRedirectURL: " + response.encodeRedirectURL("any"));
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
        action = request.getParameter("action");

        logger.info(String.format("[profile] %s Start. Got user object: %s, got action: %s", sid, user, action));

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

        if (user == null) {
            // Mustn't be here without user object in session. Have to login again - redirect to index.
//            throw new ProfileException("[profile] " + sid + " no user object. Redirecting to index. Request: " + request.getRequestURI() + "?" + request.getQueryString() + " IP: " + request.getRemoteAddr());

            throw new ProfileException(String.format("[profile] %s no user object. Request: %s?%s, remote address: %s", sid, request.getRequestURI(), request.getQueryString(), request.getRemoteAddr()));

//            logger.error(String.format("[profile] %s no user object. Request: %s?%s, remote address: %s", sid, request.getRequestURI(), request.getQueryString(), request.getRemoteAddr()));
//            result = "auth";
//            return result;
        }

        // have update attempts?
        if (user.getUpdates() >= UPDATE_ATTEMPTS) {
            logger.error(String.format("[profile] %s MUSTN'T BE HERE! No attempts left", sid));
            result = "view";
            return result;
        }

        // update(create) data action
        if ("update".equals(action)) {
            // update user data and redirect to view page

            logger.info(String.format("[profile] %s 'update' action - trying to update...", sid));

            int building = 0;
            int section = 0;
            int floor = 0;
            int flat = 0;

            String strBuilding = request.getParameter("building");
            String strSection = request.getParameter("section");
            String strFloor = request.getParameter("floor");
            String strFlat = request.getParameter("flat");

            if (strBuilding == null || strBuilding != null && strBuilding.isEmpty()) { strBuilding = "0"; }
            if (strSection == null || strSection != null && strSection.isEmpty()) { strSection = "0"; }
            if (strFloor == null || strFloor != null && strFloor.isEmpty()) { strFloor = "0"; }
            if (strFlat == null || strFlat != null && strFlat.isEmpty()) { strFlat = "0"; }

            try {
                building = Integer.valueOf(strBuilding);
                section = Integer.valueOf(strSection);
                floor = Integer.valueOf(strFloor);
                flat = Integer.valueOf(strFlat);
            } catch (NumberFormatException e) {
                logger.error(String.format("[profile] %s updating - NumberFormatException. Let's RETRY. (building=%s, section=%s, floor=%s, flat=%s)", sid, strBuilding, strSection, strFloor, strFlat));
                result = "profile";
                return result;
            }

            // insert a new user record or already have one? -- should be changed to database request for user.id?
            // valid user can be only from DB, invalid - no record in DB
//        boolean insert = user.isValid() ? false : true;
            //boolean insert = DatabaseManager.getUserFromDB(user.getVk_id()) != null ? false : true;
            boolean insert = userDAO.getEntityByVkId(user.getVk_id()) != null ? false : true;

            logger.info(String.format("[profile] %s Have to insert a new record? : %s", sid, insert));

            // current (old) values
            int oldBuilding = user.getBuilding();
            int oldSection = user.getSection();
            int oldFloor = user.getFloor();
            int oldFlat = user.getFlat();

            // set new values from form
            user.setBuilding(building);
            user.setSection(section);
            user.setFloor(floor);
            user.setFlat(flat);
            // and check them for valid
            if (user.isValid()) {
                // good data, let's update
                user.setUpdates(user.getUpdates() + 1);


                try {
                    if (insert) {   // new user
                        //DatabaseManager.createNewUserDB(user);
                        userDAO.insert(user);

                        // send a message to make admin happy
                        //HTMLHelper.notify("Новый жилец:\nhttps://vk.com/id" + user.getVk_id());
                        vkDAO.notify("Новый жилец:\nhttps://vk.com/id" + user.getVk_id());
                    } else {        // update old user record
                        //DatabaseManager.updateUserInDB(user);
                        userDAO.update(user);
                    }
                } catch (DAOException e) {
                    // error updating - return old values
                    logger.error(String.format("[profile] %s Modify Database FAILED - %s, %s (building=%d, section=%d, floor=%d, flat=%d)",
                            sid, e.getClass().getSimpleName(), e.getMessage(), building, section, floor, flat));

                    // return old values
                    user.setBuilding(oldBuilding);
                    user.setSection(oldSection);
                    user.setFloor(oldFloor);
                    user.setFlat(oldFlat);
                    user.setUpdates(user.getUpdates() - 1);

                    result = "view";
                    return result;
                }


                logger.info(String.format("[profile] %s Updating is SUCCESS", sid));

                result = "view";
                return result;
            } else {
                // bad data
                logger.error(String.format("[profile] %s Updating has FAILED - BAD DATA. Let's RETRY. (building=%d, section=%d, floor=%d, flat=%d)", sid, building, section, floor, flat));

                // return old values
                user.setBuilding(oldBuilding);
                user.setSection(oldSection);
                user.setFloor(oldFloor);
                user.setFlat(oldFlat);

                result = "profile";
                return result;
            }
        }


        logger.info(String.format("[profile] %s Modify data request. Fill the form", sid));

        result = "profile";
        return result;
    }
}
