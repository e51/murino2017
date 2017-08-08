<%@ page import="static local.tcltk.Constants.*" %>
<%@ page import="local.tcltk.User" %>
<%@ page import="org.apache.log4j.Logger" %>
<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 20.07.2017
  Time: 15:24
  To change this template use File | Settings | File Templates.
--%>
<%
    Logger logger = Logger.getLogger("error.jsp");
    User user = null;
    String sid = String.format(SID_PATTERN, request.getSession().getId().substring(request.getSession().getId().length() - SID_SIZE));

    logger.info(String.format("[e/error] %s show error page", sid));
    logger.info(String.format("User Agent: %s", request.getHeader("User-Agent")));
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file = "/WEB-INF/includes/head_part.jspf"%>
</head>
<body>
<div class="index-outer">
    <div class="index-middle">
        <div class="index-inner">
            <H1>Произошла ошибка :(</H1>
            <p class="text-normal"><BR><BR>
                Дальнейшая работа приложения невозможна.<BR>
                Пожалуйста, скорее сообщите мне об этом, и я всё исправлю.<BR>
            </p>
            <BR><BR>
            Обратная связь: <a href="https://vk.com/id<%=ADMIN_VK_ID%>" target="_blank">мой vk</a>
        </div>
    </div>
</div>
</body>
</html>
