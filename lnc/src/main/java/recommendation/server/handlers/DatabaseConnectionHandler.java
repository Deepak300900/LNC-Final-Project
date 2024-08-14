package recommendation.server.handlers;

import java.sql.*;

public class DatabaseConnectionHandler {
    private static DatabaseConnectionHandler instance;
    private Connection connection;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/deepakdatabase";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Deepak@300900";

    private DatabaseConnectionHandler() {
        try {
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
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
