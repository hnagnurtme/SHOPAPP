package com.example.shopapp.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;
import java.util.Properties;


public class DatabaseConnection {
    private static final String CONFIG_FILE = "config.properties";
    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;
    private static String dbDriver;
    
    static {
        try {
            // Load properties from configuration file
            Properties props = new Properties();
            props.load(DatabaseConnection.class.getClassLoader().getResourceAsStream(CONFIG_FILE));
            
            dbUrl = props.getProperty("db.url");
            dbUser = props.getProperty("db.user");
            dbPassword = props.getProperty("db.password");
            dbDriver = props.getProperty("db.driver");
            
            // Load database driver
            Class.forName(dbDriver);
        } catch (ClassNotFoundException | IOException e) {
            System.err.println("Error initializing database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
   
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }
    
    
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
