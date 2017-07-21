<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="static local.tcltk.Constants.*" %>
<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 20.07.2017
  Time: 12:12
  To change this template use File | Settings | File Templates.
--%>
<%
    Logger logger = Logger.getLogger("auth.jsp");

//    session.invalidate();
    session.removeAttribute("user");
    String sid = String.format(SID_PATTERN, request.getSession().getId().substring(request.getSession().getId().length() - SID_SIZE));

    logger.info(String.format("[auth] %s EMBEDDED APP authentication, forward to verify, remote address: %s", sid, request.getRemoteAddr()));
    request.getRequestDispatcher("/" + "e/" + "e-verify").forward(request, response);
%>
