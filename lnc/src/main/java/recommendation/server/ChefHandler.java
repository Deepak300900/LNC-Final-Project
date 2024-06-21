package recommendation.server;

import java.io.*;
import java.sql.*;

public class ChefHandler implements RoleHandler {
    private final Connection connection;
    private final BufferedReader in;
    private final PrintWriter out;

    public ChefHandler(Connection connection, BufferedReader in, PrintWriter out) {
        this.connection = connection;
        this.in = in;
        this.out = out;
    }

    @Override
    public void process() {
        try {
            new RecommendationEngine().calculateSentiment(connection);
            out.println("Calculated sentiment");
            handleCommands();
        } catch (IOException | SQLException e) {
            handleException(e);
        }
    }

    private void handleCommands() throws IOException, SQLException {
        while (true) {
            String command = in.readLine();
            if (command == null || "exit".equalsIgnoreCase(command)) {
                break;
            }

            switch (command) {
                case "1":
                    showFoodMenuItems();
                    break;
                case "2":
                    showRecommendedFood();
                    break;
                case "3":
                    addFoodItemToWillPrepare();
                    break;
                case "4":
                    showAllWillPrepareFoodItems();
                    break;
                case "5":
                    addFoodItemToPrepared();
                    break;
                case "6":
                    showDiscardableFood();
                    handleDiscardableFoodCommands();
                    break;
                case "7": // New case for showing destroyed food feedback
                    showDestroyedFoodFeedback();
                    break;
                default:
                    out.println("Invalid command");
                    out.println("End of Response");
                    out.flush();
            }
        }
    }

    private void showFoodMenuItems() {
        String query = "SELECT id, name, price FROM foodmenu";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            printFoodMenuTable(rs);
        } catch (SQLException e) {
            handleException(e);
        }
    }

    private void printFoodMenuTable(ResultSet rs) throws SQLException {
        out.println("+----+--------------+-------+");
        out.println("| ID |     Name     | Price |");
        out.println("+----+--------------+-------+");
        
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            float price = rs.getFloat("price");
            out.printf("| %2d | %-12s | %.2f |%n", id, name, price);
        }
        
        out.println("+----+--------------+-------+");
        out.println("End of Response");
        out.flush();
    }

    private void showRecommendedFood() {
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

    private void addFoodItemToWillPrepare() throws IOException, SQLException {
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

    private void showAllWillPrepareFoodItems() {
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

    private void addFoodItemToPrepared() throws IOException, SQLException {
        try {
            int preparedFoodItemId = Integer.parseInt(in.readLine());
            String query = "INSERT INTO preparedFoodItem (foodItemId, CreatedDate) VALUES (?, CURDATE())";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, preparedFoodItemId);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    out.println("Food item added to preparedFoodItem successfully.");
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

    private void showDiscardableFood() {
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

    private void handleDiscardableFoodCommands() throws IOException, SQLException {
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

    private void showDestroyedFoodFeedback() {
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

    private void handleException(Exception e) {
        e.printStackTrace();
        out.println("An error occurred. Please try again later.");
        out.println("End of Response");
        out.flush();
    }
}
