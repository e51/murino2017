package local.tcltk;

import local.tcltk.model.DatabaseManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static local.tcltk.Constants.*;
import static local.tcltk.HTMLHelper.getVKResponse;

/**
 * Check vk authentication (parse vk response - get code, request token)
 * If auth is ok - create user in Database and User object, make a session with it, redirect to the view page
 * If auth has failed - redirect back to the index page
 * Created by user on 27.06.2017.
 */
@WebServlet(name = "VKCheckServlet")
public class VKCheckServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(VKCheckServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = null;           // user object
        String accessToken = null;  // user token from vk (not used actually?)
        String code = null;         // vk code for token request
        long vk_id = 0;             // vk user id

        logger.info("[vkcheck] Start checking");

        // get current session
        HttpSession session = request.getSession();

        // check vk answer for error (parse GET parameters)
        String error = request.getParameter("error");

        logger.info("[vkcheck] request.error: " + error);

        if (error != null) {
            // Got auth error - redirect to the index page
            logger.info("[vkcheck] auth error - Redirecting to index");
            response.sendRedirect(response.encodeRedirectURL(SITE_URL));
            return;
        }

        // get code from vk
        code = request.getParameter("code");
        logger.info("[vkcheck] code: " + code);

        // Variant 2 - use web + json
        // use code for token request
        String contextParams = "?client_id=" + VK_APP_ID +
                "&client_secret=" + VK_CLIENT_SECRET +
                "&redirect_uri=" + VK_REDIRECT_URI +
                "&code=" + code;

        String json = getVKResponse(VK_GET_TOKEN_URL + contextParams);

        logger.info("[vkcheck] VK response json: " + json);
        // possible json-answers are:
        //{"access_token":"533bacf01e11f55b536a565b57531ac114461ae8736d6506a3", "expires_in":43200, "user_id":66748}
        //{"error":"invalid_grant","error_description":"Code is expired."}

        // parse json
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(json);
        } catch (ParseException e) {
            e.printStackTrace();

            logger.error("[vkcheck] Parse json Exception - redirecting to index");
            response.sendRedirect(response.encodeRedirectURL(SITE_URL));
            return;
        }

        JSONObject vkResponse = (JSONObject) obj;

//        error = String.valueOf(vkResponse.get("error"));
//        if (!"null".equals(error)) {
        if (vkResponse.get("error") != null) {
            // Ошибка при получении токена
            logger.error("[vkcheck] Token request error - redirecting to index. Error: " + vkResponse.get("error") + ", description: " + vkResponse.get("error_description"));
            response.sendRedirect(response.encodeRedirectURL(SITE_URL));
            return;
        }

        accessToken = String.valueOf(vkResponse.get("access_token"));
        vk_id = (long) vkResponse.get("user_id");

        logger.info("[vkcheck] access_token: " + accessToken);
        logger.info("[vkcheck] user_id: " + vk_id);

        // check vk_id
        if (vk_id <= 0) {
            logger.info("[verify] Incorrect value of vk_id == " + vk_id + ". Redirecting to index");
            response.sendRedirect(response.encodeRedirectURL(SITE_URL));
            return;
        }

        // Database part ---
        // vk_id is ok (present and correct), looking for user data in the Database
        logger.info("[vkcheck] get user from DB");
        // get user with such id from DB
        user = DatabaseManager.getUserFromDB(vk_id);
        if (user == null) {
            // no user found - make a new one
            logger.info("[verify] no user found in DB. Creating a new user");

            // create object with vk_id and default fields
            user = new User(vk_id, 0, 0, 0, 0, 0);

            DatabaseManager.createNewUserDB(user);

            logger.info("[verify] User created: " + user);
        }

        user.setToken(accessToken);

        session.setAttribute("user", user);

        logger.info("[vkcheck] Everything is OK. Redirecting to /view/");
        response.sendRedirect(response.encodeRedirectURL(VIEW_URL));
    }
}
