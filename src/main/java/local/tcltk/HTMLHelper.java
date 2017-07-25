package local.tcltk;

import local.tcltk.model.DatabaseManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static local.tcltk.Constants.*;

/**
 * Created by user on 28.06.2017.
 */
public class HTMLHelper {
    private static final Logger logger = Logger.getLogger(HTMLHelper.class);

    public static String getVKResponse(String uri) {
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
    public static void notify(String message) {

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
     * Заполнить список соседей данными из vk
     * на вход подаётся список с Users
     * у каждого User обновляются данные из vk
     * @param users
     */
    private static void fillNeighboursVKData(List<User> users) {
        StringBuilder queryParams = new StringBuilder();

        queryParams.append("users.get?user_ids=");

        for (User user : users) {
            queryParams.append(user.getVk_id()).append(",");
        }

        logger.info("[fillNeighboursVKData] queryParams: " + queryParams.toString());

        queryParams.append("&fields=photo_200,photo_100,photo_50").append("&v=5.52");

        String json = getVKResponse(VK_API_CLASSIC_QUERY_URL + queryParams);

//        logger.info("[fillNeighboursVKData] total url: " + VK_API_CLASSIC_QUERY_URL + queryParams);
        logger.info("[fillNeighboursVKData] got json: " + json);

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

    /**
     * Fill user fields with VK data (last_name, first_name, photo)
     * @param user
     */
    public static void fillUserInfo(User user) {
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


    public static String getStat() {
        StringBuilder sb = new StringBuilder();

        for (Building building : STRUCTURE.values()) {
            sb.append("<B>Корпус " + building.getValue() + ": ").append(DatabaseManager.getUsersCountByBuilding(building.getValue())).append("</B><BR>");
            for (int i = building.getMaxFloor(); i > 0 ; i--) {
                sb.append("&nbsp;&nbsp;&nbsp;&nbsp;Этаж " + i + ": ").append(DatabaseManager.getUsersCountByBuildingAndFloor(building.getValue(), i)).append("<BR>");
            }
            sb.append("<BR>");
        }

        sb.append("<BR><BR><BR><BR>");

        return sb.toString();
    }

    /**
     *
     * @param user
     * @param sql
     * @return
     */
    public static String getNeighboursHTML(User user, String sql) {

        if (!user.isValid()) {
            return "";
        }

        // 1 - get users from Database
        List<User> neighbours = DatabaseManager.getNeighboursFromDB(user, sql);

        // 2 - make a query to the VK
        fillNeighboursVKData(neighbours);

        // 3 - show users
        StringBuilder sb = new StringBuilder();


        for (User usr : neighbours) {
            sb.append("<div id='block-neighbour'><a href='https://vk.com/id" + usr.getVk_id() + "' target='_blank'> <img src='" +
                    (user.getAppVersion() == WEB_SITE_USER ? usr.getVkPhoto100() : (user.getAppVersion() == EMBEDDED_APP_USER ? usr.getVkPhoto100() : usr.getVkPhoto100())) +
                    "' class='round-neighbour-photo'><BR>" +
                    "" + usr.getVkFirstName() + "<BR>" +
                    "" + usr.getVkLastName() + "</a><BR></div>");

        }

        if (neighbours.isEmpty()) {
            sb.append("пока нет соседей :(");
        }

        return sb.toString();
    }


    /**
     * HTML part - section neighbours
     * @return
     */
    public static String getNeighboursSectionHTML(User user) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE" +
                " building = '" + user.getBuilding() + "' AND" +
                " section = '" + user.getSection() + "' AND" +
                " floor = '" + user.getFloor() + "'";

        return getNeighboursHTML(user, sql);
    }

    /**
     * HTML part - top floor neighbours
     * @return
     */
    public static String getNeighboursTopHTML(User user) {
        String sql = "SELECT * FROM " +  TABLE_NAME + " WHERE" +
                " building = '" + user.getBuilding() + "' AND" +
                " section = '" + user.getSection() + "' AND" +
                " floor = '" + (user.getFloor() + 1) + "'";
//                " floor = '" + (user.getFloor() + 1) + "' AND" +
//                " flat = '" + user.getFlat() + "'";

        if (user.isUseFlat() && user.getFlat() != 0) {
//        if (user.getFlat() != 0) {
            sql = sql + " AND" +
                " flat = '" + user.getFlat() + "'";
        }

        return getNeighboursHTML(user, sql);
    }

    /**
     * HTML part - bottom floor neighbours
     * @return
     */
    public static String getNeighboursBottomHTML(User user) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE" +
                " building = '" + user.getBuilding() + "' AND" +
                " section = '" + user.getSection() + "' AND" +
                " floor = '" + (user.getFloor() - 1) + "'";
//                " floor = '" + (user.getFloor() - 1) + "' AND" +
//                " flat = '" + user.getFlat() + "'";

        if (user.isUseFlat() && user.getFlat() != 0) {
//        if (user.getFlat() != 0) {
            sql = sql + " AND" +
                    " flat = '" + user.getFlat() + "'";
        }

        return getNeighboursHTML(user, sql);
    }

    public static String getRandomNeighboursHTML(User user) {

        int count = 2;
        if (user.getAppVersion() == MOBILE_APP_USER) {
            count = 2;
        }
        if (user.getAppVersion() == EMBEDDED_APP_USER || user.getAppVersion() == WEB_SITE_USER) {
            count = 5;
        }

        List<User> neighbours = DatabaseManager.getRandomUsersFromDB(count);

        fillNeighboursVKData(neighbours);

        // 3 - show users
        StringBuilder sb = new StringBuilder();

        for (User usr : neighbours) {
            sb.append("<div class='block-neighbour-promo'><img src='" +
                    (user.getAppVersion() == WEB_SITE_USER ? usr.getVkPhoto100() : (user.getAppVersion() == EMBEDDED_APP_USER ? usr.getVkPhoto100() : usr.getVkPhoto100())) +
                    "' class='blur'><BR></div>");
        }

        if (neighbours.isEmpty()) {
            sb.append("пока нет соседей :(");
        }

        return sb.toString();
    }


}
