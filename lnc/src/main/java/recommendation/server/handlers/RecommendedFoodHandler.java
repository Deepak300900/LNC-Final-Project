package recommendation.server.handlers;

import java.io.*;
import java.sql.*;

public class RecommendedFoodHandler {
    private final Connection connection;
    private final PrintWriter out;

    public RecommendedFoodHandler(Connection connection, PrintWriter out) {
        this.connection = connection;
        this.out = out;
    }

    public void showRecommendedFood() {
        String breakfastQuery = "SELECT id, name, price, SentimentScores FROM foodmenu WHERE type = 'Breakfast' ORDER BY SentimentScores DESC LIMIT 4";
        String lunchQuery = "SELECT id, name, price, SentimentScores FROM foodmenu WHERE type = 'Lunch' ORDER BY SentimentScores DESC LIMIT 4";
        String dinnerQuery = "SELECT id, name, price, SentimentScores FROM foodmenu WHERE type = 'Dinner' ORDER BY SentimentScores DESC LIMIT 4";

        try {
            showFoodByType(breakfastQuery, "Recommended Food For Breakfast: ");
            showFoodByType(lunchQuery, "Recommended Food For Lunch:");
            showFoodByType(dinnerQuery, "Recommended Food For Dinner:");
            out.println("End Of Menu");
            out.flush();
        } catch (SQLException e) {
            handleException(e);
        }
    }

    private void showFoodByType(String query, String menuType) throws SQLException {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            out.println("\n--------------------------------------------------------------\n");
            out.println(menuType + ":");
            printRecommendedFoodTable(rs);
        }
    }

    private void printRecommendedFoodTable(ResultSet rs) throws SQLException {        
        out.println("+----+--------------+-------+----------------+");
        out.println("| ID |     Name     | Price | Sentiment Score |");
        out.println("+----+--------------+-------+----------------+");

        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            double price = rs.getDouble("price");
            double sentimentScore = rs.getDouble("SentimentScores");
            out.printf("| %2d | %-12s | %5.2f | %14.2f |%n", id, name, price, sentimentScore);
        }

        out.println("+----+--------------+-------+----------------+");
    }

    private void handleException(SQLException e) {
        e.printStackTrace();
        out.println("An error occurred. Please try again later.");
        out.println("End Of Menu");
        out.flush();
    }
}
