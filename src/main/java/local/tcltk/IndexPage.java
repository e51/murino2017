package local.tcltk;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import local.tcltk.model.*;

/**
 * Created by user on 24.06.2017.
 */
//@WebServlet(name = "IndexPage")
public class IndexPage extends HttpServlet implements Constants {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        User user = null;
        String action = null;
        String htmlPage = "";

        HttpSession session = request.getSession();
        user = (User) session.getAttribute("user");
        action = request.getParameter("action");

        if (user == null) {
            action = null;
        }


        if ("list".equals(action)) {
            //
//            System.out.println("Token: " + user.getToken());

            if (!user.checkCompleteData()) {
                htmlPage = HTMLHelper.makeCreateUserPage(user);
            } else {
                htmlPage = HTMLHelper.makeHTMLPage(user);
            }
        } else if ("update".equals(action)) {
            // update user data and redirect to main page
            user.setBuilding(Integer.valueOf(request.getParameter("building")));
            user.setSection(Integer.valueOf(request.getParameter("section")));
            user.setFloor(Integer.valueOf(request.getParameter("floor")));
            user.setFlat(Integer.valueOf(request.getParameter("flat")));

            DatabaseManager.updateUserInDB(user);

            if (!user.checkCompleteData()) {
                htmlPage = HTMLHelper.makeCreateUserPage(user);
            } else {
                htmlPage = HTMLHelper.makeHTMLPage(user);
            }
        } else if ("refresh".equals(action)) {
            // just refresh neighbours page
            htmlPage = HTMLHelper.makeHTMLPage(user);

        } else {
            // no action - redirect to index page

            String contextPath = SITE_URL;
            String contextParams = "";
            response.sendRedirect(response.encodeRedirectURL(contextPath + contextParams));

            return;

        }

        session.setAttribute("user", user);

//        if (!user.checkCompleteData()) {
//            htmlPage = HTMLHelper.makeCreateUserPage(user);
//        } else {
//            htmlPage = HTMLHelper.makeHTMLPage(user);
//        }

        out.println(htmlPage);
//        out.close();
    }


}
