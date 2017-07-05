package local.tcltk;

import local.tcltk.model.DatabaseManager;
import org.apache.log4j.Logger;
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
 * Created by user on 27.06.2017.
 */
@WebServlet(name = "VKCheckServlet")
public class VKCheckServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(VKCheckServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = null;
        String accessToken = null;
        String vk_id = null;
        String code = null;

//        System.out.println("[vkcheck] Start");
        logger.info("[vkcheck] Start");

        HttpSession session = request.getSession();
//        user = (User) session.getAttribute("user");

        String error = request.getParameter("error");

//        System.out.println("[vkcheck] request.error: " + error);
        logger.info("[vkcheck] request.error: " + error);

        if (error == null) {
            logger.info("[vkcheck] no errors occured");
        }

        if (error != null) {
            // User cancelled auth - redirect to the index page

            logger.info("[vkcheck] auth error - Redirecting to index");

            String contextPath = SITE_URL;
            String contextParams = "";
            response.sendRedirect(response.encodeRedirectURL(contextPath + contextParams));

            return;
        }

        //?error=access_denied&error_reason=user_denied&error_description=User+denied+your+request

//        for (String name : response.getHeaderNames()) {
//            out.println(name);
//            out.println("<BR>");
//        }

//        out.println(request.getContextPath());
//        out.println("<BR>");
//        out.println(request.getRequestURI());
//        out.println("<BR>");
//        out.println(request.getRequestURL());
//        out.println("<BR>");
//        out.println("uid: " + request.getParameter("uid"));
//        out.println("<BR>");
//        out.println("first_name: " + request.getParameter("first_name"));
//        out.println("<BR>");
//        out.println("last_name: " + request.getParameter("last_name"));
//        out.println("<BR>");
//        out.println("photo: <img src='" + request.getParameter("photo") + "'>");
//        out.println("<BR>");
//        out.println("photo_rec: <img src='" + request.getParameter("photo_rec") + "'>");
//        out.println("<BR>");
//        out.println("hash: " + request.getParameter("hash"));
//        out.println("<BR>");
//        out.println("<a href='http://sosed.spb.ru/'>sosed.spb.ru</a>");

        code = request.getParameter("code");
        logger.info("[vkcheck] code: " + code);


        // Variant 1 - use VK API
//        TransportClient transportClient = HttpTransportClient.getInstance();
//        VkApiClient vk = new VkApiClient(transportClient);
////                    .userAuthorizationCodeFlow(VK_APP_ID, VK_CLIENT_SECRET, REDIRECT_URI, code)
//
//        UserAuthResponse authResponse = null;
//        try {
//            authResponse = vk.oauth()
//                    .userAuthorizationCodeFlow(VK_APP_ID, VK_CLIENT_SECRET, "http://sosed.spb.ru/verify", code)
//                    .execute();
//        } catch (OAuthException e) {
//            e.getRedirectUri();
//        } catch (ApiException e) {
//            e.printStackTrace();
//        } catch (ClientException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("--------");
//        System.out.println(authResponse.getUserId());
//        System.out.println(authResponse.getAccessToken());
//        System.out.println(authResponse.getError());
//        System.out.println("--------");
//
////        String vk_id = authResponse.getUserId();
//
//        UserActor actor = new UserActor(authResponse.getUserId(), authResponse.getAccessToken());
//
//        System.out.println(actor.getId());
//        System.out.println(actor.getAccessToken());
//        System.out.println("--------");


        // Variant 2 - use web + json

        // по коду необходимо запросить токен у vk
        String contextParams = "?client_id=" + VK_APP_ID +
                "&client_secret=" + VK_CLIENT_SECRET +
                "&redirect_uri=" + VK_REDIRECT_URI +
                "&code=" + code;

        String json = getVKResponse(VK_GET_TOKEN_URL + contextParams);

        logger.info("[vkcheck] VK response json: " + json);

        // for example:
        //{"access_token":"533bacf01e11f55b536a565b57531ac114461ae8736d6506a3", "expires_in":43200, '''user_id":66748}
        //{"error":"invalid_grant","error_description":"Code is expired."}

        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(json);
        } catch (ParseException e) {
            e.printStackTrace();

            logger.error("[vkcheck] ParseException - redirecting to index");
            response.sendRedirect(response.encodeRedirectURL(SITE_URL));
            return;

        }
        JSONObject vkResponse = (JSONObject) obj;

        error = String.valueOf(vkResponse.get("error"));
        if (!"null".equals(error)) {
            // Ошибка при получении токена, код просрочен
            logger.info("[vkcheck] Code is expired - redirecting to index. error: " + error);
            response.sendRedirect(response.encodeRedirectURL(SITE_URL));
            return;
        }

        accessToken = String.valueOf(vkResponse.get("access_token"));
        vk_id = String.valueOf(vkResponse.get("user_id"));

        logger.info("[vkcheck] access_token: " + accessToken);
        logger.info("[vkcheck] user_id: " + vk_id);


//        String contextPath = "https://oauth.vk.com/access_token";
//        String contextParams = "?client_id=6091606&client_secret=Ma&redirect_uri=http://sosed.spb.ru/vkcheck&code=" + request.getParameter("code") + "";
//        response.sendRedirect(response.encodeRedirectURL(contextPath + contextParams));


//        URL oracle = new URL(contextPath + contextParams);
//        BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
//
//        String inputLine;
//        while ((inputLine = in.readLine()) != null) {
//            System.out.println(inputLine);
//            out.println(inputLine + "<BR>");
//        }
//        in.close();


        // TODO: get vk id
//        String vk_id = request.getParameter("uid");
//        String vk_id = String.valueOf(actor.getId());
        if ("null".equals(vk_id)) {
            logger.info("[verify] vk_id == null. Redirecting to index");
//            String contextPath = "http://sosed.spb.ru/";
//            String contextParams = "";
            response.sendRedirect(response.encodeRedirectURL(SITE_URL));
            return;
        }

        // TODO: check hash - for variant 1 need to check hash


        // TODO: DB query

        // vk_id is ok (present)
        // looking for user data in the Database

        logger.info("[vkcheck] get user from DB");
        // get user with such id from DB
        user = DatabaseManager.getUserFromDB(vk_id);
        if (user == null) {
            // no user found - make a new one
            logger.info("[verify] no user found in DB. Creating a new user.");

            // create object with vk_id and default fields
            user = new User(vk_id, 0, 0, 0, 0);

            DatabaseManager.createNewUserDB(user);

            logger.info("[verify] User created: " + user);
        }

        user.setToken(accessToken);

//        user.setActor(actor);
        session.setAttribute("user", user);


/*
        // test sending message
        contextParams = "messages.send?user_id=" + user.getVk_id() +
                "&message=test" +
                "&access_token=" + MSG_TOKEN +
                "&v=5.65";

        json = getVKResponse(VK_QUERY_URL + contextParams);

        System.out.println("[msg test]: " + json);
*/


        logger.info("[vkcheck] Everything is OK. Redirecting to /show");
        String contextPath = PROFILE_URL;
        /*String */contextParams = "";
//        String contextPath = SITE_URL;
//        /*String */contextParams = "show?action=list";
        response.sendRedirect(response.encodeRedirectURL(contextPath + contextParams));

    }
}
