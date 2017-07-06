<%@ page import="java.io.PrintWriter" %>
<%@ page import="static local.tcltk.Constants.*" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title><%=SITE_TITLE%></title>
</head>
<body>
<%
    session.invalidate();
//    session.removeAttribute("user");

    String contextPath = VK_GET_AUTH_URL;
    String contextParams = "?client_id=" + VK_APP_ID +
            "&redirect_uri=" + VK_REDIRECT_URI +
            "&display=page" +
            "&response_type=code" +
            "&scope=notify" +
            "&v=5.65";

%>
<table height=100% width=100%>
    <tr>
        <td valign="center" align="center">
            <H1>Войти:</H1><BR><BR>
            <a href="<%=(contextPath + contextParams)%>"><img src="<%=SITE_URL%>img/vk_logo.jpg" sizes="150"></a>
            <BR><BR><BR><BR><BR>
            Made by <a href="https://vk.com/id6191031">me</a> =)
        </td>
    </tr>

</table>


</body>
</html>
