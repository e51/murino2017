package local.tcltk;

import local.tcltk.model.DatabaseManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;

import java.io.IOException;
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
            e.printStackTrace();
            logger.error("[getVKResponse] error request vk");
        }

        return result;
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

        queryParams.append("&fields=photo_100").append("&v=5.52");

        String json = getVKResponse(VK_QUERY_URL + queryParams);

//        logger.info("[fillNeighboursVKData] total url: " + VK_QUERY_URL + queryParams);
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
                        break;
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error("[fillNeighboursVKData] error - ParseException");
        }
    }

    /**
     * HTML main info page (neighbours information)
     * @return
     */
    public static String makeHTMLPage(User user) {
        StringBuilder sb = new StringBuilder();

        String neighbours = "Соседи по площадке:";

        String queryParams = "users.get?user_id=" + user.getVk_id() +
                "&fields=photo_200" +
                "&v=5.52";
        String json = getVKResponse(VK_QUERY_URL + queryParams);

        // {"response":[{"id":210700286,"first_name":"Lindsey","last_name":"Stirling","photo_50":"https:\/\/pp.userapi.com\/c636821\/v636821286\/38a75\/Ay-bEZoJZw8.jpg"}]}

        JSONParser parser = new JSONParser();

        Object obj = null;
        try {
            obj = parser.parse(json);

            JSONObject responseMap = (JSONObject) obj;

            logger.info("[view] user id response: " + responseMap.get("response"));

            JSONArray array = (JSONArray) responseMap.get("response");

            logger.info("[view] user id response array size should be 1: " + array.size()); // 1

            JSONObject elementMap = (JSONObject) array.get(0);

            user.setVkFirstName(String.valueOf(elementMap.get("first_name")));
            user.setVkLastName(String.valueOf(elementMap.get("last_name")));
            user.setVkPhoto(String.valueOf(elementMap.get("photo_200")));

        } catch (ParseException e) {
            e.printStackTrace();
            logger.error("[view] user id request error - ParseException");
        }

//        if (!user.isValid()) {
//
//            logger.info("[view] incomplete user data");
//            neighbours = "Без определённого места жительства:";
//        }

        String dataSection;

        if (!true) {
            dataSection = "" +
                    "            <form action='" + PROFILE_URL + "' method='post' align=center>\n" +
                    "                <p>Корпус: <input type='text' name='building' value='" + user.getBuilding() + "'></p>\n" +
                    "                <p>Секция: <input type='text' name='section' value='" + user.getSection() + "'></p>\n" +
                    "                <p>Этаж: <input type='text' name='floor' value='" + user.getFloor() + "'></p>\n" +
                    "                <!--p>Квартира: <input type='text' name='flat' value='" + user.getFlat() + "'></p-->\n" +
                    "                <p><input type='hidden' name='action' value='update'></p>\n" +
                    "                <p><input type='submit' value='Применить'></p>\n" +
                    "            </form>\n" +
                    "";
        } else {
            dataSection = "" +
                    "            <p class='text-normal'>Корпус: " + user.getBuilding() + "</p>\n" +
                    "            <p class='text-normal'>Секция: " + user.getSection() + "</p>\n" +
                    "            <p class='text-normal'>Этаж: " + user.getFloor() + "</p>\n" +
                    "            <!--p class='text-normal'>Квартира: " + user.getFlat() + "</p-->\n" +
                    "            <BR>\n" +
                    "            <BR>\n";



            if (UPDATE_ATTEMPTS - user.getUpdates() > 0) {

                String raz = "раз";
                if (UPDATE_ATTEMPTS - user.getUpdates() == 2 || UPDATE_ATTEMPTS - user.getUpdates() == 3) {
                    raz = "раза";
                }

                String buttonText = "Вселиться (1 раз)";
//                if (user.getUpdates() > 0) {
//                    buttonText = "Изменить (" + (UPDATE_ATTEMPTS - user.getUpdates()) + " " +  raz + ")";
//                }

                dataSection = dataSection +
                        "            <form action='" + PROFILE_URL + "' method='post' align=center>\n" +
                        "                <p><input type='hidden' name='action' value='change'></p>\n" +
                        "                <p><input type='submit' value='" + buttonText + "' class='submit'></p>\n" +
                        "            </form>\n" +
                        "";
            }
        }

        sb.append("<html>\n" +
                "\n" +
                "<table width=100% height=100%>\n" +
                "    <tr>\n" +
                "        <td align=center valign=center width=30%>\n" +
                "            <a href='https://vk.com/id" + user.getVk_id() + "'> <img src='" + user.getVkPhoto() + "' class='round-me'><BR>" +
                "" + user.getVkFirstName() + "<BR>" +
                "" + user.getVkLastName() + "</a><BR><BR>" +
                "            <H1>Я здесь:</H1><BR>\n" +
                "" + dataSection +
                "<BR><BR><BR>" +
                "Есть вопросы?<BR><a href='https://vk.com/id6191031' target=_blank>Пишите</a>" +
                "        </td>\n" +
                "        <td align=center valign=center width=50%>\n" +
                "            <table width=100% height=100%>\n" +
                "                <tr>\n" +
                "                    <td valign='top'>\n" +
                "                        <H1>Соседи сверху:</H1><BR>\n" + getNeighboursTopHTML(user) +
                "                    </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td valign='top'>\n" +
                "                        <H1>" + neighbours + "</H1><BR><div id='container'>\n" + getNeighboursSectionHTML(user) +
                "                    </div></td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td valign='top'>\n" +
                "                        <H1>Соседи снизу:</H1><BR>\n" + getNeighboursBottomHTML(user) +
                "                    </td>\n" +
                "                </tr>\n" +
                "            </table>\n" +
                "        </td>\n" +
                "        <td width=20% valign='top' align='left'>" +
                "<BR><BR><p class='text-total'><H3>Нас уже: " + DatabaseManager.getUsersCountByBuilding(0) + "</H3></p>" +
                "<BR>" + getStat() +
                "" +
                "        </td>" +
                "    </tr>\n" +
                "</table>\n" +
                "\n" +
                "" +
                "" +
                "" +
                "</html>\n" +
                "");

        return sb.toString();
    }

    private static String getStat() {
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < INFRASTRUCTURE.length; i++ ) {
            sb.append("<B>Корпус " + i + ": ").append(DatabaseManager.getUsersCountByBuilding(i)).append("</B><BR>");
            for (int j = INFRASTRUCTURE[i]; j > 0 ; j--) {
                sb.append("&nbsp;&nbsp;&nbsp;&nbsp;Этаж " + j + ": ").append(DatabaseManager.getUsersCountByBuildingAndFloor(i, j)).append("<BR>");
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
    private static String getNeighboursHTML(User user, String sql) {

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
            sb.append("<div id='block'><a href='https://vk.com/id" + usr.getVk_id() + "' target='_blank'> <img src='" + usr.getVkPhoto() + "' class='round-you'><BR>" +
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
    private static String getNeighboursSectionHTML(User user) {
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
    private static String getNeighboursTopHTML(User user) {
        String sql = "SELECT * FROM " +  TABLE_NAME + " WHERE" +
                " building = '" + user.getBuilding() + "' AND" +
                " section = '" + user.getSection() + "' AND" +
                " floor = '" + (user.getFloor() + 1) + "'";
//                " floor = '" + (user.getFloor() + 1) + "' AND" +
//                " flat = '" + user.getFlat() + "'";

        return getNeighboursHTML(user, sql);
    }

    /**
     * HTML part - bottom floor neighbours
     * @return
     */
    private static String getNeighboursBottomHTML(User user) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE" +
                " building = '" + user.getBuilding() + "' AND" +
                " section = '" + user.getSection() + "' AND" +
                " floor = '" + (user.getFloor() - 1) + "'";
//                " floor = '" + (user.getFloor() - 1) + "' AND" +
//                " flat = '" + user.getFlat() + "'";

        return getNeighboursHTML(user, sql);
    }


}
