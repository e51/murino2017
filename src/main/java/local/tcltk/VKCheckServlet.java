package local.tcltk;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

/**
 * Created by user on 27.06.2017.
 */
@WebServlet(name = "VKCheckServlet")
public class VKCheckServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

//        for (String name : response.getHeaderNames()) {
//            out.println(name);
//            out.println("<BR>");
//        }

        out.println(request.getContextPath());
        out.println("<BR>");
        out.println(request.getRequestURI());
        out.println("<BR>");
        out.println(request.getRequestURL());
        out.println("<BR>");
        out.println("uid: " + request.getParameter("uid"));
        out.println("<BR>");
        out.println("first_name: " + request.getParameter("first_name"));
        out.println("<BR>");
        out.println("last_name: " + request.getParameter("last_name"));
        out.println("<BR>");
        out.println("photo: <img src='" + request.getParameter("photo") + "'>");
        out.println("<BR>");
        out.println("photo_rec: <img src='" + request.getParameter("photo_rec") + "'>");
        out.println("<BR>");
        out.println("hash: " + request.getParameter("hash"));
        out.println("<BR>");
        out.println("<a href='http://sosed.spb.ru/'>sosed.spb.ru</a>");


//        String contextPath = "https://oauth.vk.com/access_token";
//        String contextParams = "?client_id=6091606&client_secret=Ma&redirect_uri=http://sosed.spb.ru/vkcheck&code=" + request.getParameter("code") + "";
//        response.sendRedirect(response.encodeRedirectURL(contextPath + contextParams));


//        URL oracle = new URL(contextPath + contextParams);
//        BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
//
//        String inputLine;
//        while ((inputLine = in.readLine()) != null) {
//            System.out.println(inputLine);
//            out.println(inputLine + "<BR>");
//        }
//        in.close();


        String contextPath = "http://sosed.spb.ru/";
        String contextParams = "?action=auth&vk_id=" + request.getParameter("uid");
        response.sendRedirect(response.encodeRedirectURL(contextPath + contextParams));


    }
}
