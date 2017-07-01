<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 28.06.2017
  Time: 13:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link href="../styles.css" rel="stylesheet">

    <!-- Put this script tag to the <head> of your page -->
    <script type="text/javascript" src="//vk.com/js/api/openapi.js?146"></script>

    <script type="text/javascript">
        VK.init({apiId: 6091606});
    </script>

</head>
<body>


<!-- Put this div tag to the place, where Auth block will be -->
<!--table width="100%" height="100%" border="5">
<tr height="100%"><td align="center" valign="center" height="100%"> Пожалуйста, войдите через VK:<BR-->
<div class="center-div">Чтобы продолжить, войдите, пожалуйста, через VK:</div>
<div id="vk_auth" class="center-div"></div>

<script type="text/javascript">
    VK.Widgets.Auth("vk_auth", {authUrl: '/verify'});
</script>
<!--/td></tr>
</table-->


</body>
</html>
