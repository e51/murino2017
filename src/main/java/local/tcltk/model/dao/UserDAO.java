package local.tcltk.model.dao;

import local.tcltk.model.domain.User;
import local.tcltk.exceptions.DAOException;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;

import static local.tcltk.Constants.TABLE_NAME;

public class UserDAO {
    private static final Logger logger = Logger.getLogger(UserDAO.class);
    private DataSource ds;

    public UserDAO() {
//        this.ds = DatabaseManager.getDataSource();
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

}
