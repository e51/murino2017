package local.tcltk;

import local.tcltk.model.DatabaseManager;
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

    public static String getVKResponse(String uri) {

        String result = null;

        try {
            result = Jsoup.connect(uri).ignoreContentType(true).validateTLSCertificates(false).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }



    /**
     * HTML new user details page. If there is a new user, he should fill his data.
     * @return
     */
    public static String makeCreateUserPage(User user) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html>\n" +
                "\n" +
                "<table width=100% height=100%>\n" +
                "    <tr>\n" +
                "        <td align=center valign=center>\n" +
                "            Впервые у нас?<BR>\n" +
                "            <BR>\n" +
                "            <form action='/show' method='post' align=center>\n" +
                "                <p>Корпус: <input type='text' name='building' value='" + user.getBuilding() + "'></p>\n" +
                "                <p>Секция: <input type='text' name='section' value='" + user.getSection() + "'></p>\n" +
                "                <p>Этаж: <input type='text' name='floor' value='" + user.getFloor() + "'></p>\n" +
                "                <p>Квартира: <input type='text' name='flat' value='" + user.getFlat() + "'></p>\n" +
                "                <p><input type='hidden' name='action' value='update'></p>\n" +
                "                <p><input type='submit' value='Применить'></p>\n" +
                "            </form>\n" +
                "            <BR>\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "</table>\n" +
                "\n" +
                "</html>");

        return sb.toString();
    }


    private static void fillNeighboursVKData(List<User> users) {

        StringBuilder queryParams = new StringBuilder();

        queryParams.append("users.get?user_ids=");

        for (User user : users) {
            queryParams.append(user.getVk_id()).append(",");
        }

        queryParams.deleteCharAt(queryParams.length() - 1);

        System.out.println(queryParams.toString());

        queryParams.append("&fields=photo_50").append("&v=5.52");

//        String queryParams = "users.get?user_id=" + user.getVk_id() +
//                "&fields=photo_50" +
//                "&v=5.52";

        String json = getVKResponse(QUERY_URL + queryParams);

        System.out.println(QUERY_URL + queryParams);
        System.out.println(json);

//        {"response":[{"id":764013,"first_name":"Алексей","last_name":"Горбунов","photo_50":"https:\/\/pp.userapi.com\/c628627\/v628627013\/4c713\/JTxd5RCWR-k.jpg","hidden":1},
//            {"id":2509303,"first_name":"Полина","last_name":"Рощина","photo_50":"https:\/\/pp.userapi.com\/c5577\/v5577303\/20f\/r12OjYr7JMU.jpg"}]}

        JSONParser parser = new JSONParser();

        Object obj = null;
        try {
            obj = parser.parse(json);

            JSONObject responseMap = (JSONObject) obj;
            System.out.println("response: " + responseMap.get("response"));

            JSONArray array = (JSONArray) responseMap.get("response");
            System.out.println(array.size()); // >=1

//            System.out.println(array.get(0));
//            System.out.println();

            System.out.println("SIZE: " + array.size());
            for (int i = 0; i < array.size(); i++) {

                JSONObject elementMap = (JSONObject) array.get(i);

                String id = String.valueOf(elementMap.get("id"));

                for (User user : users) {
                    if (user.getVk_id().equals(id)) {
                        user.setVkPhoto(String.valueOf(elementMap.get("photo_50")));
                        user.setVkFirstName(String.valueOf(elementMap.get("first_name")));
                        user.setVkLastName(String.valueOf(elementMap.get("last_name")));
                        break;
                    }
                }

            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    /**
     * HTML main info page (neighbours information)
     * @return
     */
    public static String makeHTMLPage(User user) {
        StringBuilder sb = new StringBuilder();


//        TransportClient transportClient = HttpTransportClient.getInstance();
//        VkApiClient vk = new VkApiClient(transportClient);

//        System.out.println(user.getActor());

//        List<UserXtrCounters> response = null;
//        try {
//            response = vk.users().get(user.getActor()).userIds("6191031", "2509303").fields(UserField.STATUS).execute();
//        } catch (ApiException e) {
//            e.printStackTrace();
//        } catch (ClientException e) {
//            e.printStackTrace();
//        }
//        System.out.println("********");
//        String json = getVKResponse(QUERY_URL + "users.get?user_id=" + user.getVk_id() + "&v=5.52");
//        System.out.println(json);
//        System.out.println("********");

        String queryParams = "users.get?user_id=" + user.getVk_id() +
                "&fields=photo_50" +
                "&v=5.52";
        String json = getVKResponse(QUERY_URL + queryParams);

        // {"response":[{"id":210700286,"first_name":"Lindsey","last_name":"Stirling","photo_50":"https:\/\/pp.userapi.com\/c636821\/v636821286\/38a75\/Ay-bEZoJZw8.jpg"}]}

        JSONParser parser = new JSONParser();

        Object obj = null;
        try {
            obj = parser.parse(json);

            JSONObject responseMap = (JSONObject) obj;
            System.out.println("response: " + responseMap.get("response"));

            JSONArray array = (JSONArray) responseMap.get("response");
            System.out.println(array.size()); // 1

//            System.out.println(array.get(0));
//            System.out.println();

            JSONObject elementMap = (JSONObject) array.get(0);

            user.setVkFirstName(String.valueOf(elementMap.get("first_name")));
            user.setVkLastName(String.valueOf(elementMap.get("last_name")));
            user.setVkPhoto(String.valueOf(elementMap.get("photo_50")));

        } catch (ParseException e) {
            e.printStackTrace();
        }


//        List<UserXtrCounters> users = null;
//
//        try {
//            users = vk.users().get(user.getActor())
//                    .userIds("6191031", "2509303")
//                    .fields(UserField.SEX)
//                    .lang(Lang.EN)
//                    .execute();
//        } catch (ApiException e) {
//            e.printStackTrace();
//        } catch (ClientException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(users);
//        System.out.println("# " + users.get(0).getSex());
//        System.out.println("# " + users.get(0).getId());
//        System.out.println("# " + users.get(1).getSex());
//        System.out.println("# " + users.get(1).getId());

        // allow update user data on the info page
        String dataSection;

        if (true) {
            dataSection = "" +
                    "<a href='https://vk.com/id" + user.getVk_id() + "'> <img src='" + user.getVkPhoto() + "'><BR>" +
                    "" + user.getVkFirstName() + "<BR>" +
                    "" + user.getVkLastName() + "</a><BR>" +
                    "" +
                    "            <H1>Я живу:</H1><BR>\n" +
                    "            <BR>\n" +
                    "            <form action='/show' method='post' align=center>\n" +
                    "                <p>Корпус: <input type='text' name='building' value='" + user.getBuilding() + "'></p>\n" +
                    "                <p>Секция: <input type='text' name='section' value='" + user.getSection() + "'></p>\n" +
                    "                <p>Этаж: <input type='text' name='floor' value='" + user.getFloor() + "'></p>\n" +
                    "                <p>Квартира: <input type='text' name='flat' value='" + user.getFlat() + "'></p>\n" +
                    "                <p><input type='hidden' name='action' value='update'></p>\n" +
                    "                <p><input type='submit' value='Применить'></p>\n" +
                    "            </form>\n" +
                    "" +
                    "            <BR>\n" +
                    "            <form action='/show' method='post' align=center>\n" +
                    "                <p><input type='hidden' name='action' value='refresh'></p>\n" +
                    "                <p><input type='submit' value='Обновить'></p>\n" +
                    "            </form>\n" +
                    "" +
                    "";
        } else {
            dataSection = "" +
                    "<a href='https://vk.com/id" + user.getVk_id() + "'> <img src='" + user.getVkPhoto() + "'><BR>" +
                    "" + user.getVkFirstName() + "<BR>" +
                    "" + user.getVkLastName() + "</a><BR>" +
                    "" +
                    "" +
                    "            <H1>Я живу:</H1><BR>\n" +
                    "            <BR>\n" +
                    "            <p>Корпус: " + user.getBuilding() + "</p>\n" +
                    "            <p>Секция: " + user.getSection() + "</p>\n" +
                    "            <p>Этаж: " + user.getFloor() + "</p>\n" +
                    "            <p>Квартира: " + user.getFlat() + "</p>\n" +
                    "            <BR>\n";

        }

        sb.append("<html>\n" +
                "\n" +
                "<table width=100% height=100%>\n" +
                "    <tr>\n" +
                "        <td valign=center width=30%>\n" +
                "" + dataSection +
                "        </td>\n" +
                "        <td align=center valign=center width=70%>\n" +
                "            <table width=100% height=100%>\n" +
                "                <tr>\n" +
                "                    <td>\n" +
                "                        <H1>Соседи сверху:</H1><BR>\n" + getNeighboursTopHTML(user) +
                "                    </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td>\n" +
                "                        <H1>Соседи по площадке:</H1><BR>\n" + getNeighboursSectionHTML(user) +
                "                    </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td>\n" +
                "                        <H1>Соседи снизу:</H1><BR>\n" + getNeighboursBottomHTML(user) +
                "                    </td>\n" +
                "                </tr>\n" +
                "            </table>\n" +
                "        </td>\n" +
                "        <td>" +
                "Нас уже: " + DatabaseManager.getUsersCountByBuilding(1) +
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

    private static String getNeighboursHTML(User user, String sql) {
        // 1 - get users from Database
        List<User> neighbours = DatabaseManager.getNeighboursFromDB(user, sql);

        // 2 - make a query to the VK
        fillNeighboursVKData(neighbours);

        // 3 - show users
        StringBuilder sb = new StringBuilder();

        for (User usr : neighbours) {
            sb.append("<a href='https://vk.com/id" + usr.getVk_id() + "' target='_blank'> <img src='" + usr.getVkPhoto() + "'><BR>" +
                    "" + usr.getVkFirstName() + "<BR>" +
                    "" + usr.getVkLastName() + "</a><BR>");
        }

        return sb.toString();
    }


    /**
     * HTML part - section neighbours
     * @return
     */
    private static String getNeighboursSectionHTML(User user) {
        String sql = "SELECT * FROM neighbours WHERE" +
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


        // 1 - get users from Database
        String sql = "SELECT * FROM neighbours WHERE" +
                " building = '" + user.getBuilding() + "' AND" +
                " section = '" + user.getSection() + "' AND" +
                " floor = '" + (user.getFloor() + 1) + "' AND" +
                " flat = '" + user.getFlat() + "'";

        return getNeighboursHTML(user, sql);

    }

    /**
     * HTML part - bottom floor neighbours
     * @return
     */
    private static String getNeighboursBottomHTML(User user) {

        String sql = "SELECT * FROM neighbours WHERE" +
                " building = '" + user.getBuilding() + "' AND" +
                " section = '" + user.getSection() + "' AND" +
                " floor = '" + (user.getFloor() - 1) + "' AND" +
                " flat = '" + user.getFlat() + "'";

        return getNeighboursHTML(user, sql);

    }

}
