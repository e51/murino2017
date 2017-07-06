package local.tcltk.model;

import local.tcltk.Constants;
import local.tcltk.User;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 24.06.2017.
 */
public class DatabaseManager implements Constants {
    private static final Logger logger = Logger.getLogger(DatabaseManager.class);

    /**
     * Get connection to the Database
     * @return Connection
     */
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        //STEP 2: Register JDBC driver
        Class.forName(JDBC_DRIVER);

        //STEP 3: Open a connection
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }


    /**
     * Insert a new user record in the Database
     * @param user
     */
    public static void createNewUserDB(User user) {

        try (Connection connection = DatabaseManager.getConnection()) {
            try (Statement statement = connection.createStatement()) {

                //STEP 4: Execute a query
                logger.info("Creating INSERT statement...");

                String sql = "INSERT into " + TABLE_NAME + " (id, vk_id, building, section, floor, flat)" +
                        " VALUES (DEFAULT," +
                        " " + user.getVk_id() + "," +
                        " " + user.getBuilding() + "," +
                        " " + user.getSection() + "," +
                        " " + user.getFloor() + "," +
                        " " + user.getFlat() + ")";

                int num = statement.executeUpdate(sql); // -1 == ERROR!

                logger.info(num + " rows affected!");
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("Error connection.createStatement()");
            }
        } catch (SQLException e) {
            //Handle errors for JDBC
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }

        logger.info("INSERT finished");
    }



    /**
     * Update a user database record
     * @param user
     */
    public static void updateUserInDB(User user) {

        try (Connection connection = DatabaseManager.getConnection()) {
//        if (connection == null) {
//            // error create connection
//            return;
//        }
            // Может ли connection быть null?

            try (Statement statement = connection.createStatement()) {
                String sql = "UPDATE " + TABLE_NAME + " SET" +
                        " building = " + user.getBuilding() + "," +
                        " section = " + user.getSection() + "," +
                        " floor = " + user.getFloor() + "," +
                        " flat = " + user.getFlat() + "," +
                        " updates = " + user.getUpdates() +
                        " WHERE vk_id = " + user.getVk_id() + ";";

                int num = statement.executeUpdate(sql);

                logger.info(num + " rows affected!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Error connection.createStatement()");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        logger.info("UPDATE finished");
    }

    /**
     * Find user in database by vk_id
     * @param lookingID
     * @return User
     */
    public static User getUserFromDB(long lookingID) {
        User user = null;

        try (Connection connection = DatabaseManager.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM " + TABLE_NAME + " WHERE vk_id = " + lookingID;

                ResultSet rs = statement.executeQuery(sql);

//            System.out.println("[getUserFromDB] warnings: " + rs.getWarnings());

                //STEP 5: Extract data from result set
                while(rs.next()){
                    //Retrieve by column name
                    int id  = rs.getInt("id");
                    long vk_id = rs.getInt("vk_id");
                    int building = rs.getInt("building");
                    int section = rs.getInt("section");
                    int floor = rs.getInt("floor");
                    int flat = rs.getInt("flat");
                    int updates = rs.getInt("updates");

                    user = new User(vk_id, building, section, floor, flat, updates);
                    logger.info("[getUserFromDB] found user: " + user);
                }
                //STEP 6: Clean-up environment
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("Error connection.createStatement()");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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

        try (Connection connection = DatabaseManager.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery(sql);

//            System.out.println(rs.getWarnings());

                //STEP 5: Extract data from result set
                while(rs.next()){
                    //Retrieve by column name
                    int id  = rs.getInt("id");
                    int vk_id = rs.getInt("vk_id");
                    int building = rs.getInt("building");
                    int section = rs.getInt("section");
                    int floor = rs.getInt("floor");
                    int flat = rs.getInt("flat");
                    int updates = rs.getInt("updates");

                    User neighbour = new User(vk_id, building, section, floor, flat, updates);
                    if (!neighbour.equals(user)) {
                        users.add(neighbour);
                    }
                }
                //STEP 6: Clean-up environment
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("Error connection.createStatement()");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//        System.out.println("Goodbye!");
        return users;
    }


    private static int getCount(String sql) {
        int result = 0;

        try (Connection connection = DatabaseManager.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery(sql);

//            System.out.println(rs.getWarnings());
//                logger.info("[getCount] rs: " + rs);

                //STEP 5: Extract data from result set
                while(rs.next()){
                    result = rs.getInt(1);
                }
                //STEP 6: Clean-up environment
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("Error connection.createStatement()");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * building = int - building #
     *      or = 0 - all buildings
     * @param building
     * @return
     */
    public static int getUsersCountByBuilding(int building) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM " + TABLE_NAME);

        if (building != 0) {
            sql.append(" WHERE building = " + building);
        }

        return getCount(sql.toString());
    }


    public static int getUsersCountByBuildingAndFloor(int building, int floor) {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE building = " + building + " AND floor = " + floor;

        return getCount(sql);
    }



}
