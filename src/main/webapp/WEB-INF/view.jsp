<%@ page import="static local.tcltk.Constants.SITE_TITLE" %>
<%@ page import="static local.tcltk.Constants.VIEW_URL" %>
<%@ page import="static local.tcltk.Constants.STYLES_URL" %>
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
    String sid = String.format(SID_PATTERN, request.getSession().getId().substring(request.getSession().getId().length() - 3));

//    logger.info(String.format("[view.jsp] %s show page, user: %s", sid, user));

    String flatCheckbox = user.isUseFlat() ? "checked" : "";

    String strFlat = (user.getFlat() > MAX_FLAT_NUMBER_PER_SECTION) ? "<font color=RED>" + user.getFlat() + " (ошибка)</font>" : "" + user.getFlat();

    String buttonText = "Ввести данные";
    String strProfileButton = "";

    if (UPDATE_ATTEMPTS - user.getUpdates() > 0) {
        strProfileButton =
                "            <form action='" + PROFILE_URL + "' method='post' align=center>\n" +
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

%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title><%=SITE_TITLE%></title>

    <!-- favicon part -->
    <link rel="apple-touch-icon" sizes="180x180" href="<%=SITE_URL%>/apple-touch-icon.png?v=GvJ4ke7akz">
    <link rel="icon" type="image/png" sizes="32x32" href="<%=SITE_URL%>/favicon-32x32.png?v=GvJ4ke7akz">
    <link rel="icon" type="image/png" sizes="16x16" href="<%=SITE_URL%>/favicon-16x16.png?v=GvJ4ke7akz">
    <link rel="manifest" href="<%=SITE_URL%>/manifest.json?v=GvJ4ke7akz">
    <link rel="mask-icon" href="<%=SITE_URL%>/safari-pinned-tab.svg?v=GvJ4ke7akz" color="#5bbad5">
    <link rel="shortcut icon" href="<%=SITE_URL%>/favicon.ico?v=GvJ4ke7akz">
    <meta name="theme-color" content="#ffffff">
    <!-- end of favicon part -->


    <!--link rel="stylesheet" type="text/css" href="<%=STYLES_URL%>reset.css"-->
    <link rel="stylesheet" type="text/css" href="<%=STYLES_URL%>style.css?v=1.0">
    <script>
        function use_flat_func(){
            if(document.getElementById('use_flat').checked){
                window.location='<%=VIEW_URL%>?f=1';
                return false;
            } else {
                window.location='<%=VIEW_URL%>';
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
                <BR><a href='https://vk.com/id<%=user.getVk_id()%>'><img src='<%=user.getVkPhoto()%>' class='round-me'><BR>
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
                <%=strProfileButton%>
                <BR><BR>
                Есть вопросы?<BR><a href='https://vk.com/id6191031' target=_blank>Пишите</a>
            </td>
            <td align=center valign=center width=50%>
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
                </table>
            </td>
            <td width=20% valign='top' align='left'>
                <BR><BR><p class='text-total'><H3>Нас уже:<%=DatabaseManager.getUsersCountByBuilding(0)%></H3></p>
                <BR><%=HTMLHelper.getStat()%>
            </td>
        </tr>
    </table>



</body>
</html>
