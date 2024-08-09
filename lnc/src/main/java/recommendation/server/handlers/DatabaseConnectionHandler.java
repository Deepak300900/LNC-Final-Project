package recommendation.server.handlers;

import java.sql.*;

import recommendation.server.drivers.Server;

public class DatabaseConnectionHandler {
    private static DatabaseConnectionHandler instance;
    private Connection connection;

    private DatabaseConnectionHandler() {
        try {
            this.connection = DriverManager.getConnection(Server.DB_URL, Server.DB_USER, Server.DB_PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }
    }

    public static synchronized DatabaseConnectionHandler getInstance() {
        if (instance == null) {
            instance = new DatabaseConnectionHandler();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}
