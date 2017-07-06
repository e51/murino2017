<%@ page import="local.tcltk.HTMLHelper" %>
<%@ page import="local.tcltk.User" %>
<%@ page import="static local.tcltk.Constants.SITE_URL" %>
<%@ page import="local.tcltk.model.DatabaseManager" %>
<%@ page import="static local.tcltk.Constants.PROFILE_URL" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="local.tcltk.VKCheckServlet" %>
<%@ page import="static local.tcltk.Constants.VIEW_URL" %>
<%@ page import="static local.tcltk.Constants.*" %><%--
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

    logger.info("[profile] got user object: " + user);
    logger.info("[profile] got action: " + action);

    if (user == null) {
        // отсутствует объект пользователя - странно, попросим залогиниться через vk ещё раз
        logger.error("[profile] no user object. Redirecting to index");

        response.sendRedirect(response.encodeRedirectURL(SITE_URL));
        return;
    }

    if (user.checkCompleteData() && !"update".equals(action) && !"change".equals(action)) {
        logger.info("[profile] data is ok, no update action, no change action, redirecting to /view/");

        response.sendRedirect(response.encodeRedirectURL(VIEW_URL));
        return;

    }

    if ("change".equals(action) && user.getUpdates() >= UPDATE_ATTEMPTS) {
        logger.info("[profile] change action, but no attempts, redirecting to /view/");

        response.sendRedirect(response.encodeRedirectURL(VIEW_URL));
        return;
    }

//    if (!user.checkCompleteData()) {
//        // необходимо заполнить данные, прежде чем продолжать
//        logger.info("[profile] incomplete data detected");
//
//
////        htmlPage = HTMLHelper.makeCreateUserPage(user);
//    } else if (!"update".equals(action)) {
//        // всё заполнено - переслать на страницу просмотра
//
//        logger.info("[profile] data is ok, no update action, redirecting to /view");
//
//        response.sendRedirect(response.encodeRedirectURL(VIEW_URL));
//        return;
//    }
%>


<%
    if ("update".equals(action)) {
        //обновление данных
        // update user data and redirect to main page

        logger.info("[profile] action == 'update' - updating");

        int building = 0;
        int section = 0;
        int floor = 0;
        int flat = 0;

        String strBuilding = request.getParameter("building");
        String strSection = request.getParameter("section");
        String strFloor = request.getParameter("floor");
        String strFlat = request.getParameter("flat");

        if (strBuilding == null || strBuilding != null && strBuilding.isEmpty()) {
            logger.info("[profile] flat is empty: " + strBuilding + ".");
            strBuilding = "0";
        }
        if (strSection == null || strSection != null && strSection.isEmpty()) {
            logger.info("[profile] flat is empty: " + strSection + ".");
            strSection = "0";
        }
        if (strFloor == null || strFloor != null && strFloor.isEmpty()) {
            logger.info("[profile] flat is empty: " + strFloor + ".");
            strFloor = "0";
        }
        if (strFlat == null || strFlat != null && strFlat.isEmpty()) {
            logger.info("[profile] flat is empty: " + strFlat + ".");
            strFlat = "0";
        }

        try {
//            building = Integer.valueOf(request.getParameter("building"));
//            section = Integer.valueOf(request.getParameter("section"));
//            floor = Integer.valueOf(request.getParameter("floor"));
//            flat = Integer.valueOf(request.getParameter("flat"));
            building = Integer.valueOf(strBuilding);
            section = Integer.valueOf(strSection);
            floor = Integer.valueOf(strFloor);
            flat = Integer.valueOf(strFlat);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            logger.error("[profile] updating - NumberFormatException");

            response.sendRedirect(response.encodeRedirectURL(PROFILE_URL));
            return;
        }

        user.setBuilding(building);
        user.setSection(section);
        user.setFloor(floor);
        user.setFlat(flat);
        if (user.checkCompleteData()) {
            user.setUpdates(user.getUpdates() + 1);
            DatabaseManager.updateUserInDB(user);

        } else {
            user.setBuilding(0);
            user.setSection(0);
            user.setFloor(0);
            user.setFlat(0);

//            user.setUpdates(user.getUpdates());
        }

//        DatabaseManager.updateUserInDB(user);

        logger.info("[profile] updating is OK, redirecting to /profile to check");

        response.sendRedirect(response.encodeRedirectURL(PROFILE_URL));
        return;
    }

//    if (action == null) {
        // новый пользователь
    logger.info("[profile] no action - a new user?");

//    }

    logger.info("[profile] incomplete data detected");


%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title><%=SITE_TITLE%></title>
</head>
<body>

<table width=100% height=100%>
    <tr>
        <td align=center valign=center>
            <H3>Впервые у нас?</H3>
            <BR>
            <form action='<%=PROFILE_URL%>' method='post' align=center>
                <p>Корпус*: <input type='text' name='building' value='<%=user.getBuilding()%>'></p>
                <p>Секция*: <input type='text' name='section' value='<%=user.getSection()%>'></p>
                <p>Этаж*: <input type='text' name='floor' value='<%=user.getFloor()%>'></p>
                <p>Квартира: <input type='text' name='flat' value='<%=user.getFlat()%>'></p>
                <p><input type='hidden' name='action' value='update'></p>
                <p><input type='submit' value='Применить'></p>
                <BR>
                <p>Введённые данные никому, кроме вас, не будут видны. Используются только для определения соседства.</p>
                <p>Поля отмеченные "*" обязательны для заполнения.</p>
                <p>Квартиру можно не указывать, но тогда не будут отображаться соседи сверху и снизу.</p>
                <p>Изменить полностью введённые данные после сохранения самостоятельно нельзя.</p>
            </form>
            <BR>
        </td>
    </tr>
</table>


</body>
</html>
