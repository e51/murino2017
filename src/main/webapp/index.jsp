<%@ page import="static local.tcltk.Constants.*" %>
<%@ page import="org.apache.log4j.Logger" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" type="text/css" href="<%=SITE_URL%>styles.css">
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
<table height=100% width=100%>
    <tr>
        <td valign="center" align="center">
            <H1>Войти:</H1><BR><BR>
            <a href="<%=(contextPath + contextParams)%>"><img src="<%=SITE_URL%>img/vk_logo.jpg" sizes="150"></a>
            <BR><BR><BR>
            <!--p class="text-normal">Последние изменения:<BR><BR>
                Друзья! Идёт путаница с номерами квартир. Кто-то вносит трёхзначные номера (что неверно), кто-то - однозначные.<br>
                Поскольку участников пока мало, и вероятность, что соседи ровно сверху/снизу найдутся - мала, пока отключаю<br>
                поле "квартира" для заполнения. В соседях сверху и снизу будут отображаться все, кто живёт выше/ниже этажом в вашей секции.</p-->
            <!--p class="text-normal">Последние изменения:<BR><BR>
                При внесении данных часто возникает путаница, как же всё-таки заполнять.<br>
                В форме заполнения данных добавлена подсказка по заполнению.</p-->
            <BR><BR>
            Обратная связь: <a href="https://vk.com/id6191031" target="_blank">пишите</a>
        </td>
    </tr>

</table>


</body>
</html>
