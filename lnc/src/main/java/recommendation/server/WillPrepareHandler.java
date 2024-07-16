package recommendation.server;

import java.io.*;
import java.sql.*;

public class WillPrepareHandler {
    private final Connection connection;
    private final BufferedReader in;
    private final PrintWriter out;

    public WillPrepareHandler(Connection connection, BufferedReader in, PrintWriter out) {
        this.connection = connection;
        this.in = in;
        this.out = out;
    }

    public void addFoodItemToWillPrepare() throws IOException, SQLException {
        try {
            int foodItemId = Integer.parseInt(in.readLine());
            String query = "INSERT INTO RolloutFoodItems (foodItemId, votingCount, createdDate) VALUES (?, 0, CURRENT_DATE)";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, foodItemId);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    out.println("Food item added successfully.");
                } else {
                    out.println("Failed to add food item.");
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

    public void showAllWillPrepareFoodItems() {
        String query = "SELECT fm.id, fm.name, wpf.votingCount " +
                       "FROM foodMenu fm " +
                       "JOIN RolloutFoodItems wpf ON fm.id = wpf.foodItemId " +
                       "WHERE wpf.createdDate = CURRENT_DATE";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            printWillPrepareFoodItemsTable(rs);
        } catch (SQLException e) {
            handleException(e);
        }
    }

    private void printWillPrepareFoodItemsTable(ResultSet rs) throws SQLException {
        out.println("+-------------+----------------------+-------------+");
        out.println("| Food ItemID | Name                 | Voting Count|");
        out.println("+-------------+----------------------+-------------+");

        while (rs.next()) {
            int foodItemId = rs.getInt("id");
            String name = rs.getString("name");
            int votingCount = rs.getInt("votingCount");
            out.printf("| %11d | %-20s | %11d |%n", foodItemId, name, votingCount);
        }

        out.println("+-------------+----------------------+-------------+");
        out.println("End of Response");
        out.flush();
    }

    private void handleException(SQLException e) {
        e.printStackTrace();
        out.println("An error occurred. Please try again later.");
        out.println("End of Response");
        out.flush();
    }
}
