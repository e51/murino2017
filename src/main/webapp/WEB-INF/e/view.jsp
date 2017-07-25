<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="static local.tcltk.Constants.*" %>
<%@ page import="local.tcltk.HTMLHelper" %>
<%@ page import="local.tcltk.User" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="local.tcltk.model.DatabaseManager" %>
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

    String flatCheckbox = user.isUseFlat() ? "checked" : "";

    String strFlat = (user.getFlat() > MAX_FLAT_NUMBER_PER_SECTION) ? "<font color=RED>" + user.getFlat() + "</font>" : "" + user.getFlat();

    String buttonText = "Ввести данные";
    String strProfileButton = "";

    if (UPDATE_ATTEMPTS - user.getUpdates() > 0) {
        strProfileButton =
                "            <form action='" + response.encodeURL(EMBEDDED_APP_PROFILE_URL) + "' method='post' align=center>\n" +
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

    logger.info(String.format("[e/view.jsp] %s show page", sid));
%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file = "/WEB-INF/includes/head_part.jspf"%>

    <script>
        function use_flat_func(){
            if(document.getElementById('use_flat').checked){
                window.location='<%=response.encodeURL(EMBEDDED_APP_VIEW_URL)%>?f=1';
                return false;
            } else {
                window.location='<%=response.encodeURL(EMBEDDED_APP_VIEW_URL)%>';
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
                <BR><a href='https://vk.com/id<%=user.getVk_id()%>'>
                <img src='<%=user.getVkPhoto200()%>'><BR>
                <%=user.getVkFirstName()%><BR>
                <%=user.getVkLastName()%></a><BR>
                <H1>Я здесь:</H1>
                <p class='text-normal'>Корпус: <%=user.getBuilding()%></p>
                <p class='text-normal'>Секция: <%=user.getSection()%></p>
                <p class='text-normal'>Этаж: <%=user.getFloor()%></p>
                <p class='text-normal'>Квартира: <%=strFlat%></p>
                <BR>
                <p class='text-normal'>
                    <input type='checkbox' name='use_flat' id='use_flat' value='1' onclick='return use_flat_func();' <%=flatCheckbox%>/>
                    Учитывать номер квартиры при<BR> поиске соседей сверху/снизу
                </p>
                <!--%=strProfileButton%-->
                <BR><BR>
                <a href='https://vk.com/write<%=ADMIN_VK_ID%>' target=_blank>Задать вопрос</a>
            </td>
            <td align=left valign=top width=70%>
                <c:choose>
                    <c:when test="${user.isValid()}">
                        <table width=100% height=100%>
                            <tr>
                                <td valign='top'>
                                    <H1><%=topNeighboursTitle%></H1><BR><%=HTMLHelper.getNeighboursTopHTML(user)%>
                                </td>
                            </tr>
                            <tr>
                                <td valign='top'>
                                    <H1><%=floorNeighboursTitle%></H1><BR><div id='container'><%=HTMLHelper.getNeighboursSectionHTML(user)%>
                                </div></td>
                            </tr>
                            <tr>
                                <td valign='top'>
                                    <H1><%=bottomNeighboursTitle%></H1><BR><%=HTMLHelper.getNeighboursBottomHTML(user)%>
                                </td>
                            </tr>
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
                            <!--table height="240" width="100%"><tr><td class="view-new-user-block"-->
                            <%=HTMLHelper.getRandomNeighboursHTML(user)%>
                            <!--BR>
                            Здесь будут отображаться Ваши соседи после ввода данных.

                            <BR><BR-->
                            <BR><BR><BR><BR><BR><BR>
                            <form action='<%=response.encodeURL(EMBEDDED_APP_PROFILE_URL)%>' method='post' align=center>
                                <input type='submit' value='Найти соседей' class='submit-data-btn2'>
                                <BR>
                            </form>
                            <!--BR><BR-->
                        </div>
                        <!--/td></tr></table-->

                    </c:otherwise>
                </c:choose>

            </td>
            <!--td width=20% valign='top' align='left'>
                <BR><BR><p class='text-total'><H3>Нас уже: <%=DatabaseManager.getUsersCountByBuilding(0)%></H3></p>
                <BR><%=HTMLHelper.getStat()%>
            </td-->
        </tr>
    </table>

</body>
</html>
