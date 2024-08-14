package recommendation.server.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import recommendation.server.handlers.NotificationHandler;

public class SaveSelectedFoodHelper {
    private final Connection connection;
    private final BufferedReader in;
    private final PrintWriter out;

    public SaveSelectedFoodHelper(Connection connection, BufferedReader in, PrintWriter out) {
        this.connection = connection;
        this.in = in;
        this.out = out;
    }

    public void saveSelectedFood() throws IOException, SQLException {
        try {
            int preparedFoodItemId = Integer.parseInt(in.readLine());
            
            String query = "INSERT INTO preparedFoodItem (foodItemId, CreatedDate) VALUES (?, CURDATE())";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                if (isFoodIdCorrect(preparedFoodItemId)) {
                pstmt.setInt(1, preparedFoodItemId);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                        out.println("Food item selected successfully for tomorrow.");
                        
                        String foodDetails = new FoodMenuHelper(this.connection).getFoodItemDetails(preparedFoodItemId);
                        
                        sendNotification("Food Item Selected for Tomorrow", foodDetails);
                } else {
                    out.println("Failed to add food item.");
                }
                out.println("End of Response");
                out.flush();
                } else {
                    out.println("Invalid Id. Please enter a valid Id.");
                out.println("End of Response");
                out.flush();
               }
            }
        } catch (NumberFormatException e) {
            out.println("Invalid input format.");
            out.println("End of Response");
            out.flush();
        } catch (SQLException e) {
            out.println("Already added this item.");
                out.println("End of Response");
                out.flush();
        }
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
}
