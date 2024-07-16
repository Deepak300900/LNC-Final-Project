package recommendation.server;

import java.sql.*;

public class DatabaseConnectionManager {
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(Server.DB_URL, Server.DB_USER, Server.DB_PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            return null;
        }
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}
