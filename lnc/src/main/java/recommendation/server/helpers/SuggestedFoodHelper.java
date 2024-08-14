package recommendation.server.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
//import java.time.LocalDate;

import recommendation.server.handlers.NotificationHandler;


public class SuggestedFoodHelper {
    private final Connection connection;
    private final BufferedReader in;
    private final PrintWriter out;

    public SuggestedFoodHelper(Connection connection, BufferedReader in, PrintWriter out) {
        this.connection = connection;
        this.in = in;
        this.out = out;
    }

    public void saveSuggestedFood() throws IOException, SQLException {
        try {
            int foodItemId = Integer.parseInt(in.readLine());
            boolean isFoodIdAdded = isFoodIdAdded(foodItemId);
            if (!isFoodIdAdded && isFoodIdCorrect(foodItemId)) {
            String query = "INSERT INTO RolloutFoodItems (foodItemId, votingCount, createdDate) VALUES (?, 0, CURRENT_DATE)";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, foodItemId);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    out.println("Food item added successfully.");

                        String foodDetails = new FoodMenuHelper(this.connection).getFoodItemDetails(foodItemId);

                        sendNotification("Food Item Added to Rollout", foodDetails);
                } else {
                    out.println("Failed to add food item.");
                }
                out.println("End of Response");
                out.flush();
            }
            } else {
                if(isFoodIdAdded){
                out.println("This food Rollout today already");
                }else{
                out.println("Invalid food id. Please enter valid food Id");
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

    private boolean isFoodIdAdded(int foodId) {
        String query = "SELECT COUNT(*) FROM rolloutfooditems WHERE foodItemId = ? AND createdDate = CURRENT_DATE";
        //LocalDate today = LocalDate.now();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, foodId);
           // stmt.setDate(2, java.sql.Date.valueOf(today));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);

                    return count != 0; 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }

        return true; 
    }

    private boolean isFoodIdCorrect(int foodId) throws SQLException {
        String query = "SELECT COUNT(*) FROM FoodMenu WHERE Id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, foodId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() && resultSet.getInt(1) > 0;
            }
        }
    }


    private void sendNotification(String title, String message) throws SQLException {
        NotificationHandler notificationHandler = new NotificationHandler(connection);
        notificationHandler.addNotification(title, message);
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
