<%@ page import="static local.tcltk.Constants.VIEW_URL" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="static local.tcltk.Constants.SID_PATTERN" %>
<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 17.07.2017
  Time: 11:00
  To change this template use File | Settings | File Templates.
--%>
<%--
    Redirect from old URL: http://sosed.spb.ru/.../view/ to the new one: http://sosed.spb.ru/.../view
--%>
<%
    Logger logger = Logger.getLogger("view.jsp");
    String sid = String.format(SID_PATTERN, request.getSession().getId().substring(request.getSession().getId().length() - 3));

    logger.info(String.format("[view/] %s Redirecting from old /view/ to /view, remote address: %s", sid, request.getRemoteAddr()));
    response.sendRedirect(response.encodeRedirectURL(VIEW_URL));
%>

<!--%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

</body>
</html-->
