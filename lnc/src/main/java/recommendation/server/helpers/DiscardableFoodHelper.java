package recommendation.server.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDate;

import recommendation.server.handlers.NotificationHandler;

public class DiscardableFoodHelper {
    private final Connection connection;
    private final BufferedReader in;
    private final PrintWriter out;

    public DiscardableFoodHelper(Connection connection, BufferedReader in, PrintWriter out) {
        this.connection = connection;
        this.in = in;
        this.out = out;
    }

    public boolean showDiscardableFood() {
        String query = "SELECT id, name, price, SentimentScores FROM foodmenu WHERE SentimentScores < 55 AND Rating <= 2";

        boolean isDiscardableFoodAvailable = false;
    
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (rs.isBeforeFirst()) {
                isDiscardableFoodAvailable = true;
                printDiscardableFoodTable(rs);
            } else {
                out.println("No discardable food items found.");
                out.println("End of Response");
                out.flush();
            }
        } catch (SQLException e) {
            handleException(e);
        }
        return isDiscardableFoodAvailable;
    }
    

    private void printDiscardableFoodTable(ResultSet rs) throws SQLException {
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
            String foodDetails = new FoodMenuHelper(connection).getFoodItemDetails(foodItemId);
            String query = "DELETE FROM foodmenu WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, foodItemId);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    out.println("Discardable food item deleted successfully.");
                    sendNotification("A Food Item is no more. RIP. ", foodDetails);
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
             if(!isOperationAvailable()){
                out.println("NOT_AVAILABLE");
                out.println("This Option has been used under 30 days");
                out.println("End of Response");
                out.flush();
                return;
             }  

            out.println("AVAILABLE");
            int foodItemId = Integer.parseInt(in.readLine());
            
            String query = "INSERT INTO discardablefood (foodItemId, date) VALUES (?, CURDATE())";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, foodItemId);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    out.println("Discardable food item stored successfully.");
                    String foodDetails = new FoodMenuHelper(connection).getFoodItemDetails(foodItemId);
                    sendNotification("Discardable Food Item Added", foodDetails);
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
        } catch (IOException e) {
            out.println("Invalid input.");
                out.println("End of Response");
                out.flush();
        } catch (SQLException e) {
            out.println("The item is already added.");
                out.println("End of Response");
                out.flush();
        }
    }

    private boolean isOperationAvailable() {

        LocalDate dateThreshold = LocalDate.now().minusDays(30);
        String query = "SELECT COUNT(*) FROM discardablefood WHERE date > ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, java.sql.Date.valueOf(dateThreshold));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    public void showDiscardableFoodFeedback() {
        String query = "SELECT dff.foodItemId, fm.name, dff.userEmail, dff.foodConcern, dff.improvement, dff.momRecipe " +
                       "FROM discardablefoodfeedback dff " +
                       "JOIN foodmenu fm ON dff.foodItemId = fm.id";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
             printDiscardableFoodFeedbackTable(rs);
        } catch (SQLException e) {
            handleException(e);
        }
    }

    private void printDiscardableFoodFeedbackTable(ResultSet rs) throws SQLException {
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

    private void sendNotification(String title, String message) throws SQLException {
        NotificationHandler notificationHandler = new NotificationHandler(connection);
        notificationHandler.addNotification(title, message);
    }

    private void handleException(SQLException e) {
        e.printStackTrace();
        out.println("An error occurred. Please try again later.");
        out.println("End of Response");
        out.flush();
    }
}
