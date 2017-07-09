<%@ page import="local.tcltk.User" %>
<%@ page import="local.tcltk.model.DatabaseManager" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="static local.tcltk.Constants.*" %>
<%@ page import="static local.tcltk.HTMLHelper.getVKResponse" %>
<%@ page import="java.net.URLEncoder" %>
<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 28.06.2017
  Time: 19:40
  To change this template use File | Settings | File Templates.
--%>

<%
    Logger logger = Logger.getLogger("index.jsp");

    User user = null;
    String action = null;

    session = request.getSession();
    user = (User) session.getAttribute("user");
    action = request.getParameter("action");
    String sid = request.getSession().getId().substring(request.getSession().getId().length() - 3);

    logger.info("[profile] {" + sid + "} Start. got user object: " + user + ", got action: " + action);

    if (user == null) {
        // отсутствует объект пользователя - странно, попросим залогиниться через vk ещё раз
        logger.error("[profile] {" + sid + "} no user object. Redirecting to index");
        response.sendRedirect(response.encodeRedirectURL(SITE_URL));
        return;
    }

    if (user.isValid() && !"update".equals(action) && !"change".equals(action)) {
        logger.info("[profile] {" + sid + "} data is correct, no 'update' action, no 'change' action, redirecting to /view/");
        response.sendRedirect(response.encodeRedirectURL(VIEW_URL));
        return;
    }

//    if ("change".equals(action) && user.getUpdates() >= UPDATE_ATTEMPTS) {
    if (user.getUpdates() >= UPDATE_ATTEMPTS) {
        logger.error("[profile] {" + sid + "} MUST NOT BE HERE! No attempts left, redirecting to /view/");
        response.sendRedirect(response.encodeRedirectURL(VIEW_URL));
        return;
    }

    if ("update".equals(action)) {
        //обновление данных
        // update user data and redirect to main page

        logger.info("[profile] {" + sid + "} 'update' action - trying to update...");

        int building = 0;
        int section = 0;
        int floor = 0;
        int flat = 0;

        String strBuilding = request.getParameter("building");
        String strSection = request.getParameter("section");
        String strFloor = request.getParameter("floor");
        String strFlat = request.getParameter("flat");

        if (strBuilding == null || strBuilding != null && strBuilding.isEmpty()) {
//            logger.info("[profile] building is empty: " + strBuilding + ".");
            strBuilding = "0";
        }
        if (strSection == null || strSection != null && strSection.isEmpty()) {
//            logger.info("[profile] section is empty: " + strSection + ".");
            strSection = "0";
        }
        if (strFloor == null || strFloor != null && strFloor.isEmpty()) {
//            logger.info("[profile] floor is empty: " + strFloor + ".");
            strFloor = "0";
        }
        if (strFlat == null || strFlat != null && strFlat.isEmpty()) {
//            logger.info("[profile] flat is empty: " + strFlat + ".");
            strFlat = "0";
        }

        try {
            building = Integer.valueOf(strBuilding);
            section = Integer.valueOf(strSection);
            floor = Integer.valueOf(strFloor);
            flat = Integer.valueOf(strFlat);
        } catch (NumberFormatException e) {
            e.printStackTrace();

            logger.error("[profile] {" + sid + "} updating - NumberFormatException. Let's RETRY. (building=" + strBuilding + ", section=" + strSection + ", floor=" + strFloor + ", flat=" + strFlat + ")");
            response.sendRedirect(response.encodeRedirectURL(PROFILE_URL + "?action=change"));
            return;
        }

        // insert a new user record or already have one? -- should be changed to database request for user.id?
        // valid user can be only from DB, invalid - no record in DB
//        boolean insert = user.isValid() ? false : true;
        boolean insert = DatabaseManager.getUserFromDB(user.getVk_id()) != null ? false : true;

        logger.info("[profile] {" + sid + "} have to insert a new record? : " + insert);

        // current (old) values
        int oldBuilding = user.getBuilding();
        int oldSection = user.getSection();
        int oldFloor = user.getFloor();
        int oldFlat = user.getFlat();

        // set new values from form
        user.setBuilding(building);
        user.setSection(section);
        user.setFloor(floor);
        user.setFlat(flat);
        // and check them for valid
        if (user.isValid()) {
            // good data, let's update
            user.setUpdates(user.getUpdates() + 1);

            if (insert) {   // new user
                DatabaseManager.createNewUserDB(user);

                // send a message to make admin happy
                String message = "Новый жилец:\nhttps://vk.com/id" + user.getVk_id();

                // test sending message
                String contextParams = "messages.send?user_id=" + MY_VK_ID +
                        "&message=" + URLEncoder.encode(message, "UTF-8") +
                        "&access_token=" + MSG_PERM_TOKEN_MR_GREEN +
                        "&v=5.65";

                getVKResponse(VK_QUERY_URL + contextParams);

//                System.out.println("[msg test]: " + json);

            } else {        // update old user record
                DatabaseManager.updateUserInDB(user);
            }
        } else {    // bad data
            logger.error("[profile] {" + sid + "} updating has FAILED - BAD DATA. Let's RETRY. (building=" + building + ", section=" + section + ", floor=" + floor + ", flat=" + flat + ")");

            // return old values
            user.setBuilding(oldBuilding);
            user.setSection(oldSection);
            user.setFloor(oldFloor);
            user.setFlat(oldFlat);

            response.sendRedirect(response.encodeRedirectURL(PROFILE_URL + "?action=change"));
            return;
        }

        logger.info("[profile] {" + sid + "} updating is SUCCESS, redirecting to /profile/ for checking");
        response.sendRedirect(response.encodeRedirectURL(PROFILE_URL));
        return;
    }

//    if (action == null) {
        // новый пользователь
//    logger.info("[profile] no action - modify data");

//    }
    logger.info("[profile] {" + sid + "} modify data request. Fill the form");
%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title><%=SITE_TITLE%></title>
    <link rel="stylesheet" type="text/css" href="<%=SITE_URL%>styles.css">
</head>
<body>

<table width=100% height=100% class="text-normal">
    <tr>
        <td align=center valign=center>
            <div class="profile-container">
            <H2>Впервые у нас?</H2>
            <!--BR-->
                <p>Данные для заполнения брать из договора, где прописан адрес квартиры в формате 1.2.3.4<BR>
                    Где: 1 - корпус, 2 - секция, 3 - этаж, 4 - квартира<BR>
                    Если где-то фигурирует дробная секция, например: 1.5, то это означает: корпус 1, секция 5.<BR>
                </p>
            <form action='<%=PROFILE_URL%>' method='post' align=center>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Например:
                <p>&nbsp;Корпус<font color="red"><b>*</b></font>: <input type='text' name='building' value='<%=user.getBuilding()%>' size='9' class="input-style"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1</p>
                <p>&nbsp;Секция<font color="red"><b>*</b></font>: <input type='text' name='section' value='<%=user.getSection()%>' size='9' class="input-style"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;7</p>
                <p>&nbsp;&nbsp;&nbsp;&nbsp;Этаж<font color="red"><b>*</b></font>: <input type='text' name='floor' value='<%=user.getFloor()%>' size='9' class="input-style"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2</p>
                <!--p>Квартира: <input type='text' name='flat' value='<%=user.getFlat()%>' size='9' class="input-style"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2</p-->
                <p><input type='hidden' name='action' value='update'></p>
                <p><input type='submit' value=' Сохранить ' class='submit-profile'></p>
                <BR>
                <p>Введённые данные никому, кроме Вас, видны не будут.</p>
                <p>Поля отмеченные "<font color="red"><b>*</b></font>" обязательны для заполнения.</p>
                <!--p>Квартиру можно не указывать (влияет на отображение конкретно Ваших соседей сверху и снизу).</p-->
            </form>
            <BR>
            </div>
        </td>
    </tr>
</table>

</body>
</html>
