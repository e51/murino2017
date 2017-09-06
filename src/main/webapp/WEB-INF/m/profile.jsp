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

    logger.info(String.format("[m/profile] %s show form fill page", sid));
%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <!--meta charset="utf-8" /-->
    <!--[if lt IE 9]><script src="https://cdnjs.cloudflare.com/ajax/libs/html5shiv/3.7.3/html5shiv.min.js"></script><![endif]-->

    <%@include file = "/WEB-INF/includes/head_part.jspf"%>
</head>
<body>

<div class="wrapper">

    <header class="profile-header">
        <BR>
        <form action="<%=response.encodeURL(MOBILE_APP_HELP_URL)%>" method="post">
            <input type='submit' value='П О М О Щ Ь' class="profile-help-btn">
        </form>
    </header><!-- .header-->

    <div class="profile-middle">

        <div class="container">
            <main class="profile-content">
                <div class="profile-body">
                    <div style="font-size: 24px; float: right; width: 100%; text-align: center">
                        Впервые у нас?
                    </div>
                    <BR>
                    <form action='<%=response.encodeURL(MOBILE_APP_PROFILE_URL)%>' method='post' align=center>
                        <div style="width: 100%; text-align: right">
                            Например:
                        </div>
                        <div class="profile-line">
                            &nbsp;Корпус<font color="red"><b>*</b></font>: <input type='text' name='building' value='<%=user.getBuilding()%>' size='9' class="input-style"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1
                        </div>
                        <div class="profile-line">
                            <p>&nbsp;Секция<font color="red"><b>*</b></font>: <input type='text' name='section' value='<%=user.getSection()%>' size='9' class="input-style"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;7
                        </div>
                        <div class="profile-line">
                            <p>&nbsp;&nbsp;&nbsp;&nbsp;Этаж<font color="red"><b>*</b></font>: <input type='text' name='floor' value='<%=user.getFloor()%>' size='9' class="input-style"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2
                        </div>
                        <div class="profile-line">
                            <p>Квартира: <input type='text' name='flat' value='<%=user.getFlat()%>' size='9' class="input-style"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2
                        </div>
                        <div class="profile-line">
                            <input type='hidden' name='action' value='update'>
                            <input type='submit' value=' Сохранить ' class='submit-profile'>
                        </div>
                        <BR>
                        <BR>
                    </form>
                    <BR>
                </div>
            </main><!-- .content -->
        </div><!-- .container-->

        <!--aside class="right-sidebar">
            <BR><BR>
        </aside><!-- .right-sidebar -->

    </div><!-- .middle-->

</div><!-- .wrapper -->

<!--footer class="footer">
    Есть вопросы?<BR><a href='https://vk.com/id<%=ADMIN_VK_ID%>' target=_blank>Пишите</a>
</footer><!-- .footer -->


</body>
</html>
