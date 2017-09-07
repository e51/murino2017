package local.tcltk.model.emb.mobile;

import local.tcltk.HTMLHelper;
import local.tcltk.model.dao.VkDAO;
import local.tcltk.model.domain.User;
import local.tcltk.model.Action;
import local.tcltk.exceptions.VerifyException;
import local.tcltk.model.dao.UserDAO;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;

import static local.tcltk.Constants.*;

public class VerifyActionMobileApp implements Action {
    private static final Logger logger = Logger.getLogger(VerifyActionMobileApp.class);

    private boolean isAuthPassed(HttpServletRequest request) throws NoSuchAlgorithmException, InvalidKeyException {
        StringBuilder data = new StringBuilder();

        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();

            if (!"sign".equals(param) && !"hash".equals(param) && !"api_result".equals(param)) {
                String value = request.getParameter(param);
                if (value != null) {
                    data.append(value);
                }
            }
        }

/*
        // version 2
        for (String param : request.getQueryString().split("&")) {
            String[] pair = param.split("=");
            if (pair.length == 2) {
                if (!"sign".equals(pair[0]) && !"hash".equals(pair[0]) && !"api_result".equals(pair[0])) {
                    data.append(pair[1]);
                }
//                System.out.println(pair[0] + " = " + pair[1]);
            }
        }
*/
        // compute hash from query parameters and app's secret key
        SecretKeySpec keySpec = new SecretKeySpec(VK_EMBED_COMMUNITY_APP_SECRET.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(keySpec);
        byte[] hashBytes = mac.doFinal(data.toString().getBytes());

        // compare hash and sign
        return Hex.encodeHexString(hashBytes).equals(request.getParameter("sign"));
    }

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
        logger.info(String.format("[verify] Query string: %s", request.getQueryString()));

        // get current session
        HttpSession session = request.getSession();

        try {
            if (!isAuthPassed(request)) {
                throw new VerifyException("Auth error, sign is wrong");
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            logger.error(String.format("[verify] Error while verifying: %s, %s", sid, e.getClass().getSimpleName(), e.getMessage()));
            throw e;
        } catch (VerifyException e) {
            logger.error(String.format("[verify] %s AUTH NOT PASSED! Sign is wrong", sid));
            throw e;
        }

        logger.info(String.format("[verify] %s Authentication PASSED, sign is OK", sid));

        // parse parameters
        String api_url = request.getParameter("api_url");
        String api_id = request.getParameter("api_id");
        String api_settings = request.getParameter("api_settings");
        String viewer_id = request.getParameter("viewer_id");
        String viewer_type = request.getParameter("viewer_type");
        String vk_sid = request.getParameter("sid");
        String secret = request.getParameter("secret");
        String access_token = request.getParameter("access_token");
        String user_id = request.getParameter("user_id");
        String group_id = request.getParameter("group_id");
        String is_app_user = request.getParameter("is_app_user");
        String auth_key = request.getParameter("auth_key");
        String language = request.getParameter("language");
        String parent_language = request.getParameter("parent_language");
        String is_secure = request.getParameter("is_secure");
        String ads_app_id = request.getParameter("ads_app_id");
        String referrer = request.getParameter("referrer");
        String lc_name = request.getParameter("lc_name");
        String sign = request.getParameter("sign");
        String hash = request.getParameter("hash");

        long vk_id = 0;
        try {
            vk_id = Long.valueOf(viewer_id);
        } catch (NumberFormatException e) {
            logger.error(String.format("[verify] %s Error parse viewer_id: %s. %s, %s", sid, viewer_id, e.getClass().getSimpleName(), e.getMessage()));
        }

        // check vk_id
        if (vk_id <= 0) {
            throw new VerifyException(String.format("Incorrect value of vk_id: %d, got viewer_id: %s", vk_id, viewer_id));
        }

        // Database part - - - - - - - - -
        // vk_id is ok (present and correct), looking for user data in the Database
//        logger.info("[verify] {" + sid + "} looking for user from DB with vk_id = " + vk_id);
        logger.info(String.format("[verify] %s looking for user from DB with vk_id: %d", sid, vk_id));
        // get user with such id from DB
        //user = DatabaseManager.getUserFromDB(vk_id);
        user = new UserDAO().getEntityByVkId(vk_id);

        if (user == null) {
            // no user found - make a new one

            // create object with vk_id and default fields
            user = new User(vk_id, 0, 0, 0, 0, 0);
//            logger.info("[verify] {" + sid + "} no user found in DB. A new user detected. Creating object: " + user);
            logger.info(String.format("[verify] %s no user found in DB. A new user detected. Creating object: %s", sid, user));

            // send a message to make admin happy
            //HTMLHelper.notify("Новый посетитель:\nhttps://vk.com/id" + vk_id);
            new VkDAO().notify("Новый посетитель:\nhttps://vk.com/id" + vk_id);
        }

        user.setToken(access_token);
        user.setAppVersion(MOBILE_APP_USER);
        session.setAttribute("user", user);

        logger.info(String.format("[verify] %s verification PASSED.", sid));

        //HTMLHelper.fillUserInfo(user);
        new VkDAO().fillUserInfo(user);

        result = "m-verify";
        return result;
    }
}
