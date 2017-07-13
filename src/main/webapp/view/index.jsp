<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="local.tcltk.User" %>
<%@ page import="static local.tcltk.Constants.SITE_URL" %>
<%@ page import="local.tcltk.HTMLHelper" %>
<%@ page import="static local.tcltk.Constants.SITE_TITLE" %>
<%@ page import="static local.tcltk.Constants.*" %>
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
    String use_flat = null;

    String sid = request.getSession().getId().substring(request.getSession().getId().length() - 3);
    session = request.getSession();
    user = (User) session.getAttribute("user");

    if (user == null) {
        // отсутствует объект пользователя - странно, попросим залогиниться через vk ещё раз
        logger.error("[view] {" + sid + "} no user object. Redirecting to index");

        response.sendRedirect(response.encodeRedirectURL(SITE_URL));
        return;
    }

    use_flat = request.getParameter("f");

    if ("1".equals(use_flat)) {
        user.setUseFlat(true);
    } else {
        user.setUseFlat(false);
    }

    logger.info("[view] {" + sid + "} got user object: " + user + ", show neighbours");

    // make html page:
%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title><%=SITE_TITLE%></title>

    <!-- favicon part -->
    <link rel="apple-touch-icon" sizes="180x180" href="<%=SITE_URL%>/apple-touch-icon.png?v=GvJ4ke7akz">
    <link rel="icon" type="image/png" sizes="32x32" href="<%=SITE_URL%>/favicon-32x32.png?v=GvJ4ke7akz">
    <link rel="icon" type="image/png" sizes="16x16" href="<%=SITE_URL%>/favicon-16x16.png?v=GvJ4ke7akz">
    <link rel="manifest" href="<%=SITE_URL%>/manifest.json?v=GvJ4ke7akz">
    <link rel="mask-icon" href="<%=SITE_URL%>/safari-pinned-tab.svg?v=GvJ4ke7akz" color="#5bbad5">
    <link rel="shortcut icon" href="<%=SITE_URL%>/favicon.ico?v=GvJ4ke7akz">
    <meta name="theme-color" content="#ffffff">
    <!-- end of favicon part -->


    <!--link rel="stylesheet" type="text/css" href="<%=STYLES_URL%>reset.css"-->
    <link rel="stylesheet" type="text/css" href="<%=STYLES_URL%>style.css?v=1.0">
    <script>
        function use_flat_func(){
            if(document.getElementById('use_flat').checked){
                window.location='<%=VIEW_URL%>?f=1';
                return false;
            } else {
                window.location='<%=VIEW_URL%>';
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
    <%=HTMLHelper.makeHTMLPage(user)%>
</body>
</html>
