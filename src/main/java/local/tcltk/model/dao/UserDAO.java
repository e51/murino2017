package local.tcltk.model.dao;

import local.tcltk.model.domain.User;
import local.tcltk.exceptions.DAOException;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static local.tcltk.Constants.TABLE_NAME;

public class UserDAO {
    private static final Logger logger = Logger.getLogger(UserDAO.class);
    private DataSource ds;

    public UserDAO() {
        this(DatabaseManager.getDataSource());
    }

    public UserDAO(DataSource ds) {
        this.ds = ds;
    }

    /**
     * Find user in database by vk_id
     * @param vk_id
     * @return User
     */
    public User getEntityByVkId(long vk_id) throws DAOException {
        long time1 = System.nanoTime();
        User user = null;

        try (Connection connection = ds.getConnection()) {
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
        } catch (SQLException e) {
//            e.printStackTrace();
            logger.error(String.format("[getEntityByVkId] Error getting user from DB. %s: %s", e.getClass().getSimpleName(), e.getMessage()));
            throw new DAOException(String.format("[getEntityByVkId] Error getting user from DB. %s: %s", e.getClass().getSimpleName(), e.getMessage()), e);
        }

        logger.info(String.format("Elapsed time: %dms", (System.nanoTime() - time1) / 1_000_000));

        return user;
    }

    public List<User> getRandomNeighbours(int count) throws DAOException {
        long time1 = System.nanoTime();
        List<User> users = new ArrayList<>();

        try (Connection connection = ds.getConnection()) {
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
        } catch (SQLException e) {
//            e.printStackTrace();
            logger.error(String.format("[getUserFromDB] Error getting user from DB. %s: %s", e.getClass().getSimpleName(), e.getMessage()));
            throw new DAOException(String.format("[getUserFromDB] Error getting user from DB. %s: %s", e.getClass().getSimpleName(), e.getMessage()), e);
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
    private List<User> getNeighboursFromDB(User user, String sql) {
        long time1 = System.nanoTime();
        List<User> neighbours = new ArrayList<>();

        if (user.isValid()) {
            try (Connection connection = ds.getConnection()) {
                try (Statement statement = connection.createStatement()) {
                    ResultSet rs = statement.executeQuery(sql);

                    // Extract data from result set
                    while (rs.next()) {
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
            } catch (SQLException e) {
                logger.error(String.format("[getNeighboursFromDB] Error getting neighbours from DB. %s: %s", e.getClass().getSimpleName(), e.getMessage()));
            }
        }

        logger.info(String.format("Elapsed time: %dms", (System.nanoTime() - time1) / 1_000_000));
        return neighbours;
    }

    public List<User> getTopNeighbours(User user) {
        String sql = "SELECT * FROM " +  TABLE_NAME + " WHERE" +
                " building = '" + user.getBuilding() + "' AND" +
                " section = '" + user.getSection() + "' AND" +
                " floor = '" + (user.getFloor() + 1) + "'";
//                " floor = '" + (user.getFloor() + 1) + "' AND" +
//                " flat = '" + user.getFlat() + "'";

        if (user.isUseFlat() && user.getFlat() != 0) {
//        if (user.getFlat() != 0) {
            sql = sql + " AND" +
                    " flat = '" + user.getFlat() + "'";
        }

        List<User> neighbours = getNeighboursFromDB(user, sql);

        return neighbours;
    }

    public List<User> getFloorNeighbours(User user) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE" +
                " building = '" + user.getBuilding() + "' AND" +
                " section = '" + user.getSection() + "' AND" +
                " floor = '" + user.getFloor() + "'";

        List<User> neighbours = getNeighboursFromDB(user, sql);

        return neighbours;
    }

    public List<User> getBottomNeighbours(User user) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE" +
                " building = '" + user.getBuilding() + "' AND" +
                " section = '" + user.getSection() + "' AND" +
                " floor = '" + (user.getFloor() - 1) + "'";
//                " floor = '" + (user.getFloor() - 1) + "' AND" +
//                " flat = '" + user.getFlat() + "'";

        if (user.isUseFlat() && user.getFlat() != 0) {
//        if (user.getFlat() != 0) {
            sql = sql + " AND" +
                    " flat = '" + user.getFlat() + "'";
        }

        List<User> neighbours = getNeighboursFromDB(user, sql);

        return neighbours;
    }


    /**
     * Insert a new user record in the Database
     * @param user
     */
    public void insert(User user) throws DAOException {
        try (Connection connection = ds.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                logger.info("[insert] Creating INSERT statement...");

                String sql = String.format("INSERT into %s (vk_id, building, section, floor, flat, updates) VALUES (%d, %d, %d, %d, %d, %d);",
                        TABLE_NAME, user.getVk_id(), user.getBuilding(), user.getSection(), user.getFloor(), user.getFlat(), user.getUpdates());

                // Execute a query
                int num = statement.executeUpdate(sql); // -1 == ERROR!

                if (num != 1) {
                    logger.error(String.format("[insert] INSERT FAILED! %d rows affected!", num));
                    // throw Exception?
                    throw new SQLClientInfoException();
                } else {
                    logger.info(String.format("[insert] INSERT SUCCESS. %d rows affected!", num));
                }
            }
        } catch (SQLException e) {
            // Handle errors for JDBC, Class.forName
            logger.error(String.format("[insert] INSERT FAILED! %s: %s", e.getClass().getSimpleName(), e.getMessage()));
            throw new DAOException(String.format("[insert] INSERT FAILED! %s: %s", e.getClass().getSimpleName(), e.getMessage()), e);
        }

    }

    /**
     * Update a user database record
     * @param user
     */
    public void update(User user) throws DAOException {
        try (Connection connection = ds.getConnection()) {
            try (Statement statement = connection.createStatement()) {

                String sql = String.format("UPDATE %s SET building=%d, section=%d, floor=%d, flat=%d, updates=%d WHERE vk_id=%d;",
                        TABLE_NAME, user.getBuilding(), user.getSection(), user.getFloor(), user.getFlat(), user.getUpdates(), user.getVk_id());

                logger.info("[update] SQL: " + sql);

                int num = statement.executeUpdate(sql);

                if (num != 1) {
                    logger.error(String.format("[update] UPDATE FAILED! %d rows affected!", num));
                    // throw Exception?
                    throw new SQLClientInfoException();
                } else {
                    logger.info(String.format("[update] UPDATE SUCCESS. %d rows affected!", num));
                }
            }
        } catch (SQLException e) {
            logger.error(String.format("[update] UPDATE FAILED! %s: %s", e.getClass().getSimpleName(), e.getMessage()));
            throw new DAOException(String.format("[update] UPDATE FAILED! %s: %s", e.getClass().getSimpleName(), e.getMessage()), e);
        }

    }

    /**
     * Get count of elements for some sql query
     * @param sql - query for execute
     * @return int count
     */
    private int getCount(String sql) {
        int result = 0;

        try (Connection connection = ds.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery(sql);

                while(rs.next()){
                    result = rs.getInt(1);
                }

                rs.close();
            }
        } catch (SQLException e) {
            logger.error(String.format("[getCount] Error: %s, %s ", e.getClass().getSimpleName(), e.getMessage()));
        }

        return result;
    }

    /**
     * Get users count by building number
     * @param building: 0 = all
     * @return
     */
    public int getUsersCountByBuilding(int building) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM " + TABLE_NAME);

        if (building != 0) {
            sql.append(" WHERE building = " + building);
        }

        return getCount(sql.toString());
    }


}
