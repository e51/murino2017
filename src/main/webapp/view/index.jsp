<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="local.tcltk.User" %>
<%@ page import="static local.tcltk.Constants.SITE_URL" %>
<%@ page import="local.tcltk.HTMLHelper" %>
<%@ page import="static local.tcltk.Constants.PROFILE_URL" %>
<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 05.07.2017
  Time: 0:20
  To change this template use File | Settings | File Templates.
--%>

<%
    Logger logger = Logger.getLogger("index.jsp");

    User user = null;

    session = request.getSession();
    user = (User) session.getAttribute("user");

    logger.info("[view] got user object: " + user);

    if (user == null) {
        // отсутствует объект пользователя - странно, попросим залогиниться через vk ещё раз
        logger.info("[view] no user object. Redirecting to index");

        response.sendRedirect(response.encodeRedirectURL(SITE_URL));
        return;
    }

    // Проверка полноты данных
    if (!user.checkCompleteData()) {
        logger.info("[view] incomplete user data - redirecting to profile");

        response.sendRedirect(response.encodeRedirectURL(PROFILE_URL));
        return;
    } else {
        logger.info("[view] data is ok, show neighbours");
    }


    // make html page:
%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Neighbours</title>
</head>
<body>
    <%=HTMLHelper.makeHTMLPage(user)%>
</body>
</html>
