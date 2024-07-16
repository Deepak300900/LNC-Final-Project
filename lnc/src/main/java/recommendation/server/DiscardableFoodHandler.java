package recommendation.server;

import java.io.*;
import java.sql.*;

public class DiscardableFoodHandler {
    private final Connection connection;
    private final BufferedReader in;
    private final PrintWriter out;

    public DiscardableFoodHandler(Connection connection, BufferedReader in, PrintWriter out) {
        this.connection = connection;
        this.in = in;
        this.out = out;
    }

    public void showDiscardableFood() {
        String query = "SELECT fm.id, fm.name, fm.price, rf.sentimentscore " +
                       "FROM foodmenu fm " +
                       "JOIN recommendedfood rf ON fm.id = rf.foodId " +
                       "WHERE rf.sentimentscore < 55 " +
                       "AND DATE(rf.date) = CURDATE()";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            printDiscardableFoodTable(rs);
        } catch (SQLException e) {
            handleException(e);
        }
    }

    private void printDiscardableFoodTable(ResultSet rs) throws SQLException {
        out.println("+----+--------------+-------+----------------+");
        out.println("| ID |     Name     | Price | Sentiment Score |");
        out.println("+----+--------------+-------+----------------+");

        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            double price = rs.getDouble("price");
            double sentimentScore = rs.getDouble("sentimentscore");
            out.printf("| %2d | %-12s | %5.2f | %14.2f |%n", id, name, price, sentimentScore);
        }

        out.println("+----+--------------+-------+----------------+");
        out.println("End of Response");
        out.flush();
    }

    public void handleDiscardableFoodCommands() throws IOException, SQLException {
        String command = in.readLine();
        switch (command) {
            case "1":
                deleteDiscardableFood();
                break;
            case "2":
                storeDiscardableFood();
                break;
            default:
                out.println("Invalid option.");
                out.println("End of Response");
                out.flush();
        }
    }

    private void deleteDiscardableFood() throws IOException, SQLException {
        try {
            int foodItemId = Integer.parseInt(in.readLine());
            String query = "DELETE FROM foodmenu WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, foodItemId);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    out.println("Discardable food item deleted successfully.");
                } else {
                    out.println("Failed to delete discardable food item.");
                }
                out.println("End of Response");
                out.flush();
            }
        } catch (NumberFormatException e) {
            out.println("Invalid input format.");
            out.println("End of Response");
            out.flush();
        }
    }

    private void storeDiscardableFood() throws IOException, SQLException {
        try {
            int foodItemId = Integer.parseInt(in.readLine());
            String query = "INSERT INTO discardablefood (foodItemId, date) VALUES (?, CURDATE())";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, foodItemId);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    out.println("Discardable food item stored successfully.");
                } else {
                    out.println("Failed to store discardable food item.");
                }
                out.println("End of Response");
                out.flush();
            }
        } catch (NumberFormatException e) {
            out.println("Invalid input format.");
            out.println("End of Response");
            out.flush();
        }
    }

    private void handleException(SQLException e) {
        e.printStackTrace();
        out.println("An error occurred. Please try again later.");
        out.println("End of Response");
        out.flush();
    }
}
