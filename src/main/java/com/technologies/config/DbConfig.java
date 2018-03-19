package com.technologies.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.DbUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database configuration.
 */
@Slf4j
public class DbConfig {

    private static final String dbDriver = ConfigLoader.getAppProperty("h2_driver");
    private static final String dbConnectionUrl = ConfigLoader.getAppProperty("h2_connection_url");
    private static final String dbUser = ConfigLoader.getAppProperty("h2_user");
    private static final String dbPassword = ConfigLoader.getAppProperty("h2_password");

    static {
        DbUtils.loadDriver(dbDriver);
    }

    /**
     * Get current connection
     *
     * @return Connection {@link Connection}
     *
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbConnectionUrl, dbUser, dbPassword);
    }

}
