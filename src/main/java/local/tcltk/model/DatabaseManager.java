package local.tcltk.model;

import local.tcltk.Constants;
import local.tcltk.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
