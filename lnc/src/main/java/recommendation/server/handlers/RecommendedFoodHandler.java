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
        String query = "SELECT fm.id, fm.name, fm.price, uf.sentimentscore " +
                       "FROM foodmenu fm " +
                       "JOIN (" +
                       "    SELECT FoodId, sentimentscore " +
                       "    FROM recommendedfood " +
                       "    WHERE DATE(date) = CURDATE() " +
                       "    ORDER BY sentimentscore DESC " +
                       "    LIMIT 5" +
                       ") uf ON fm.id = uf.foodId";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            printRecommendedFoodTable(rs);
        } catch (SQLException e) {
            handleException(e);
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
            double sentimentScore = rs.getDouble("sentimentscore");
            out.printf("| %2d | %-12s | %5.2f | %14.2f |%n", id, name, price, sentimentScore);
        }

        out.println("+----+--------------+-------+----------------+");
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
