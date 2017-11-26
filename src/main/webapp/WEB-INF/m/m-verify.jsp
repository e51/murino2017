<%@ page import="java.util.Enumeration" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="static local.tcltk.Constants.SID_SIZE" %>
<%@ page import="static local.tcltk.Constants.SID_PATTERN" %>
<%@ page import="static local.tcltk.Constants.*" %>
<%@ page import="local.tcltk.model.dao.DatabaseManager" %>
<%@ page import="local.tcltk.model.domain.User" %>
<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 22.07.2017
  Time: 18:49
  To change this template use File | Settings | File Templates.
--%>
<%
    Logger logger = Logger.getLogger("m-verify.jsp");
    String sid = String.format(SID_PATTERN, request.getSession().getId().substring(request.getSession().getId().length() - SID_SIZE));
    User user = (User) session.getAttribute("user");

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
    int count = DatabaseManager.getUsersCountByBuilding(0);

    logger.info(String.format("[m-verify.jsp] %s show page", sid));
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file = "/WEB-INF/includes/head_part.jspf"%>
    <meta http-equiv="refresh" content="1; url=<%=response.encodeRedirectURL(MOBILE_APP_VIEW_URL)%>"/>
    <link rel="stylesheet" type="text/css" href="<%=STYLES_URL%>counter.css">
</head>
<body>
    <div class="index-outer">
        <div class="index-middle">
            <div class="index-inner">
                <div class="us-block">
                    <BR>Нас уже:<BR><BR>
                </div>
                <!--table width="320" height="100%">
                    <tr><td valign="center" align="center"-->
                <div id="countdown" class="countdownHolder">
                    <!--span class="counterText" style="top: 0px;"><BR>Нас уже:<BR><BR> </span-->
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
                <img src="<%=SITE_ROOT%>img/giphy.gif" width="256" height="140">
                <!--/td></tr>
            </table-->
            </div>
        </div>
    </div>


</body>
</html>
