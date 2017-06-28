<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 26.06.2017
  Time: 11:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

    <form action="/hello" method="post">
        <p>Корпус: <input type="text" name="building"></p>
        <p>Секция: <input type="text" name="section"></p>
        <p>Этаж: <input type="text" name="floor"></p>
        <p>Квартира: <input type="text" name="flat"></p>
        <p><input type="submit" value="Применить"></p>
    </form>

</body>
</html>
