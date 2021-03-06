package local.tcltk.model.dao;

import local.tcltk.model.domain.User;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import static local.tcltk.Constants.*;

public class VkDAO {
    private static final Logger logger = Logger.getLogger(VkDAO.class);

    public String getVKResponse(String uri) {
        String result = null;

        try {
            result = Jsoup.connect(uri).ignoreContentType(true).validateTLSCertificates(false).execute().body();
        } catch (IOException e) {
            logger.error("[getVKResponse] Got Exception! - error request vk");
        }

        return result;
    }

    /**
     * Send notify message to admin
     * @param message
     */
    public void notify(String message) {

        //don't send for tests
        if (!SITE_PORT.isEmpty()) {
            return;
        }

        String contextParams = null;
        try {
            contextParams = "messages.send?user_id=" + ADMIN_VK_ID +
                    "&message=" + URLEncoder.encode(message, "UTF-8") +
                    "&access_token=" + NOTIFY_ACC_PERM_TOKEN +
                    "&v=5.65";
        } catch (UnsupportedEncodingException e) {
            logger.error("[notify] Error notify admin - UnsupportedEncodingException");
        }

        getVKResponse(VK_API_CLASSIC_QUERY_URL + contextParams);
    }


    /**
     * Fill user fields with VK data (last_name, first_name, photo)
     * @param user
     */
    public void fillUserInfo(User user) {
        String queryParams = "users.get?user_id=" + user.getVk_id() +
                "&fields=photo_200,photo_100,photo_50" +
                "&v=5.52";
//        String queryParams = String.format("users.get?user_id=%d&fields=photo_200&v=5.52", user.getVk_id());

        String json = getVKResponse(VK_API_CLASSIC_QUERY_URL + queryParams);
        // {"response":[{"id":210700286,"first_name":"Lindsey","last_name":"Stirling","photo_50":"https:\/\/pp.userapi.com\/c636821\/v636821286\/38a75\/Ay-bEZoJZw8.jpg"}]}

        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(json);

            JSONObject responseMap = (JSONObject) obj;

//            logger.info("[HTMLHelper.fillUserInfo] user id response: " + responseMap.get("response"));

            JSONArray array = (JSONArray) responseMap.get("response");

//            logger.info("[HTMLHelper.fillUserInfo] user id response array size should be 1: " + array.size()); // 1

            JSONObject elementMap = (JSONObject) array.get(0);

            user.setVkFirstName(String.valueOf(elementMap.get("first_name")));
            user.setVkLastName(String.valueOf(elementMap.get("last_name")));
            user.setVkPhoto(String.valueOf(elementMap.get("photo_200")));
            user.setVkPhoto200(String.valueOf(elementMap.get("photo_200")));
            user.setVkPhoto100(String.valueOf(elementMap.get("photo_100")));
            user.setVkPhoto50(String.valueOf(elementMap.get("photo_50")));

        } catch (NullPointerException e) {
            logger.error("[fillUserInfo] Got NULL answer from vk, json = null");
        } catch (ParseException e) {
            logger.error(String.format("[fillUserInfo] user id request error: %s, %s", e.getClass().getSimpleName(), e.getMessage()));
        }
    }

    /**
     * Заполнить список соседей данными из vk
     * на вход подаётся список с Users
     * у каждого User обновляются данные из vk
     * @param users
     */
    public void fillNeighboursVKData(List<User> users) {
        StringBuilder queryParams = new StringBuilder();

        queryParams.append("users.get?user_ids=");

        for (User user : users) {
            queryParams.append(user.getVk_id()).append(",");
        }

        logger.info("[fillNeighboursVKData] queryParams: " + queryParams.toString());

        queryParams.append("&fields=photo_200,photo_100,photo_50").append("&v=5.52");

        String json = getVKResponse(VK_API_CLASSIC_QUERY_URL + queryParams);

//        logger.info("[fillNeighboursVKData] got json: " + json);

        // for example
//        {"response":[{"id":764013,"first_name":"Алексей","last_name":"Горбунов","photo_50":"https:\/\/pp.userapi.com\/c628627\/v628627013\/4c713\/JTxd5RCWR-k.jpg","hidden":1},
//            {"id":2509303,"first_name":"Полина","last_name":"Рощина","photo_50":"https:\/\/pp.userapi.com\/c5577\/v5577303\/20f\/r12OjYr7JMU.jpg"}]}

        // parse result
        JSONParser parser = new JSONParser();

        Object obj = null;
        try {
            obj = parser.parse(json);

            JSONObject responseMap = (JSONObject) obj;
//            logger.info("response: " + responseMap.get("response"));

            JSONArray array = (JSONArray) responseMap.get("response");
            logger.info("[fillNeighboursVKData] response array size: " + array.size()); // >=1

            for (int i = 0; i < array.size(); i++) {

                JSONObject elementMap = (JSONObject) array.get(i);

                long id = (long) elementMap.get("id");

                for (User user : users) {
                    if (user.getVk_id() == id) {
                        user.setVkPhoto(String.valueOf(elementMap.get("photo_100")));
                        user.setVkFirstName(String.valueOf(elementMap.get("first_name")));
                        user.setVkLastName(String.valueOf(elementMap.get("last_name")));
                        user.setVkPhoto200(String.valueOf(elementMap.get("photo_200")));
                        user.setVkPhoto100(String.valueOf(elementMap.get("photo_100")));
                        user.setVkPhoto50(String.valueOf(elementMap.get("photo_50")));

                        logger.info("[fillNeighboursVKData] got neighbour: " + user); // >=1
                        break;
                    }
                }
            }
        } catch (NullPointerException e) {
            logger.error("[fillNeighboursVKData] Got NULL answer from vk, json = null");
        } catch (ParseException e) {
//            e.printStackTrace();
            logger.error(String.format("[fillNeighboursVKData] Parse error: %s, %s", e.getClass().getSimpleName(), e.getMessage()));
        }
    }

}
