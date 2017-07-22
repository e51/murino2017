<%@ page import="static local.tcltk.Constants.*" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="java.util.Enumeration" %>
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

    String sid = String.format(SID_PATTERN, request.getSession().getId().substring(request.getSession().getId().length() - SID_SIZE));

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
//    logger.info("- - - - - auth.jsp end - - - - - -");


    String contextPath = VK_WEB_APP_GET_AUTH_URL;
    String contextParams = "?client_id=" + VK_WEB_APP_ID +
            "&redirect_uri=" + WEB_APP_VK_REDIRECT_URI +
            "&display=page" +
            "&response_type=code" +
//            "&scope=notify" +
            "&scope=" +
//            response.encodeURL("&state=v") +
            "&v=5.65";

    logger.info(String.format("[auth] %s Redirecting to VK for authentication, remote address: %s", sid, request.getRemoteAddr()));
    response.sendRedirect(response.encodeRedirectURL(contextPath + contextParams));
%>
