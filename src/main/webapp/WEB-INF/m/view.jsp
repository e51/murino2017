<%@ page import="static local.tcltk.Constants.*" %>
<%@ page import="local.tcltk.HTMLHelper" %>
<%@ page import="local.tcltk.User" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="local.tcltk.model.DatabaseManager" %>
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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false"%>
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

    <%@include file = "/WEB-INF/includes/head_part.jspf"%>

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
            <div class="photo-block"><img src='<%=user.getVkPhoto100()%>'></div>
            <div class="address-block">
                <div class="address-line">
                    Корпус: <%=user.getBuilding()%>
                </div>
                <div class="address-line">
                    Секция: <%=user.getSection()%>
                </div>
                <div class="address-line">
                    Этаж: <%=user.getFloor()%>
                </div>
                <div class="address-line">
                    Квартира: <%=strFlat%>
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
                        <div class="block-checkbox">
                            <input type='checkbox' name='use_flat' id='use_flat' value='1' onclick='return use_flat_func();' <%=flatCheckbox%>/> Учитывать номер квартиры
                        </div>
                        <div class="block-section">
                            <div class="block-section-title">
                                <%=topNeighboursTitle%>
                            </div>
                            <%=HTMLHelper.getNeighboursTopHTML(user)%><BR>
                        </div>
                        <div class="block-section">
                            <div class="block-section-title">
                                <strong><%=floorNeighboursTitle%></strong>
                            </div>
                            <div id='container'><%=HTMLHelper.getNeighboursSectionHTML(user)%></div><BR>
                        </div>
                        <div class="block-section">
                            <div class="block-section-title">
                                <strong><%=bottomNeighboursTitle%></strong>
                            </div>
                            <%=HTMLHelper.getNeighboursBottomHTML(user)%><BR>
                        </div>
                        <%=strProfileButton%>
                    </c:when>
                    <c:otherwise>
                    <div class="index-outer">
                        <div class="index-middle">
                            <div class="index-inner">
                                <div class="view-new-user-block">
                                <!--table height="240" width="100%"><tr><td class="view-new-user-block"-->
                                    <BR>
                                    Здесь будут отображаться Ваши соседи после ввода данных.

                                    <BR><BR>

                                    <form action='<%=response.encodeURL(MOBILE_APP_PROFILE_URL)%>' method='post' align=center>
                                        <input type='submit' value='Ввести данные' class='submit-data-btn2'>
                                        <BR><BR>
                                    </form>
                                    <BR><BR>

                                </div>
                                <!--/td></tr></table-->
                            </div>
                        </div>
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
    Есть вопросы?<BR><a href='https://vk.com/id<%=ADMIN_VK_ID%>' target=_blank>Пишите</a>
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
