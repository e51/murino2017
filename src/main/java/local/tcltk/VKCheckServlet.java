package local.tcltk;

import local.tcltk.model.DatabaseManager;
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
 * Created by user on 27.06.2017.
 */
@WebServlet(name = "VKCheckServlet")
public class VKCheckServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = null;
        String accessToken = null;
        String vk_id = null;
        String code = null;

        HttpSession session = request.getSession();
//        user = (User) session.getAttribute("user");

        String error = request.getParameter("error");

        System.out.println(error);
        if (error == null) {
            System.out.println("no errors occured");
        }

        if (error != null) {
            // User cancelled auth - redirect to the index page

            System.out.println("REDIRECTING");

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
        System.out.println("code: " + code);


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

        String contextParams = "?client_id=" + VK_APP_ID +
                "&client_secret=" + VK_CLIENT_SECRET +
                "&redirect_uri=" + VK_REDIRECT_URI +
                "&code=" + code;
        String query = VK_GET_TOKEN_URL + contextParams;

        String json = getVKResponse(query);

        System.out.println(json);

        //{"access_token":"533bacf01e11f55b536a565b57531ac114461ae8736d6506a3", "expires_in":43200, '''user_id":66748}
        //{"error":"invalid_grant","error_description":"Code is expired."}

        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(json);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject vkResponse = (JSONObject) obj;

        accessToken = String.valueOf(vkResponse.get("access_token"));
        vk_id = String.valueOf(vkResponse.get("user_id"));

        System.out.println("access_token: " + accessToken);
        System.out.println("user_id: " + vk_id);
        System.out.println();


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
        if (vk_id == null) {
            System.out.println("[verify] vk_id == null");
//            String contextPath = "http://sosed.spb.ru/";
//            String contextParams = "";
            response.sendRedirect(response.encodeRedirectURL(SITE_URL));

        }

        // TODO: check hash




        // TODO: DB query


        System.out.println("[verify] else - " + vk_id);
        // vk_id is ok (present)
        // looking for user data in the Database

        user = DatabaseManager.getUserFromDB(vk_id);
        if (user == null) {
            // no user found - make a new user
            System.out.println("[verify] no user found in DB");

            // create object with vk_id and default fields
            user = new User(vk_id, 0, 0, 0, 0);

            System.out.println("[verify] " + user);

            DatabaseManager.createNewUserDB(user);
        }

        user.setToken(accessToken);

//        user.setActor(actor);
        session.setAttribute("user", user);


        contextParams = "messages.send?user_id=" + user.getVk_id() +
                "&message=test" +
                "&access_token=" + user.getToken() +
                "&v=5.65";

        json = getVKResponse(QUERY_URL + contextParams);

        System.out.println(json);



        String contextPath = SITE_URL;
        /*String */contextParams = "show?action=list";
        response.sendRedirect(response.encodeRedirectURL(contextPath + contextParams));



    }
}
