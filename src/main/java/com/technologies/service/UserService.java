package com.technologies.service;

import com.technologies.config.DbConfig;
import com.technologies.exception.AccountTransferException;
import com.technologies.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.DbUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD for user entity.
 */
@Slf4j
public class UserService {

    private final String GET_USER_BY_ID = "SELECT * FROM User WHERE userId = ? ";
    private final String GET_ALL_USERS = "SELECT * FROM User";
    private final String GET_USER_BY_NAME = "SELECT * FROM User WHERE userName = ? ";
    private final String ADD_NEW_USER = "INSERT INTO User (userName, userPhone, userEmail) VALUES (?, ?, ?)";
    private final String UPDATE_USER = "UPDATE User SET userName = ?, userPhone = ?, userEmail = ? WHERE userId = ? ";
    private final String DELETE_USER = "DELETE FROM User WHERE userId = ? ";

    /**
     * Get user by ID
     *
     * @param userId {@link Long}
     *
     * @return User {@link User}
     *
     * @throws AccountTransferException
     */
    public User getUserById(Long userId) throws AccountTransferException {
        Connection conn = null;
        PreparedStatement prepStatement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            conn = DbConfig.getConnection();
            prepStatement = conn.prepareStatement(GET_USER_BY_ID);
            prepStatement.setLong(1, userId);
            resultSet = prepStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setUserId(resultSet.getLong("userId"));
                user.setUserName(resultSet.getString("userName"));
                user.setUserPhone(resultSet.getString("userPhone"));
                user.setUserEmail(resultSet.getString("userEmail"));
            }
            return user;
        } catch (SQLException ex) {
            log.error("Can't find user with id: " + userId);
            throw new AccountTransferException("User with id: " + userId + " not found. " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, prepStatement, resultSet);
        }
    }

    /**
     * Get all users
     *
     * @return List of users
     *
     * @throws AccountTransferException
     */
    public List<User> getAllUsers() throws AccountTransferException {
        Connection conn = null;
        PreparedStatement prepStatement = null;
        ResultSet resultSet = null;
        User user = null;
        List<User> users = new ArrayList<>();

        try {
            conn = DbConfig.getConnection();
            prepStatement = conn.prepareStatement(GET_ALL_USERS);
            resultSet = prepStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setUserId(resultSet.getLong("userId"));
                user.setUserName(resultSet.getString("userName"));
                user.setUserPhone(resultSet.getString("userPhone"));
                user.setUserEmail(resultSet.getString("userEmail"));

                users.add(user);
            }
            return users;
        } catch (SQLException ex) {
            log.error("Can't get user data");
            throw new AccountTransferException("User data: " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, prepStatement, resultSet);
        }
    }

    /**
     * Get user by name
     *
     * @param userName {@link String}
     *
     * @return User {@link}
     *
     * @throws AccountTransferException
     */
    public User getUserByName(String userName) throws AccountTransferException {
        Connection conn = null;
        PreparedStatement prepStatement = null;
        ResultSet resultSet = null;
        User currentUser = null;

        try {
            conn = DbConfig.getConnection();
            prepStatement = conn.prepareStatement(GET_USER_BY_NAME);
            prepStatement.setString(1, userName);
            resultSet = prepStatement.executeQuery();
            if (resultSet.next()) {
                currentUser = new User();
                currentUser.setUserId(resultSet.getLong("userId"));
                currentUser.setUserName(resultSet.getString("userName"));
                currentUser.setUserPhone(resultSet.getString("userPhone"));
                currentUser.setUserEmail(resultSet.getString("userEmail"));
            }
            return currentUser;
        } catch (SQLException ex) {
            log.error("Can't find user with name: " + userName);
            throw new AccountTransferException("User with name: " + userName + " not found. " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, prepStatement, resultSet);
        }
    }

    /**
     * Add new user
     *
     * @param user {@link User}
     *
     * @throws AccountTransferException
     */
    public void addUser(User user) throws AccountTransferException {
        Connection conn = null;
        PreparedStatement prepStatement = null;

        try {
            conn = DbConfig.getConnection();
            prepStatement = conn.prepareStatement(ADD_NEW_USER);
            prepStatement.setString(1, user.getUserName());
            prepStatement.setString(2, user.getUserPhone());
            prepStatement.setString(3, user.getUserEmail());
            int result = prepStatement.executeUpdate();
        } catch (SQLException ex) {
            log.error("Can't add new user");
            throw new AccountTransferException("Add user: " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(prepStatement);
        }

    }

    /**
     * Update current user
     *
     * @param userId {@link Long}
     * @param user {@link User}
     *
     * @throws AccountTransferException
     */
    public void updateUser(Long userId, User user) throws AccountTransferException {
        Connection conn = null;
        PreparedStatement prepStatement = null;

        try {
            conn = DbConfig.getConnection();
            prepStatement = conn.prepareStatement(UPDATE_USER);
            prepStatement.setString(1, user.getUserName());
            prepStatement.setString(2, user.getUserPhone());
            prepStatement.setString(3, user.getUserEmail());
            prepStatement.setLong(4, userId);
            int result = prepStatement.executeUpdate();
        } catch (SQLException ex) {
            log.error("Can't update user");
            throw new AccountTransferException("Update user: " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(prepStatement);
        }
    }

    /**
     * Delete current user
     *
     * @param userId {@link Long}
     *
     * @throws AccountTransferException
     */
    public void deleteUser(Long userId) throws AccountTransferException {
        Connection conn = null;
        PreparedStatement prepStatement = null;

        try {
            conn = DbConfig.getConnection();
            prepStatement = conn.prepareStatement(DELETE_USER);
            prepStatement.setLong(1, userId);
            int result = prepStatement.executeUpdate();
        } catch (SQLException ex) {
            log.error("Can't delete user");
            throw new AccountTransferException("Delete user: " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(prepStatement);
        }
    }

}
