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
@WebServlet(name = "IndexPage")
public class IndexPage extends HttpServlet implements Constants {
    private User user;


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String htmlPage = "";

        String action = request.getParameter("action");
        String state = request.getParameter("state");

        if ("auth".equals(action)) {
            // find user in Database and show neighbours page

            // read vk id
            String vk_id = request.getParameter("vk_id");

            if (vk_id == null) {
                System.out.println("[auth] vk_id == null");
                // if no valid vk_id - redirect to auth page again
                htmlPage = makeAuthPage();
            } else {
                System.out.println("[auth] else - " + vk_id);
                // vk_id is ok (present)
                // looking for user data in the Database
                user = getUserFromDB(vk_id);
                if (user == null) {
                    // no user found - make a new user
                    System.out.println("[auth] no user found in DB");

                    // create object with vk_id and default fields
                    user = new User(vk_id, 0, 0, 0, 0);

                    System.out.println("[auth] " + user);

                    createNewUserDB(user);

                    htmlPage = makeCreateUserPage();
                } else {
                    htmlPage = makeHTMLPage();
                }
            }
        } else if ("update".equals(action)) {
            // update user data and redirect to main page
            user.setBuilding(Integer.valueOf(request.getParameter("building")));
            user.setSection(Integer.valueOf(request.getParameter("section")));
            user.setFloor(Integer.valueOf(request.getParameter("floor")));
            user.setFlat(Integer.valueOf(request.getParameter("flat")));

            updateUserInDB(user);

            htmlPage = makeHTMLPage();
        } else if ("123456".equals(state)) {
            out.println(request.getParameter("access_token"));
            out.println("<BR>");
            out.println(request.getParameter("expires_in"));
            out.println("<BR>");
            out.println(request.getParameter("user_id"));
            out.println("<BR>");
            out.println(request.getParameter("state"));
            out.println("<BR>");


        } else if ("refresh".equals(action)) {
//            out.println("<H1>refresh action</H1>");
            // just refresh neighbours page
            htmlPage = makeHTMLPage();

        } else {
            // no action - first visit index page
//            String contextPath = "https://oauth.vk.com/authorize";
//            String contextParams = "?client_id=6091606&redirect_uri=http://sosed.spb.ru/vkcheck&display=page&response_type=code&v=5.65&state=get_code";
//            response.sendRedirect(response.encodeRedirectURL(contextPath + contextParams));
            String contextPath = "http://sosed.spb.ru/auth.jsp";
            String contextParams = "";
            response.sendRedirect(response.encodeRedirectURL(contextPath + contextParams));

//            htmlPage = makeAuthPage();
        }

        out.println(htmlPage);
    }


    /**
     * Insert a new user record in the Database
     * @param user
     */
    private void createNewUserDB(User user) {

        Connection connection = DatabaseManager.getConnection();
        Statement statement = null;

        if (connection == null) {
            // error create connection
            System.out.println("[INSERT] Error getting connection");
            return;
        }

        try {
            //STEP 4: Execute a query
            System.out.println("Creating INSERT statement...");
            statement = connection.createStatement();
            String sql;

            sql = "INSERT into neighbours (id, vk_id, building, section, floor, flat)" +
                    " VALUES (DEFAULT," +
                    " '" + user.getVk_id() + "'," +
                    " " + user.getBuilding() + "," +
                    " " + user.getSection() + "," +
                    " " + user.getFloor() + "," +
                    " " + user.getFlat() + ")";

            int num = statement.executeUpdate(sql); // -1 == ERROR!

            System.out.println(num + " rows affected!");

            //STEP 6: Clean-up environment
            statement.close();
            connection.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(statement!=null)
                    statement.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(connection!=null)
                    connection.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("INSERT finished");
    }

    /**
     * Update a user database record
     * @param user
     */
    private void updateUserInDB(User user) {

        Connection connection = DatabaseManager.getConnection();
        Statement statement = null;

        if (connection == null) {
            // error create connection
            return;
        }

        try {
            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            statement = connection.createStatement();
            String sql;

            sql = "UPDATE neighbours SET" +
                    " building = '" + user.getBuilding() + "'," +
                    " section = '" + user.getSection() + "'," +
                    " floor = '" + user.getFloor() + "'," +
                    " flat = '" + user.getFlat() + "'" +
                    " WHERE vk_id = '" + user.getVk_id() + "';";

            int num = statement.executeUpdate(sql);

            System.out.println(num + " rows affected!");

            //STEP 6: Clean-up environment
            statement.close();
            connection.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(statement!=null)
                    statement.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(connection!=null)
                    connection.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("UPDATE finished");
    }

    /**
     * Find user in database by vk_id
     * @param lookingID
     * @return User
     */
    private User getUserFromDB(String lookingID) {
        User user = null;

        Connection connection = DatabaseManager.getConnection();
        Statement statement = null;

        if (connection == null) {
            // error create connection
            return null;
        }

        try{
            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            statement = connection.createStatement();
            String sql;
            sql = "SELECT * FROM neighbours WHERE vk_id = '" + lookingID + "'";

            ResultSet rs = statement.executeQuery(sql);

            System.out.println(rs.getWarnings());

            //STEP 5: Extract data from result set
            while(rs.next()){
                //Retrieve by column name
                System.out.println("got something");

                int id  = rs.getInt("id");
                String vk_id = rs.getString("vk_id");
                int building = rs.getInt("building");
                int section = rs.getInt("section");
                int floor = rs.getInt("floor");
                int flat = rs.getInt("flat");

                user = new User(vk_id, building, section, floor, flat);
                System.out.println(user);
            }
            //STEP 6: Clean-up environment
            rs.close();
            statement.close();
            connection.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(statement!=null)
                    statement.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(connection!=null)
                    connection.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");

        return user;
    }

    /**
     * Get neighbours list to the user with sql query
     * @param user
     * @param sql
     * @return
     */
    private List<User> getNeighboursFromDB(User user, String sql) {
        List<User> users = new ArrayList<User>();

        Connection connection = DatabaseManager.getConnection();
        Statement statement = null;

        if (connection == null) {
            // error create connection
            return users;
        }

        try{
            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(sql);

            System.out.println(rs.getWarnings());

            //STEP 5: Extract data from result set
            while(rs.next()){
                //Retrieve by column name
                System.out.println("got something");

                int id  = rs.getInt("id");
                String vk_id = rs.getString("vk_id");
                int building = rs.getInt("building");
                int section = rs.getInt("section");
                int floor = rs.getInt("floor");
                int flat = rs.getInt("flat");

                User neighbour = new User(vk_id, building, section, floor, flat);
                if (!neighbour.equals(user)) {
                    users.add(neighbour);
                }
            }
            //STEP 6: Clean-up environment
            rs.close();
            statement.close();
            connection.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(statement!=null)
                    statement.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(connection!=null)
                    connection.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");

        return users;
    }


    /**
     * HTML authentication page (index page)
     * @return
     */
    private String makeAuthPage() {
        StringBuilder sb = new StringBuilder();

        sb.append("<html>\n" +
                "\n" +
                "<table width=100% height=100%>\n" +
                "    <tr>\n" +
                "        <td align=center valign=center>\n" +
                "            <BR>\n" +
                "            <form action='/' method='post'>\n" +
                "                Ваш id: <input type='text' name='vk_id'>\n" +
                "                <input type='hidden' name='action' value='auth'>\n" +
                "                <input type='submit' value=' Войти '>\n" +
                "            </form>\n" +
                "            <BR>\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "</table>\n" +
                "\n" +
                "</html>");

        return sb.toString();
    }

    /**
     * HTML new user details page. If there is a new user, he should fill his data.
     * @return
     */
    private String makeCreateUserPage() {
        StringBuilder sb = new StringBuilder();

        sb.append("<html>\n" +
                "\n" +
                "<table width=100% height=100%>\n" +
                "    <tr>\n" +
                "        <td align=center valign=center>\n" +
                "            Впервые у нас?<BR>\n" +
                "            <BR>\n" +
                "            <form action='/' method='post' align=center>\n" +
                "                <p>Корпус: <input type='text' name='building' value='0'></p>\n" +
                "                <p>Секция: <input type='text' name='section' value='0'></p>\n" +
                "                <p>Этаж: <input type='text' name='floor' value='0'></p>\n" +
                "                <p>Квартира: <input type='text' name='flat' value='0'></p>\n" +
                "                <p><input type='hidden' name='action' value='update'></p>\n" +
                "                <p><input type='submit' value='Применить'></p>\n" +
                "            </form>\n" +
                "            <BR>\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "</table>\n" +
                "\n" +
                "</html>");

        return sb.toString();
    }

    /**
     * HTML main info page (neighbours information)
     * @return
     */
    private String makeHTMLPage() {
        StringBuilder sb = new StringBuilder();

        // allow update user data on the info page
        String dataSection;

        if (true) {
            dataSection = "" +
                    "            <H1>Я живу:</H1><BR>\n" +
                    "            <BR>\n" +
                    "            <form action='/' method='post' align=center>\n" +
                    "                <p>Корпус: <input type='text' name='building' value='" + user.getBuilding() + "'></p>\n" +
                    "                <p>Секция: <input type='text' name='section' value='" + user.getSection() + "'></p>\n" +
                    "                <p>Этаж: <input type='text' name='floor' value='" + user.getFloor() + "'></p>\n" +
                    "                <p>Квартира: <input type='text' name='flat' value='" + user.getFlat() + "'></p>\n" +
                    "                <p><input type='hidden' name='action' value='update'></p>\n" +
                    "                <p><input type='submit' value='Применить'></p>\n" +
                    "            </form>\n" +
                    "" +
                    "            <BR>\n" +
                    "            <form action='/' method='post' align=center>\n" +
                    "                <p><input type='hidden' name='action' value='refresh'></p>\n" +
                    "                <p><input type='submit' value='Обновить'></p>\n" +
                    "            </form>\n" +
                    "" +
                    "";
        } else {
            dataSection = "" +
                    "            <H1>Я живу:</H1><BR>\n" +
                    "            <BR>\n" +
                    "            <p>Корпус: " + user.getBuilding() + "</p>\n" +
                    "            <p>Секция: " + user.getSection() + "</p>\n" +
                    "            <p>Этаж: " + user.getFloor() + "</p>\n" +
                    "            <p>Квартира: " + user.getFlat() + "</p>\n" +
                    "            <BR>\n";

        }

        sb.append("<html>\n" +
                "\n" +
                "<table width=100% height=100%>\n" +
                "    <tr>\n" +
                "        <td valign=center width=30%>\n" +
                "" + dataSection +
                "        </td>\n" +
                "        <td align=center valign=center width=70%>\n" +
                "            <table width=100% height=100%>\n" +
                "                <tr>\n" +
                "                    <td>\n" +
                "                        <H1>Соседи сверху:</H1><BR>\n" + getNeighboursTopHTML() +
                "                    </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td>\n" +
                "                        <H1>Соседи по площадке:</H1><BR>\n" + getNeighboursSectionHTML() +
                "                    </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td>\n" +
                "                        <H1>Соседи снизу:</H1><BR>\n" + getNeighboursBottomHTML() +
                "                    </td>\n" +
                "                </tr>\n" +
                "            </table>\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "</table>\n" +
                "\n" +
                "" +
                "" +
                "" +
                "</html>\n" +
                "");


        return sb.toString();

    }


    /**
     * HTML part - section neighbours
     * @return
     */
    private String getNeighboursSectionHTML() {
        String sql = "SELECT * FROM neighbours WHERE" +
                " building = '" + user.getBuilding() + "' AND" +
                " section = '" + user.getSection() + "' AND" +
                " floor = '" + user.getFloor() + "'";

        List<User> users = getNeighboursFromDB(user, sql);

        StringBuilder sb = new StringBuilder();

        for (User user : users) {

            sb.append("<p> " + user.getVk_id() + " </p>");
        }

        return sb.toString();
    }

    /**
     * HTML part - top floor neighbours
     * @return
     */
    private String getNeighboursTopHTML() {
        String sql = "SELECT * FROM neighbours WHERE" +
                " building = '" + user.getBuilding() + "' AND" +
                " section = '" + user.getSection() + "' AND" +
                " floor = '" + (user.getFloor() + 1) + "' AND" +
                " flat = '" + user.getFlat() + "'";

        List<User> users = getNeighboursFromDB(user, sql);

        StringBuilder sb = new StringBuilder();

        for (User user : users) {

            sb.append("<p> " + user.getVk_id() + " </p>");
        }

        return sb.toString();
    }

    /**
     * HTML part - bottom floor neighbours
     * @return
     */
    private String getNeighboursBottomHTML() {

        String sql = "SELECT * FROM neighbours WHERE" +
                " building = '" + user.getBuilding() + "' AND" +
                " section = '" + user.getSection() + "' AND" +
                " floor = '" + (user.getFloor() - 1) + "' AND" +
                " flat = '" + user.getFlat() + "'";

        List<User> users = getNeighboursFromDB(user, sql);

        StringBuilder sb = new StringBuilder();

        for (User user : users) {

            sb.append("<p> " + user.getVk_id() + " </p>");
        }

        return sb.toString();
    }

}
