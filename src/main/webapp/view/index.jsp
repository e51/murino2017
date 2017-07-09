<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="local.tcltk.User" %>
<%@ page import="static local.tcltk.Constants.SITE_URL" %>
<%@ page import="local.tcltk.HTMLHelper" %>
<%@ page import="static local.tcltk.Constants.PROFILE_URL" %>
<%@ page import="static local.tcltk.Constants.SITE_TITLE" %>
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

    String sid = request.getSession().getId().substring(request.getSession().getId().length() - 3);
    session = request.getSession();
    user = (User) session.getAttribute("user");

    if (user == null) {
        // отсутствует объект пользователя - странно, попросим залогиниться через vk ещё раз
        logger.error("[view] {" + sid + "} no user object. Redirecting to index");

        response.sendRedirect(response.encodeRedirectURL(SITE_URL));
        return;
    }

    logger.info("[view] {" + sid + "} got user object: " + user + ", show neighbours");

    // make html page:
%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title><%=SITE_TITLE%></title>
    <link rel="stylesheet" type="text/css" href="<%=SITE_URL%>styles.css">
</head>
<body>
    <%=HTMLHelper.makeHTMLPage(user)%>
</body>
</html>
