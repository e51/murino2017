<%@ page import="static local.tcltk.Constants.*" %>
<%@ page import="org.apache.log4j.Logger" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <!--link rel="stylesheet" type="text/css" href="<%=STYLES_URL%>reset.css"-->
    <link rel="stylesheet" type="text/css" href="<%=STYLES_URL%>style.css">
    <title><%=SITE_TITLE%></title>
</head>
<body>
<%
    Logger logger = Logger.getLogger("index.jsp");

    session.invalidate();
//    session.removeAttribute("user");

    String contextPath = VK_GET_AUTH_URL;
    String contextParams = "?client_id=" + VK_APP_ID +
            "&redirect_uri=" + VK_REDIRECT_URI +
            "&display=page" +
            "&response_type=code" +
//            "&scope=notify" +
            "&scope=" +
            "&v=5.65";

    String sid = request.getSession().getId().substring(request.getSession().getId().length() - 3);
    logger.info("[index] {" + sid + "}, remote address: " + request.getRemoteAddr());

%>
<table height=100% width=100% valign="center">
    <tr valign="center">
        <td valign="center" align="center">
            <H1>Войти:</H1><BR><BR>
            <a href="<%=(contextPath + contextParams)%>"><img src="img/vk_logo.jpg" sizes="150"></a>
            <BR><BR><BR>
            <p class="text-normal">Последние изменения:<BR><BR>
                Добавлена возможность отображения соседей сверху/снизу без привязки к номеру квартиры.<BR>
                Также создано открытое <a href="https://vk.com/club149737048" target="_blank">сообщество в VK</a> для общения и обсуждений.<BR>
                Пишите свои предложения, отзывы, комментарии.

            </p>
            <BR><BR>
            Обратная связь: <a href="https://vk.com/id6191031" target="_blank">мой vk</a>
        </td>
    </tr>

</table>


</body>
</html>
