<%@ page import="static local.tcltk.Constants.*" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="local.tcltk.model.DatabaseManager" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <!--link rel="stylesheet" type="text/css" href="<%=STYLES_URL%>reset.css"-->
    <link rel="stylesheet" type="text/css" href="<%=STYLES_URL%>style.css?v=1.0">
    <link rel="stylesheet" type="text/css" href="<%=STYLES_URL%>counter.css">
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

</head>
<body>
<%
    Logger logger = Logger.getLogger("index.jsp");

    session.invalidate();
//    session.removeAttribute("user");

    String sid = String.format(SID_PATTERN, request.getSession().getId().substring(request.getSession().getId().length() - 3));
    logger.info(String.format("[index.jsp] %s remote address: %s", sid, request.getRemoteAddr()));


/*
    String contextPath = VK_GET_AUTH_URL;
    String contextParams = "?client_id=" + VK_APP_ID +
            "&redirect_uri=" + VK_REDIRECT_URI +
            "&display=page" +
            "&response_type=code" +
//            "&scope=notify" +
            "&scope=" +
            "&v=5.65";
*/

    int count = DatabaseManager.getUsersCountByBuilding(0);
%>

<div class="index-outer">
    <div class="index-middle">
        <div class="index-inner">
            <H1>Войти:</H1><BR><BR>
            <a href="<%=AUTH_URL%>"><img src="img/vk_logo.jpg" sizes="150"></a>
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

            <p class="text-normal">Последние изменения:<BR><BR>
                Добавлена возможность отображения соседей сверху/снизу без привязки к номеру квартиры.<BR>
                <!--Также создано открытое <a href="https://vk.com/club149737048" target="_blank">сообщество в VK</a> для общения и обсуждений.<BR>
                Пишите свои предложения, отзывы, комментарии.-->

            </p>
            <BR><BR>
            Обратная связь: <a href="https://vk.com/id6191031" target="_blank">мой vk</a>
        </div>
    </div>
</div>

</body>
</html>
