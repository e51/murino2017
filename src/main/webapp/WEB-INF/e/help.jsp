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
    Logger logger = Logger.getLogger("help.jsp");
    User user = (User) session.getAttribute("user");

    logger.info("[help] show help page");
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
            <div class="div-help-page">
                <H2>Подсказка:</H2>
                <!--BR-->
                <p>Данные для заполнения брать из договора, где прописан строительный адрес квартиры в формате 1.2.3.4<BR>
                    Где: 1 - корпус, 2 - секция, 3 - этаж, 4 - квартира<BR>
                    Если где-то фигурирует дробная секция, например: 1.5, то это означает: корпус 1, секция 5.<BR>
                    Номер квартиры нужен именно <strong>строительный</strong> (короткий номер от 1 до <%=MAX_FLAT_NUMBER_PER_SECTION%>)<BR>
                </p>
                    <BR>
                    <p>Введённые данные никому, кроме Вас, видны не будут.</p>
                    <p>Поля отмеченные "<font color="red"><b>*</b></font>" обязательны для заполнения.</p>
                    <p>Квартиру можно не указывать (влияет на отображение конкретно Ваших соседей сверху и снизу).</p>
                    <p>Повторно изменить данные в будущем можно будет обратившись по контактам на сайте.</p>
                <BR>

                <form action='<%=EMBEDDED_APP_PROFILE_URL%>' method='post' align=center>
                    <p><input type='submit' value='Назад' class='submit'></p>
                </form>

            </div>
        </td>
    </tr>
</table>

</body>
</html>
