package local.tcltk.controller;

import local.tcltk.HTMLHelper;
import local.tcltk.User;
import local.tcltk.exceptions.ProfileException;
import local.tcltk.model.DatabaseManager;
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

        // get current session
        HttpSession session = request.getSession();

        user = (User) session.getAttribute("user");
        action = request.getParameter("action");
        String sid = String.format(SID_PATTERN, request.getSession().getId().substring(request.getSession().getId().length() - 3));

        logger.info(String.format("[profile] %s Start. Got user object: %s, got action: %s", sid, user, action));

        if (user == null) {
            // Mustn't be here without user object in session. Have to login again.
//            throw new ProfileException("[profile] " + sid + " no user object. Redirecting to index. Request: " + request.getRequestURI() + "?" + request.getQueryString() + " IP: " + request.getRemoteAddr());

            throw new ProfileException(String.format("[profile] %s no user object. Request: %s?%s, remote address: %s", sid, request.getRequestURI(), request.getQueryString(), request.getRemoteAddr()));

//            logger.error(String.format("[profile] %s no user object. Request: %s?%s, remote address: %s", sid, request.getRequestURI(), request.getQueryString(), request.getRemoteAddr()));
//            result = "auth";
//            return result;

//            logger.error("[profile] {" + sid + "} no user object. Redirecting to index. Request: " + request.getRequestURI() + "?" + request.getQueryString() + " IP: " + request.getRemoteAddr());
//            response.sendRedirect(response.encodeRedirectURL(SITE_URL));
//            return;
        }

/*
//        if (user.isValid() && !"update".equals(action) && !"change".equals(action)) {
//        if (user.isValid() && !"update".equals(action) && user.getUpdates() >= UPDATE_ATTEMPTS) {
        if (user.isValid() && user.getUpdates() >= UPDATE_ATTEMPTS) {
            // if user is valid but no attempts left - then go to the view
//            logger.info("[profile] " + sid + " data is correct, no 'update' action, no 'change' action, redirecting to /view/");
            logger.info("[profile] " + sid + " data is correct, no 'update' action, no attempts left, so redirecting to /view/");
            result = "view";
            return result;
//            response.sendRedirect(response.encodeRedirectURL(VIEW_URL));
//            return;
        }
*/

//    if ("change".equals(action) && user.getUpdates() >= UPDATE_ATTEMPTS) {
        if (user.getUpdates() >= UPDATE_ATTEMPTS) {
//            logger.error("[profile] " + sid + " MUST NOT BE HERE! No attempts left, redirecting to /view/");
            logger.error(String.format("[profile] %s MUSTN'T BE HERE! No attempts left", sid));
            result = "view";
            return result;
//            response.sendRedirect(response.encodeRedirectURL(VIEW_URL));
//            return;
        }

        if ("update".equals(action)) {
            // update user data and redirect to view page

//            logger.info("[profile] " + sid + " 'update' action - trying to update...");
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
//                e.printStackTrace();

//                logger.error("[profile] " + sid + " updating - NumberFormatException. Let's RETRY. (building=" + strBuilding + ", section=" + strSection + ", floor=" + strFloor + ", flat=" + strFlat + ")");
                logger.error(String.format("[profile] %s updating - NumberFormatException. Let's RETRY. (building=%s, section=%s, floor=%s, flat=%s)", sid, strBuilding, strSection, strFloor, strFlat));
                result = "profile";
                return result;
//                response.sendRedirect(response.encodeRedirectURL(PROFILE_URL + "?action=change"));
//                return;
            }

            // insert a new user record or already have one? -- should be changed to database request for user.id?
            // valid user can be only from DB, invalid - no record in DB
//        boolean insert = user.isValid() ? false : true;
            boolean insert = DatabaseManager.getUserFromDB(user.getVk_id()) != null ? false : true;

//            logger.info("[profile] " + sid + " Have to insert a new record? : " + insert);
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

                if (insert) {   // new user
                    DatabaseManager.createNewUserDB(user);

                    // send a message to make admin happy
                    HTMLHelper.notify("Новый жилец:\nhttps://vk.com/id" + user.getVk_id());

                } else {        // update old user record
                    DatabaseManager.updateUserInDB(user);
                }

//            logger.info("[profile] " + sid + " Updating is SUCCESS, redirecting to /profile/ for checking");
//                logger.info("[profile] " + sid + " Updating is SUCCESS, redirecting to /view/");
                logger.info(String.format("[profile] %s Updating is SUCCESS", sid));

                result = "view";
                return result;
//            response.sendRedirect(response.encodeRedirectURL(PROFILE_URL));
//            return;

            } else {
                // bad data
//                logger.error("[profile] " + sid + " Updating has FAILED - BAD DATA. Let's RETRY. (building=" + building + ", section=" + section + ", floor=" + floor + ", flat=" + flat + ")");
                logger.error(String.format("[profile] %s Updating has FAILED - BAD DATA. Let's RETRY. (building=%d, section=%d, floor=%d, flat=%d)", sid, building, section, floor, flat));

                // return old values
                user.setBuilding(oldBuilding);
                user.setSection(oldSection);
                user.setFloor(oldFloor);
                user.setFlat(oldFlat);

                result = "profile";
                return result;

//                response.sendRedirect(response.encodeRedirectURL(PROFILE_URL + "?action=change"));
//                return;
            }
        }


        logger.info(String.format("[profile] %s Modify data request. Fill the form", sid));

        result = "profile";
        return result;
    }
}
