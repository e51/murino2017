package local.tcltk.model;

import local.tcltk.HTMLHelper;
import local.tcltk.model.domain.User;
import local.tcltk.exceptions.VerifyException;
import local.tcltk.model.dao.UserDAO;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static local.tcltk.Constants.*;

/**
 * Check vk authentication (parse vk response - get code, request token)
 * If auth is ok - create user in Database and User object, make a session with it, redirect to the view page
 * If auth has failed - redirect back to the index page
 */
public class VerifyAction implements Action {
    private static final Logger logger = Logger.getLogger(VerifyAction.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = null;               // user object - create user after successful authentication
        String result = null;           // return to index by default
        String sid = String.format(SID_PATTERN, request.getSession().getId().substring(request.getSession().getId().length() - SID_SIZE));

//        logger.info("Request URI: " + request.getRequestURI());
//        logger.info("Query string: " + request.getQueryString());
//        logger.info("SID: " + request.getSession().getId());
//        logger.info("Plane URL: " );
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

        logger.info(String.format("[verify] %s Start checking. Remote address: %s", sid, request.getRemoteAddr()));

        // get current session
        HttpSession session = request.getSession();

        // check vk answer for error (parse GET parameters)
        String error = request.getParameter("error");

        if (error != null) {
            // Got auth error - redirect to the index page
//            throw new VerifyException("[verify] " + sid + " auth error: " + request.getParameter("error_reason") + ", " + request.getParameter("error_description"));
            throw new VerifyException(String.format("[verify] %s auth error: %s, %s", sid, request.getParameter("error_reason"), request.getParameter("error_description")));
//            logger.error("[verify] {" + sid + "} auth error: " + request.getParameter("error_reason") + ", " + request.getParameter("error_description") + " - Redirecting to index");
//            logger.error(request.getQueryString());
//            result = "";
//            response.sendRedirect(response.encodeRedirectURL(WEB_APP_SITE_URL));
//            return;
        }

        // no error

        String code = null;         // vk code for token request
        code = request.getParameter("code"); // get code from vk
//        logger.info("[verify] " + sid + " code: " + code);
        logger.info(String.format("[verify] %s code: %s", sid, code));

        // VK token request part - - - - - - - - -
        // Variant 2 - use web + json
        // use code for token request
        String contextParams = "?client_id=" + VK_WEB_APP_ID +
                "&client_secret=" + VK_WEB_APP_SECRET +
                "&redirect_uri=" + WEB_APP_VK_REDIRECT_URI +
                "&code=" + code;

        String json = HTMLHelper.getVKResponse(VK_WEB_APP_GET_TOKEN_URL + contextParams);

//        logger.info("[verify] " + sid + " VK response json: " + json);
        logger.info(String.format("[verify] %s VK response json: %s", sid, json));
        // possible json-answers are:
        //{"access_token":"533bacf01e11f55b536a565b57531ac114461ae8736d6506a3", "expires_in":43200, "user_id":66748}
        //{"error":"invalid_grant","error_description":"Code is expired."}

        if (json == null) {
            // error getting vk answer - redirect to index
//            throw new VerifyException("[verify] {" + sid + "} Got NULL answer from vk - redirecting to index" + ", IP: " + request.getRemoteAddr());
            throw new VerifyException(String.format("[verify] %s Got NULL answer from vk, remote address: %s", sid, request.getRemoteAddr()));
//            logger.error("[verify] {" + sid + "} Got NULL answer from vk - redirecting to index" + ", IP: " + request.getRemoteAddr());
//                response.sendRedirect(response.encodeRedirectURL(WEB_APP_SITE_URL));
//                return;
        }

        // no errors

        // parse json
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(json);
        } catch (ParseException e) {
//            e.printStackTrace();

//            throw new VerifyException("[verify] {" + sid + "} Parse json Exception - redirecting to index");
            throw new VerifyException(String.format("[verify] %s Parse json Exception", sid));
//            logger.error("[verify] {" + sid + "} Parse json Exception - redirecting to index");
//                    response.sendRedirect(response.encodeRedirectURL(WEB_APP_SITE_URL));
//                    return;
        }

        JSONObject vkResponse = (JSONObject) obj;

        if (vkResponse.get("error") != null) {
            // token get error

//            throw new VerifyException("[verify] {" + sid + "} Token request error - redirecting to index. Error: " + vkResponse.get("error") + ", description: " + vkResponse.get("error_description"));
            throw new VerifyException(String.format("[verify] %s Token request error. Error: %s, description: %s" + sid, vkResponse.get("error"), vkResponse.get("error_description")));
//            logger.error("[verify] {" + sid + "} Token request error - redirecting to index. Error: " + vkResponse.get("error") + ", description: " + vkResponse.get("error_description"));
//            response.sendRedirect(response.encodeRedirectURL(WEB_APP_SITE_URL));
//            return;
        }

        String accessToken = String.valueOf(vkResponse.get("access_token"));   // user token from vk (not used actually?)
        long vk_id = (long) vkResponse.get("user_id");  // vk user id

        // check vk_id
        if (vk_id <= 0) {
//            throw new VerifyException("[verify] {" + sid + "} Incorrect value of vk_id == " + vk_id + ". Redirecting to index");
            throw new VerifyException(String.format("[verify] %s Incorrect value of vk_id: %d", sid, vk_id));
//            logger.error("[verify] {" + sid + "} Incorrect value of vk_id == " + vk_id + ". Redirecting to index");
//            response.sendRedirect(response.encodeRedirectURL(WEB_APP_SITE_URL));
//            return;
        }

        // Database part - - - - - - - - -
        // vk_id is ok (present and correct), looking for user data in the Database
//        logger.info("[verify] {" + sid + "} looking for user from DB with vk_id = " + vk_id);
        logger.info(String.format("[verify] %s looking for user from DB with vk_id: %d", sid, vk_id));
        // get user with such id from DB
//        user = DatabaseManager.getUserFromDB(vk_id);

//        user = new UserDAO((DataSource) request.getServletContext().getAttribute("ds")).getEntityByVkId(vk_id);
        user = new UserDAO().getEntityByVkId(vk_id);

        if (user == null) {
            // no user found - make a new one

            // create object with vk_id and default fields
            user = new User(vk_id, 0, 0, 0, 0, 0);
//            logger.info("[verify] {" + sid + "} no user found in DB. A new user detected. Creating object: " + user);
            logger.info(String.format("[verify] %s no user found in DB. A new user detected. Creating object: %s", sid, user));

            // send a message to make admin happy
            HTMLHelper.notify("Новый посетитель:\nhttps://vk.com/id" + vk_id);
        }

        user.setToken(accessToken);
        user.setAppVersion(WEB_SITE_USER);
        session.setAttribute("user", user);

        logger.info("[verify] " + sid + " verification PASSED.");

        HTMLHelper.fillUserInfo(user);

//        response.sendRedirect(response.encodeRedirectURL(WEB_APP_VIEW_URL));
        result = "verify";
        return result;
    }
}
