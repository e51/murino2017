<%@ page import="static local.tcltk.Constants.*" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="local.tcltk.model.DatabaseManager" %>
<%@ page import="local.tcltk.User" %>
<%@ page import="java.util.Enumeration" %>
<%
    Logger logger = Logger.getLogger("index.jsp");

//    logger.info("Request URI: " + request.getRequestURI());
//    logger.info("Query string: " + request.getQueryString());
//    logger.info("SID: " + session.getId());
//    logger.info("Plane URL: " );
//    logger.info("encodeURL: " + response.encodeURL("any"));
//    logger.info("encodeRedirectURL: " + response.encodeRedirectURL("any"));
//
//    Enumeration<String> names = request.getHeaderNames();
//    logger.info("");
//    while (names.hasMoreElements()) {
//        String name = names.nextElement();
//        logger.info(name + ": " + request.getHeader(name));
//    }
//    logger.info("");
//    for (String name : response.getHeaderNames()) {
//        logger.info(name + ": " + response.getHeader(name));
//    }
//    logger.info("");
//
//    logger.info("- - - - - index.jsp end - - - - - -");

//    session.invalidate();
    session.removeAttribute("user");

    User user = null;

    String sid = String.format(SID_PATTERN, request.getSession().getId().substring(request.getSession().getId().length() - SID_SIZE));
    logger.info(String.format("[index] %s remote address: %s", sid, request.getRemoteAddr()));

    int count = DatabaseManager.getUsersCountByBuilding(0);
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file = "/WEB-INF/includes/head_part.jspf"%>

    <link rel="stylesheet" type="text/css" href="<%=STYLES_URL%>counter.css">
</head>
<body>
    <div class="index-outer">
        <div class="index-middle">
            <div class="index-inner">
                <H1>Войти:</H1><BR><BR>
                <a href="<%=response.encodeURL(WEB_APP_AUTH_URL)%>"><img src="img/vk_logo.jpg" sizes="150"></a>
                <BR><BR><BR><BR>
                <div id="countdown" class="countdownHolder">
                    <span class="counterText" style="top: 0px;">Нас уже: </span>
                    <span class="countDigits">
                        <span class="position">
                            <span class="digit static" style="top: 0px; opacity: 1;"><%=count/100%></span>
                        </span>
                        <span class="position">
                            <span class="digit static" style="top: 0px; opacity: 1;"><%=(count/10)%10%></span>
                        </span>
                        <span class="position">
                            <span class="digit static" style="top: 0px; opacity: 1;"><%=count%10%></span>
                        </span>
                    </span>
                </div>

                <p class="text-normal"><!--Последние изменения:<BR><BR-->
                    В <a href="https://vk.com/club149737048" target="_blank">сообщество VK</a> добавлено <a href="https://vk.com/app6119477_-149737048" target="_blank">приложение</a> сайтa.<BR>
                    Запускайте приложение по ссылке выше или из сообщества.<BR>
                    <img src="img/sosed-app.png"><BR>
                    <!--Мобильная вёрстка в разработке...<BR--><BR>
                    Исправлены проблемы при входе с Android-устройств.<BR>
                    <!--Добавлена возможность отображения соседей сверху/снизу без привязки к номеру квартиры.<BR-->
                    <!--Также создано открытое <a href="https://vk.com/club149737048" target="_blank">сообщество в VK</a> для общения и обсуждений.<BR>
                    Пишите свои предложения, отзывы, комментарии.-->

                </p>
                <BR><BR>
                Обратная связь: <a href="https://vk.com/id<%=ADMIN_VK_ID%>" target="_blank">мой vk</a>
            </div>
        </div>
    </div>
</body>
</html>
