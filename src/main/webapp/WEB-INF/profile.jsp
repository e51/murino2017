<%@ page import="static local.tcltk.Constants.*" %>
<%@ page import="local.tcltk.User" %>
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

    logger.info("Request URI: " + request.getRequestURI());
    logger.info("Query string: " + request.getQueryString());
    logger.info("SID: " + session.getId());
    logger.info("Plane URL: " );
    logger.info("encodeURL: " + response.encodeURL("page4"));
    logger.info("encodeRedirectURL: " + response.encodeRedirectURL("page4"));
    logger.info("- - - - - profile.jsp end - - - - - -");

//    logger.info("[profile.jsp] show page");
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
                <H2>Впервые у нас?</H2>
                <!--BR-->
                <p>Данные для заполнения брать из договора, где прописан строительный адрес квартиры в формате 1.2.3.4<BR>
                    Где: 1 - корпус, 2 - секция, 3 - этаж, 4 - квартира<BR>
                    Если где-то фигурирует дробная секция, например: 1.5, то это означает: корпус 1, секция 5.<BR>
                    Номер квартиры нужен именно <strong>строительный</strong> (короткий номер от 1 до <%=MAX_FLAT_NUMBER_PER_SECTION%>)<BR>
                </p>
                <form action='<%=response.encodeRedirectURL(WEB_APP_PROFILE_URL)%>' method='post' align=center>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Например:
                    <p>&nbsp;Корпус<font color="red"><b>*</b></font>: <input type='text' name='building' value='<%=user.getBuilding()%>' size='9' class="input-style"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1</p>
                    <p>&nbsp;Секция<font color="red"><b>*</b></font>: <input type='text' name='section' value='<%=user.getSection()%>' size='9' class="input-style"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;7</p>
                    <p>&nbsp;&nbsp;&nbsp;&nbsp;Этаж<font color="red"><b>*</b></font>: <input type='text' name='floor' value='<%=user.getFloor()%>' size='9' class="input-style"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2</p>
                    <p>Квартира: <input type='text' name='flat' value='<%=user.getFlat()%>' size='9' class="input-style"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2</p>
                    <p><input type='hidden' name='action' value='update'></p>
                    <p><input type='submit' value=' Сохранить ' class='submit-profile'></p>
                    <BR>
                    <p>Введённые данные никому, кроме Вас, видны не будут.</p>
                    <p>Поля отмеченные "<font color="red"><b>*</b></font>" обязательны для заполнения.</p>
                    <p>Квартиру можно не указывать (влияет на отображение конкретно Ваших соседей сверху и снизу).</p>
                    <p>Повторно изменить данные в будущем можно будет обратившись по контактам на сайте.</p>
                </form>
                <BR>
            </div>
        </td>
    </tr>
</table>

</body>
</html>
