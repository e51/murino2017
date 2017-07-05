<%@ page import="local.tcltk.HTMLHelper" %>
<%@ page import="local.tcltk.User" %>
<%@ page import="static local.tcltk.Constants.SITE_URL" %>
<%@ page import="local.tcltk.model.DatabaseManager" %>
<%@ page import="static local.tcltk.Constants.PROFILE_URL" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="local.tcltk.VKCheckServlet" %><%--
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
        logger.info("[profile] no user object. Redirecting to index");

        response.sendRedirect(response.encodeRedirectURL(SITE_URL));
        return;

    }

    if (!user.checkCompleteData()) {
        // необходимо заполнить данные, прежде чем продолжать
        logger.info("[profile] uncomplete data detected");


//        htmlPage = HTMLHelper.makeCreateUserPage(user);
    } else if (!"update".equals(action)) {
        // всё заполнено - переслать на страницу просмотра

        logger.info("[profile] data is ok, no update action, redirecting to /show");

        String contextPath = SITE_URL;
        String contextParams = "view/";
//        String contextParams = "show?action=list";
        response.sendRedirect(response.encodeRedirectURL(contextPath + contextParams));
        return;
//        htmlPage = HTMLHelper.makeHTMLPage(user);
    }

%>



<%
    if (action == null) {
        // новый пользователь
        logger.info("[profile] no action - a new user?");


    }

    if ("update".equals(action)) {
        //обновление данных
        // update user data and redirect to main page
        logger.info("[profile] action == 'update' - updating");


        int building = 0;
        int section = 0;
        int floor = 0;
        int flat = 0;

        try {

            building = Integer.valueOf(request.getParameter("building"));
            section = Integer.valueOf(request.getParameter("section"));
            floor = Integer.valueOf(request.getParameter("floor"));
            flat = Integer.valueOf(request.getParameter("flat"));

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

        DatabaseManager.updateUserInDB(user);

        logger.info("[profile] updating is OK, redirecting to /show");

        String contextPath = SITE_URL;
        String contextParams = "profile/";
//        String contextParams = "show?action=list";
        response.sendRedirect(response.encodeRedirectURL(contextPath + contextParams));
        return;


    }


%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Profile</title>
</head>
<body>

<table width=100% height=100%>
    <tr>
        <td align=center valign=center>
            Впервые у нас?<BR>
            <BR>
            <form action='<%=SITE_URL + "profile/"%>' method='post' align=center>
                <p>Корпус: <input type='text' name='building' value='<%=user.getBuilding()%>'></p>
                <p>Секция: <input type='text' name='section' value='<%=user.getSection()%>'></p>
                <p>Этаж: <input type='text' name='floor' value='<%=user.getFloor()%>'></p>
                <p>Квартира: <input type='text' name='flat' value='<%=user.getFlat()%>'></p>
                <p><input type='hidden' name='action' value='update'></p>
                <p><input type='submit' value='Применить'></p>
            </form>
            <BR>
        </td>
    </tr>
</table>


</body>
</html>
