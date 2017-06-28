<%@ page import="local.tcltk.Solution" %>
<%@ page import="java.io.PrintWriter" %>

<html>
<body>
<%--<h2><%=Solution.getMessage()%> Neighbours!</h2>--%>

<%
    PrintWriter out2 = response.getWriter();


    for (String name : response.getHeaderNames()) {
        out2.println(name);
        out2.println("<BR>");

    }

    out2.println(request.getHeader("location"));
    out2.println("<BR>");
    out2.println(request.getContextPath());
    out2.println("<BR>");
    out2.println(request.getRequestURI());
    out2.println("<BR>");
    out2.println(request.getRequestURL());
    out2.println("<BR>");
    out2.println(request.getParameter("access_token"));
    out2.println("<BR>");
    out2.println(request.getParameter("expires_in"));
    out2.println("<BR>");
    out2.println(request.getParameter("user_id"));
    out2.println("<BR>");
    out2.println(request.getParameter("state"));
    out2.println("<BR>");
    out2.println("<a href='http://sosed.spb.ru/'>sosed.spb.ru</a>");


%>

</body>
</html>
