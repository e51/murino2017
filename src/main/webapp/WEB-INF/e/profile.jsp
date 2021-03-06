<%@ page import="static local.tcltk.Constants.*" %>
<%@ page import="local.tcltk.model.domain.User" %>
<%@ page import="org.apache.log4j.Logger" %>
<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 16.07.2017
  Time: 19:19
  To change this template use File | Settings | File Templates.
--%>
<%
    Logger logger = Logger.getLogger("profile.jsp");
    User user = (User) session.getAttribute("user");
    String sid = String.format(SID_PATTERN, request.getSession().getId().substring(request.getSession().getId().length() - SID_SIZE));

    logger.info(String.format("[e/profile] %s show form fill page", sid));
%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file = "/WEB-INF/includes/head_part.jspf"%>
</head>
<body>

<table width=100% height=100% class="text-normal">
    <tr>
        <td align=center valign=center>
            <div class="profile-container">
                <p align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="<%=response.encodeURL(EMBEDDED_APP_HELP_URL)%>">П О М О Щ Ь</a><BR></p>
                <H2>Впервые у нас?</H2>
                <!--BR-->
                <form action='<%=response.encodeURL(EMBEDDED_APP_PROFILE_URL)%>' method='post' align=center>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Например:
                    <p>&nbsp;Корпус (по ДДУ)<font color="red"><b>*</b></font>: <input type='text' name='building' value='<%=user.getBuilding()%>' size='9' class="input-style"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</p>
                    <p>&nbsp;Секция<font color="red"><b>*</b></font>: <input type='text' name='section' value='<%=user.getSection()%>' size='9' class="input-style"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;7</p>
                    <p>&nbsp;&nbsp;&nbsp;&nbsp;Этаж<font color="red"><b>*</b></font>: <input type='text' name='floor' value='<%=user.getFloor()%>' size='9' class="input-style"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2</p>
                    <p>Квартира: <input type='text' name='flat' value='<%=user.getFlat()%>' size='9' class="input-style"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2</p>
                    <p><input type='hidden' name='action' value='update'></p>
                    <p><input type='submit' value=' Сохранить ' class='submit-profile'></p>
                    <BR>
                    <!--p>При заполнении используйте <b>строительный</b> адрес квартиры.</p-->
                </form>

                <BR>

            </div>
        </td>
    </tr>
</table>

</body>
</html>
