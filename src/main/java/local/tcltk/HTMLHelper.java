package local.tcltk;

import local.tcltk.exceptions.DAOException;
import local.tcltk.model.dao.DatabaseManager;
import local.tcltk.model.dao.UserDAO;
import local.tcltk.model.dao.VkDAO;
import local.tcltk.model.domain.Building;
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

/**
 * Created by user on 28.06.2017.
 */
public class HTMLHelper {
    private static final Logger logger = Logger.getLogger(HTMLHelper.class);

/*
    public static String getVKResponse(String uri) {
        String result = null;

        try {
            result = Jsoup.connect(uri).ignoreContentType(true).validateTLSCertificates(false).execute().body();
        } catch (IOException e) {
            logger.error("[getVKResponse] Got Exception! - error request vk");
        }

        return result;
    }
*/

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
/*
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
*/


    /**
     * HTML part - section neighbours
     * @return
     */
/*
    public static String getNeighboursSectionHTML(User user) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE" +
                " building = '" + user.getBuilding() + "' AND" +
                " section = '" + user.getSection() + "' AND" +
                " floor = '" + user.getFloor() + "'";

        return getNeighboursHTML(user, sql);
    }
*/

    /**
     * HTML part - top floor neighbours
     * @return
     */
/*
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
*/

    /**
     * HTML part - bottom floor neighbours
     * @return
     */
/*
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
*/

/*
    public static String getRandomNeighboursHTML(User user) {

        int count = 2;
        if (user.getAppVersion() == MOBILE_APP_USER) {
            count = 3;
        }
        if (user.getAppVersion() == EMBEDDED_APP_USER || user.getAppVersion() == WEB_SITE_USER) {
            count = 7;
        }

//        List<User> neighbours = DatabaseManager.getRandomUsersFromDB(count);
        List<User> neighbours = null;
        try {
            neighbours = new UserDAO().getRandomNeighbours(count);
        } catch (DAOException e) {
            e.printStackTrace();
        }

        //fillNeighboursVKData(neighbours);
        new VkDAO().fillNeighboursVKData(neighbours);

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
*/


}
