package recommendation.server;

import java.io.*;
import java.sql.*;

public class DestroyedFoodFeedbackHandler {
    private final Connection connection;
    private final PrintWriter out;

    public DestroyedFoodFeedbackHandler(Connection connection, PrintWriter out) {
        this.connection = connection;
        this.out = out;
    }

    public void showDestroyedFoodFeedback() {
        String query = "SELECT dff.foodItemId, fm.name, dff.userEmail, dff.foodConcern, dff.improvement, dff.momRecipe " +
                       "FROM discardablefoodfeedback dff " +
                       "JOIN foodmenu fm ON dff.foodItemId = fm.id";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            printDestroyedFoodFeedbackTable(rs);
        } catch (SQLException e) {
            handleException(e);
        }
    }

    private void printDestroyedFoodFeedbackTable(ResultSet rs) throws SQLException {
        out.println("+-------------+--------------+------------------+-----------------+----------------------+----------------+");
        out.println("| Food ItemID | Name         | User Email       | Food Concern    | Improvement          | Mom Recipe     |");
        out.println("+-------------+--------------+------------------+-----------------+----------------------+----------------+");

        while (rs.next()) {
            int foodItemId = rs.getInt("foodItemId");
            String name = rs.getString("name");
            String userEmail = rs.getString("userEmail");
            String foodConcern = rs.getString("foodConcern");
            String improvement = rs.getString("improvement");
            String momRecipe = rs.getString("momRecipe");
            out.printf("| %11d | %-12s | %-16s | %-15s | %-20s | %-14s |%n", foodItemId, name, userEmail, foodConcern, improvement, momRecipe);
        }

        out.println("+-------------+--------------+------------------+-----------------+----------------------+----------------+");
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
