<%@ page import="java.util.Enumeration" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="static local.tcltk.Constants.SID_SIZE" %>
<%@ page import="static local.tcltk.Constants.SID_PATTERN" %>
<%@ page import="static local.tcltk.Constants.*" %>
<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 22.07.2017
  Time: 18:49
  To change this template use File | Settings | File Templates.
--%>
<%
    Logger logger = Logger.getLogger("verify.jsp");
    String sid = String.format(SID_PATTERN, request.getSession().getId().substring(request.getSession().getId().length() - SID_SIZE));

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
//    logger.info("- - - - - verify.jsp end - - - - - -");

    logger.info(String.format("[verify.jsp] %s show page", sid));
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title><%=SITE_TITLE%></title>
    <!--meta http-equiv="refresh" content="1; url=<%=response.encodeRedirectURL(WEB_APP_VIEW_URL)%>"/-->

    <script type="text/JavaScript">
        setTimeout("location.href = '<%=response.encodeURL(WEB_APP_VIEW_URL)%>';", 1000);
    </script>
</head>
<body>
<div class="index-outer">
    <div class="index-middle">
        <div class="index-inner">
            <table width="100%" height="100%">
                <tr><td valign="center" align="center"><img src="<%=SITE_ROOT%>img/giphy.gif"></td></tr>
            </table>
        </div>
    </div>
</div>

</body>
</html>
