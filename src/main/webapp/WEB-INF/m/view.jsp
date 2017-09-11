<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="static local.tcltk.Constants.*" %>
<%@ page import="local.tcltk.HTMLHelper" %>
<%@ page import="local.tcltk.model.domain.User" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="local.tcltk.model.dao.DatabaseManager" %>
<%@ page import="javax.swing.text.html.HTML" %>
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

    String flatCheckbox = user.isUseFlat() ? "checked" : "";

    String strFlat = (user.getFlat() > MAX_FLAT_NUMBER_PER_SECTION) ? "<font color=RED>" + user.getFlat() + "</font>" : "" + user.getFlat();

    String buttonText = "Ввести данные";
    String strProfileButton = "";

    if (UPDATE_ATTEMPTS - user.getUpdates() > 0) {
        strProfileButton =
                "            <form action='" + response.encodeURL(MOBILE_APP_PROFILE_URL) + "' method='post' align=center>" +
                "                <input type='submit' value='" + buttonText + "' class='submit-data-btn'>" +
                "            </form>";
    }

    String topNeighboursTitle = "Все соседи этажом выше:";
    String floorNeighboursTitle = "Соседи по площадке:";
    String bottomNeighboursTitle = "Все соседи этажом ниже:";

    if (user.isUseFlat()) {
        topNeighboursTitle = "Соседи над вами:";
        bottomNeighboursTitle = "Соседи под вами:";
    }

    logger.info(String.format("[m/view.jsp] %s show page", sid));
%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<!--!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <!--meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <title></title>
    <meta name="keywords" content="" />
    <meta name="description" content="" />
    <link rel="stylesheet" href="style.css" type="text/css" /-->

<!DOCTYPE html>
<html>
<head>

    <!--meta charset="utf-8" /-->
    <!--[if lt IE 9]><script src="https://cdnjs.cloudflare.com/ajax/libs/html5shiv/3.7.3/html5shiv.min.js"></script><![endif]-->
    <!--title></title>
    <meta name="keywords" content="" />
    <meta name="description" content="" />
    <link href="style.css" rel="stylesheet"-->

    <%@ include file = "/WEB-INF/includes/head_part.jspf" %>

    <script>
        function use_flat_func(){
            if(document.getElementById('use_flat').checked){
                window.location='<%=response.encodeURL(MOBILE_APP_VIEW_URL)%>?f=1';
                return false;
            } else {
                window.location='<%=response.encodeURL(MOBILE_APP_VIEW_URL)%>';
                return false;
            }
            return true;
        }
    </script>
</head>
<body>

<div class="wrapper">

    <header class="header">
        <div class="header-user-data">
            <div class="photo-block"><img src='${user.vkPhoto100}<%--=user.getVkPhoto100()--%>'></div>
            <div class="address-block">
                <div class="address-line">
                    Корпус: ${user.building}
                </div>
                <div class="address-line">
                    Секция: ${user.section}
                </div>
                <div class="address-line">
                    Этаж: ${user.floor}
                </div>
                <div class="address-line">
                    Квартира: ${user.flat}
                </div>
            </div>

            <!--div class="data-btn-block">
            <%=strProfileButton%>
            </div-->

        </div>

    </header><!-- .header-->

    <div class="middle">
        <div class="container">
            <main class="content">
                <c:choose>
                    <c:when test="${user.isValid()}">
                        <!--div class="block-checkbox">
                            <input type='checkbox' name='use_flat' id='use_flat' value='1' onclick='return use_flat_func();' <%=flatCheckbox%>/> Учитывать номер квартиры
                        </div-->
                        <div class="block-section">
                            <div class="block-section-title">
                                <%=topNeighboursTitle%>
                            </div>
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
                            <BR>
                        </div>
                        <div class="block-section">
                            <div class="block-section-title">
                                <strong><%=floorNeighboursTitle%></strong>
                            </div>
                            <div id='container'>
                                <%--=HTMLHelper.getNeighboursSectionHTML(user)--%>
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
                            </div><BR>
                        </div>
                        <c:choose>
                            <c:when test="${user.floor == 1}">
                                <!-- nobody below us -->
                            </c:when>
                            <c:otherwise>
                                <div class="block-section">
                                    <div class="block-section-title">
                                        <strong><%=bottomNeighboursTitle%></strong>
                                    </div>
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
                                    <BR>
                                </div>
                            </c:otherwise>
                        </c:choose>

                        <%=strProfileButton%>
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
                                <!--BR><BR-->
                                <form action='<%=response.encodeURL(MOBILE_APP_PROFILE_URL)%>' method='post' align=center>
                                    <input type='submit' value='Найти соседей' class='submit-data-btn2'>
                                    <BR>
                                </form>
                                <!--BR><BR-->

                            </div>
                    </c:otherwise>
                </c:choose>

            </main><!-- .content -->
        </div><!-- .container-->

        <!--aside class="right-sidebar">
            Нас уже: <%=DatabaseManager.getUsersCountByBuilding(0)%>
            <BR><BR>
            <%=HTMLHelper.getStat()%>
        </aside><!-- .right-sidebar -->

    </div><!-- .middle-->

</div><!-- .wrapper -->

<footer class="footer">
    <a href='https://m.vk.com/write<%=ADMIN_VK_ID%>' target=_blank>Задать вопрос</a>
</footer><!-- .footer -->



<!--table width=100% height=100%>
        <tr>
            <td align=center valign=top width=30%>
                <p class='text-normal'>
                    <input type='checkbox' name='use_flat' id='use_flat' value='1' onclick='return use_flat_func();' <%=flatCheckbox%>/>
                    Учитывать номер квартиры
                </p>

            </td>
        </tr>
    </table-->

</body>
</html>
