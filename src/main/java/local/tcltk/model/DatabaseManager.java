package local.tcltk.model;

import local.tcltk.Constants;
import local.tcltk.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 24.06.2017.
 */
public class DatabaseManager implements Constants {
//    private static DatabaseManage instance;

    /**
     * Get connection to the Database
     * @return Connection
     */
    public static Connection getConnection() {
        Connection connection = null;

        try {
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }

        return connection;
    }


    /**
     * Insert a new user record in the Database
     * @param user
     */
    public static void createNewUserDB(User user) {

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
    public static void updateUserInDB(User user) {

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
    public static User getUserFromDB(String lookingID) {
        User user = null;

        Connection connection = DatabaseManager.getConnection();
        Statement statement = null;

        if (connection == null) {
            // error create connection
            return null;
        }

        try{
            //STEP 4: Execute a query
//            System.out.println("Creating statement...");
            statement = connection.createStatement();
            String sql;
            sql = "SELECT * FROM neighbours WHERE vk_id = '" + lookingID + "'";

            ResultSet rs = statement.executeQuery(sql);

            System.out.println(rs.getWarnings());

            //STEP 5: Extract data from result set
            while(rs.next()){
                //Retrieve by column name
//                System.out.println("got something");

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
//        System.out.println("Goodbye!");

        return user;
    }

    /**
     * Get neighbours list to the user with sql query
     * @param user
     * @param sql
     * @return
     */
    public static List<User> getNeighboursFromDB(User user, String sql) {
        List<User> users = new ArrayList<User>();

        Connection connection = DatabaseManager.getConnection();
        Statement statement = null;

        if (connection == null) {
            // error create connection
            return users;
        }

        try{
            //STEP 4: Execute a query
//            System.out.println("Creating statement...");
            statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(sql);

//            System.out.println(rs.getWarnings());

            //STEP 5: Extract data from result set
            while(rs.next()){
                //Retrieve by column name
//                System.out.println("got something");

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
//        System.out.println("Goodbye!");

        return users;
    }


//    private static ResultSet getSomeQuery(String sql) {
//
//    }

    public static String getUsersCountByBuilding(int building) {
        String sql = "SELECT COUNT(*) FROM neighbours WHERE" +
                " building = " + building;

        String result = null;

        Connection connection = DatabaseManager.getConnection();
        Statement statement = null;

        if (connection == null) {
            // error create connection
            return null;
        }

        try{
            //STEP 4: Execute a query
//            System.out.println("Creating statement...");
            statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(sql);

//            System.out.println(rs.getWarnings());
            System.out.println(rs);
            //STEP 5: Extract data from result set
            while(rs.next()){
                System.out.println("********");
//                System.out.println(rs.getInt(0));
                System.out.println(rs.getInt(1));
                System.out.println(rs.getInt("count"));
                System.out.println("********");

                result = String.valueOf(rs.getInt(1));

                //Retrieve by column name
//                System.out.println("got something");

//                result = rs.
//                int id  = rs.getInt("id");
//                String vk_id = rs.getString("vk_id");
//                int building = rs.getInt("building");
//                int section = rs.getInt("section");
//                int floor = rs.getInt("floor");
//                int flat = rs.getInt("flat");
//
//                User neighbour = new User(vk_id, building, section, floor, flat);
//                if (!neighbour.equals(user)) {
//                    users.add(neighbour);
//                }
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
//        System.out.println("Goodbye!");

        return result;

    }

//    public static void UpdateUser(User user) throws SQLException {
//
//        Connection con = null;
//        PreparedStatement pstmt = null;
//
//        try {
//            con = DriverManager.getConnection("jdbc:default:connection");
//
//            pstmt = con.prepareStatement(
//                    "UPDATE neighbours " +
//                            "SET korp = ? " +
//                            "SET section = ? " +
//                            "SET floor = ? " +
//                            "SET flat = ? " +
//                            "WHERE vk_id = ?");
//
//            pstmt.setInt(1, user.getBuilding());
//            pstmt.setInt(2, user.getSection());
//            pstmt.setInt(3, user.getFloor());
//            pstmt.setInt(4, user.getFlat());
//            pstmt.setString(5, user.getVk_id());
//            pstmt.executeUpdate();
//        } finally {
//            if (pstmt != null) pstmt.close();
//        }
//    }



}
