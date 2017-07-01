<%@ page import="local.tcltk.Solution" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="static local.tcltk.Constants.VK_APP_ID" %>
<%@ page import="static local.tcltk.Constants.VK_REDIRECT_URI" %>
<%@ page import="static local.tcltk.Constants.SITE_URL" %>

<html>
<body>
<%
    session.invalidate();
//    session.removeAttribute("user");

    String contextPath = "https://oauth.vk.com/authorize";
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
            <a href="<%=(contextPath + contextParams)%>"><img src="<%=SITE_URL%>img/vk_logo.jpg" sizes="150"></a>
        </td>
    </tr>
</table>


</body>
</html>
