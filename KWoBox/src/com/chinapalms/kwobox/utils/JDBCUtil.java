package com.chinapalms.kwobox.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.service.TokenService;

public class JDBCUtil {

    static Log log = LogFactory.getLog(JDBCUtil.class);

    private static String url = null;
    private static String user = null;
    private static String password = null;

    static {
        try {
            InputStream is = JDBCUtil.class
                    .getResourceAsStream("/db.properties");
            Properties properties = new Properties();
            properties.load(is);
            url = properties.getProperty("url");
            user = properties.getProperty("user");
            password = properties.getProperty("password");
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    public static Connection getDBConnection() {
        try {
            Connection connection = DriverManager.getConnection(url, user,
                    password);
            return connection;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static void closeDBConnection(Connection connection,
            PreparedStatement statement) {
        try {
            if (statement != null) {
                if (!statement.isClosed()) {
                    statement.close();
                }
            }
            if (connection != null) {
                if (!connection.isClosed()) {
                    connection.close();
                }
            }
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void closeDBConnection(Connection connection,
            PreparedStatement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                if (!resultSet.isClosed()) {
                    resultSet.close();
                }
            }
            if (statement != null) {
                if (!statement.isClosed()) {
                    statement.close();
                }
            }
            if (connection != null) {
                if (!connection.isClosed()) {
                    connection.close();
                }
            }
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
