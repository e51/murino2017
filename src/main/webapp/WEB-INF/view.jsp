<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="static local.tcltk.Constants.*" %>
<%@ page import="local.tcltk.HTMLHelper" %>
<%@ page import="local.tcltk.model.domain.User" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="local.tcltk.model.dao.DatabaseManager" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="local.tcltk.model.dao.UserDAO" %>
<%@ page isELIgnored="false"%>
<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 16.07.2017
  Time: 22:28
  To change this template use File | Settings | File Templates.
--%>
<%
    Logger logger = Logger.getLogger("view.jsp");
    User user = (User) session.getAttribute("user");
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
//    logger.info("- - - - - view.jsp end - - - - - -");

//    logger.info(String.format("[view.jsp] %s show page, user: %s", sid, user));

    String flatCheckbox = user.isUseFlat() ? "checked" : "";

    String strFlat = (user.getFlat() > MAX_FLAT_NUMBER_PER_SECTION) ? "<font color=RED>" + user.getFlat() + "</font>" : "" + user.getFlat();

    String buttonText = "Ввести данные";
    String strProfileButton = "";

    if (UPDATE_ATTEMPTS - user.getUpdates() > 0) {
        strProfileButton =
                "            <form action='" + response.encodeURL(WEB_APP_PROFILE_URL) + "' method='post' align=center>\n" +
                "                <p><input type='submit' value='" + buttonText + "' class='submit'></p>\n" +
                "            </form>";
    }

    String topNeighboursTitle = "Все соседи этажом выше:";
    String floorNeighboursTitle = "Соседи по площадке:";
    String bottomNeighboursTitle = "Все соседи этажом ниже:";

    if (user.isUseFlat()) {
        topNeighboursTitle = "Соседи над вами:";
        bottomNeighboursTitle = "Соседи под вами:";
    }

    logger.info(String.format("[view.jsp] %s show page", sid));
%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@ include file = "/WEB-INF/includes/head_part.jspf" %>

    <script>
        function use_flat_func(){
            if(document.getElementById('use_flat').checked){
                window.location='<%=response.encodeURL(WEB_APP_VIEW_URL)%>?f=1';
                return false;
            } else {
                window.location='<%=response.encodeURL(WEB_APP_VIEW_URL)%>';
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
    <table width=100% height=100%>
        <tr>
            <td align=center valign=top width=30%>
                <BR><a href='https://vk.com/id${user.vk_id}<%--=user.getVk_id()--%>'><img src='${user.vkPhoto200}<%--=user.getVkPhoto200()--%>'><BR>
                ${user.vkFirstName}<BR>
                ${user.vkLastName}<%--=user.getVkLastName()--%></a><BR>
                <H1>Я здесь:</H1>
                <p class='text-normal'>Корпус: ${user.building}</p>
                <p class='text-normal'>Секция: ${user.section}</p>
                <p class='text-normal'>Этаж: ${user.floor}</p>
                <p class='text-normal'>Квартира: ${user.flat}</p>
                <BR>
                <!--p class='text-normal'>
                    <input type='checkbox' name='use_flat' id='use_flat' value='1' onclick='return use_flat_func();' <%=flatCheckbox%>/>
                    Учитывать номер квартиры при<BR> поиске соседей сверху/снизу
                </p-->
                <!--%=strProfileButton%-->
                <BR><BR>
                <a href='https://vk.com/write<%=ADMIN_VK_ID%>' target=_blank>Задать вопрос</a>
            </td>
            <td align=left valign=top width=50%>
                <c:choose>
                    <c:when test="${user.isValid()}">
                        <table width=100% height=100%>
                            <tr>
                                <td valign='top'>
                                    <H1><%=topNeighboursTitle%></H1><BR>
                                    <c:choose>
                                        <c:when test="${fn:length(requestScope.topNeighbours) gt 0}">
                                            <c:forEach var="user" items="${requestScope.topNeighbours}">
                                                <div id='block-neighbour'>
                                                    <a href='https://vk.com/id${user.vk_id}' target='_blank'>
                                                        <img src='${user.vkPhoto100}' class='round-neighbour-photo'><BR>${user.vkFirstName}<BR>${user.vkLastName}
                                                    </a><BR>
                                                </div>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            пока нет соседей :(
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                            <tr>
                                <td valign='top'>
                                    <H1><%=floorNeighboursTitle%></H1><BR>
                                    <div id='container'>
                                        <c:choose>
                                            <c:when test="${fn:length(requestScope.floorNeighbours) gt 0}">
                                                <c:forEach var="user" items="${requestScope.floorNeighbours}">
                                                    <div id='block-neighbour'>
                                                        <a href='https://vk.com/id${user.vk_id}' target='_blank'>
                                                            <img src='${user.vkPhoto100}' class='round-neighbour-photo'><BR>${user.vkFirstName}<BR>${user.vkLastName}
                                                        </a><BR>
                                                    </div>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                пока нет соседей :(
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </td>
                            </tr>
                            <c:choose>
                                <c:when test="${user.floor == 1}">
                                    <!-- nobody below us -->
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td valign='top'>
                                            <H1><%=bottomNeighboursTitle%></H1><BR>
                                            <c:choose>
                                                <c:when test="${fn:length(requestScope.bottomNeighbours) gt 0}">
                                                    <c:forEach var="user" items="${requestScope.bottomNeighbours}">
                                                        <div id='block-neighbour'>
                                                            <a href='https://vk.com/id${user.vk_id}' target='_blank'>
                                                                <img src='${user.vkPhoto100}' class='round-neighbour-photo'><BR>${user.vkFirstName}<BR>${user.vkLastName}
                                                            </a><BR>
                                                        </div>
                                                    </c:forEach>
                                                </c:when>
                                                <c:otherwise>
                                                    пока нет соседей :(
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                            <tr>
                                <td>
                                    <%=strProfileButton%>
                                </td>
                            </tr>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <div class="block-section">
                            <div class="block-section-title">
                                <strong>Возможные соседи:</strong>
                            </div>
                            <div class="block-neighbours-promo">
                                <c:forEach var="user" items="${requestScope.randomNeighbours}">
                                    <div class='block-neighbour-promo'><img src='${user.vkPhoto100}' class='blur'><BR></div>
                                </c:forEach>
                            </div>
                            <BR><BR><BR><BR><BR><BR>
                            <form action='<%=response.encodeURL(WEB_APP_PROFILE_URL)%>' method='post' align=center>
                                <input type='submit' value='Найти соседей' class='submit-data-btn2'>
                                <BR>
                            </form>
                            <!--BR><BR-->
                        </div>
                        <!--/td></tr></table-->

                    </c:otherwise>
                </c:choose>

            </td>
            <td width=20% valign='top' align='left'>
                <BR><BR><p class='text-total'><H3>Нас уже: <%=(new UserDAO().getUsersCountByBuilding(0))%> <%--=DatabaseManager.getUsersCountByBuilding(0)--%></H3></p>
                <BR><%=HTMLHelper.getStat()%>
            </td>
        </tr>
    </table>

</body>
</html>
