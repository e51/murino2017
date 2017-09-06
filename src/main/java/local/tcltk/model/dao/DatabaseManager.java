package local.tcltk.model.dao;

import local.tcltk.model.domain.User;
import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static local.tcltk.Constants.*;

/**
 * Created by user on 24.06.2017.
 */
public class DatabaseManager {
    private static final Logger logger = Logger.getLogger(DatabaseManager.class);
    private static Context context;
    private static DataSource ds;

    static {
        // Obtain our environment naming context
        Context initCtx = null;
        try {
            initCtx = new InitialContext();
            context = (Context) initCtx.lookup("java:comp/env");

            // Look up our data source
            ds = (DataSource) context.lookup("jdbc/database");

        } catch (NamingException e) {
            logger.error(String.format("Error getting DataSource. %s %s", e.getClass().getSimpleName(), e.getMessage()));
//            throw new DAOException("Error getting Datasource", e);
            e.printStackTrace();
        }
    }

    public static DataSource getDataSource() {
        return ds;
    }

    /**
     * Get connection to the Database
     * @return Connection
     */
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
/*
        // Register JDBC driver
        Class.forName(JDBC_DRIVER);

        // Open a connection
        return DriverManager.getConnection(DB_URL, USER, PASS);
*/

        return ds.getConnection();
    }

    /**
     * Insert a new user record in the Database
     * @param user
     */
/*
    public static void createNewUserDB(User user) throws SQLException, ClassNotFoundException {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                logger.info("[createNewUserDB] Creating INSERT statement...");

                String sql = String.format("INSERT into %s (vk_id, building, section, floor, flat, updates) VALUES (%d, %d, %d, %d, %d, %d);",
                        TABLE_NAME, user.getVk_id(), user.getBuilding(), user.getSection(), user.getFloor(), user.getFlat(), user.getUpdates());

                // Execute a query
                int num = statement.executeUpdate(sql); // -1 == ERROR!

                if (num != 1) {
                    logger.error(String.format("[createNewUserDB] INSERT FAILED! %d rows affected!", num));
                    // throw Exception?
                    throw new SQLClientInfoException();
                } else {
                    logger.info(String.format("[createNewUserDB] INSERT SUCCESS. %d rows affected!", num));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            // Handle errors for JDBC, Class.forName
            logger.error(String.format("[createNewUserDB] INSERT FAILED! %s: %s", e.getClass().getSimpleName(), e.getMessage()));
            throw e;
        }
    }
*/

    /**
     * Update a user database record
     * @param user
     */
/*
    public static void updateUserInDB(User user) throws SQLException, ClassNotFoundException {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (Statement statement = connection.createStatement()) {

                String sql = String.format("UPDATE %s SET building=%d, section=%d, floor=%d, flat=%d, updates=%d WHERE vk_id=%d;",
                        TABLE_NAME, user.getBuilding(), user.getSection(), user.getFloor(), user.getFlat(), user.getUpdates(), user.getVk_id());

                logger.info("[updateUserInDB] SQL: " + sql);

                int num = statement.executeUpdate(sql);

                if (num != 1) {
                    logger.error(String.format("[updateUserInDB] UPDATE FAILED! %d rows affected!", num));
                    // throw Exception?
                    throw new SQLClientInfoException();
                } else {
                    logger.info(String.format("[updateUserInDB] UPDATE SUCCESS. %d rows affected!", num));
                }
            }
        } catch (SQLException | ClassNotFoundException  e) {
            logger.error(String.format("[updateUserInDB] UPDATE FAILED! %s: %s", e.getClass().getSimpleName(), e.getMessage()));
            throw e;
        }
    }
*/

    /**
     * Find user in database by vk_id
     * @param vk_id
     * @return User
     */
    public static User getUserFromDB(long vk_id) {
        long time1 = System.nanoTime();
        User user = null;

        try (Connection connection = DatabaseManager.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                String sql = String.format("SELECT * FROM %s WHERE vk_id = %d", TABLE_NAME, vk_id);

                ResultSet rs = statement.executeQuery(sql);

                // Extract data from result set
                while(rs.next()){
                    //Retrieve by column name
                    user = new User(
                            rs.getInt("vk_id"),
                            rs.getInt("building"),
                            rs.getInt("section"),
                            rs.getInt("floor"),
                            rs.getInt("flat"),
                            rs.getInt("updates")
                    );
//                    logger.info("[getUserFromDB] found user: " + user);
                }
                // Clean-up environment
                rs.close();
            }
        } catch (SQLException | ClassNotFoundException e) {
//            e.printStackTrace();
            logger.error(String.format("[getUserFromDB] Error getting user from DB. %s: %s", e.getClass().getSimpleName(), e.getMessage()));
//            throw e;
        }

        logger.info(String.format("Elapsed time: %dms", (System.nanoTime() - time1) / 1_000_000));
        return user;
    }

    public static List<User> getRandomUsersFromDB(int count) {
        long time1 = System.nanoTime();
        List<User> users = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                String sql = String.format("SELECT * FROM %s ORDER BY RANDOM() LIMIT %d;", TABLE_NAME, count);

                ResultSet rs = statement.executeQuery(sql);

                // Extract data from result set
                while(rs.next()){
                    //Retrieve by column name
                    User user = new User(
                            rs.getInt("vk_id"),
                            rs.getInt("building"),
                            rs.getInt("section"),
                            rs.getInt("floor"),
                            rs.getInt("flat"),
                            rs.getInt("updates")
                    );
                    users.add(user);
//                    logger.info("[getUsersFromDB] found user: " + user);
                }
                // Clean-up environment
                rs.close();
            }
        } catch (SQLException | ClassNotFoundException e) {
//            e.printStackTrace();
            logger.error(String.format("[getUserFromDB] Error getting user from DB. %s: %s", e.getClass().getSimpleName(), e.getMessage()));
//            throw e;
        }

        logger.info(String.format("Elapsed time: %dms", (System.nanoTime() - time1) / 1_000_000));
        return users;
    }

    /**
     * Get neighbours list to the user with sql query
     * @param user
     * @param sql
     * @return
     */
    public static List<User> getNeighboursFromDB(User user, String sql) {
        long time1 = System.nanoTime();
        List<User> neighbours = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery(sql);

                // Extract data from result set
                while(rs.next()){
                    //Retrieve by column name
                    User neighbour = new User(
                            rs.getInt("vk_id"),
                            rs.getInt("building"),
                            rs.getInt("section"),
                            rs.getInt("floor"),
                            rs.getInt("flat"),
                            rs.getInt("updates")
                    );

                    if (!neighbour.equals(user)) {
                        neighbours.add(neighbour);
                    }
                }
                // Clean-up environment
                rs.close();
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(String.format("[getNeighboursFromDB] Error getting neighbours from DB. %s: %s", e.getClass().getSimpleName(), e.getMessage()));
        }

        logger.info(String.format("Elapsed time: %dms", (System.nanoTime() - time1) / 1_000_000));
        return neighbours;
    }

    /**
     * Get count of elements for some sql query
     * @param sql - query for execute
     * @return int count
     */
    private static int getCount(String sql) {
        int result = 0;

        try (Connection connection = DatabaseManager.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery(sql);

                while(rs.next()){
                    result = rs.getInt(1);
                }

                rs.close();
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(String.format("[getCount] Error: %s, %s ", e.getClass().getSimpleName(), e.getMessage()));
        }

        return result;
    }


    /**
     * Get users count by building number
     * @param building: 0 = all
     * @return
     */
    public static int getUsersCountByBuilding(int building) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM " + TABLE_NAME);

        if (building != 0) {
            sql.append(" WHERE building = " + building);
        }

        return getCount(sql.toString());
    }

    /**
     * Get users count by building number and floor number
     * @param building
     * @param floor
     * @return
     */
    public static int getUsersCountByBuildingAndFloor(int building, int floor) {
        String sql = String.format("SELECT COUNT(*) FROM %s WHERE building = %d AND floor = %d", TABLE_NAME, building, floor);

        return getCount(sql);
    }

}
