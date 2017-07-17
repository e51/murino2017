<%@ page import="static local.tcltk.Constants.*" %>
<%@ page import="org.apache.log4j.Logger" %>
<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 17.07.2017
  Time: 9:48
  To change this template use File | Settings | File Templates.
--%>
<%
    Logger logger = Logger.getLogger("auth.jsp");

//    session.invalidate();
    session.removeAttribute("user");

    String sid = String.format(SID_PATTERN, request.getSession().getId().substring(request.getSession().getId().length() - 3));
//    logger.info(String.format("[auth.jsp] %s remote address: %s", sid, request.getRemoteAddr()));

    String contextPath = VK_GET_AUTH_URL;
    String contextParams = "?client_id=" + VK_APP_ID +
            "&redirect_uri=" + VK_REDIRECT_URI +
            "&display=page" +
            "&response_type=code" +
//            "&scope=notify" +
            "&scope=" +
            "&v=5.65";

    logger.info(String.format("[auth.jsp] %s Redirecting to VK for authorization, remote address: %s", sid, request.getRemoteAddr()));
    response.sendRedirect(response.encodeRedirectURL(contextPath + contextParams));

%>

<!--%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

</body>
</html-->
