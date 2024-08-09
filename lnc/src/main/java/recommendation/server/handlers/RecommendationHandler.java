package recommendation.server.handlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class RecommendationHandler {
    private FeedbackHandler feedbackAnalyzer;
    private Map<String, Double> recommendedFood;

    public RecommendationHandler() {
        this.feedbackAnalyzer = new FeedbackHandler();
        this.recommendedFood = new HashMap<>();
    }

    public void calculateSentiment(Connection connection) throws SQLException {
        try {
                Map<String, String> foodItemsWithFeedbacks = fetchFeedbacksFromDatabase(connection);
    
                for (Map.Entry<String, String> entry : foodItemsWithFeedbacks.entrySet()) {
                    String foodItemId = entry.getKey();
                    String feedbacks = entry.getValue();
                    double sentimentScore = feedbackAnalyzer.processFeedbacks(feedbacks);
                    recommendedFood.put(foodItemId, sentimentScore);
                }
    
                storeRecommendations(connection);
            } catch (SQLException e) {
                System.err.println("Error fetching feedbacks from database: " + e.getMessage());
            }
    }

    private Map<String, String> fetchFeedbacksFromDatabase(Connection connection) throws SQLException {
        Map<String, String> foodItemsWithFeedbacks = new HashMap<>();
        String sql = "SELECT fooditemid, feedback FROM userfeedback";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String foodItemId = rs.getString("fooditemid");
                String feedback = rs.getString("feedback");

                if (foodItemsWithFeedbacks.containsKey(foodItemId)) {
                    String existingFeedback = foodItemsWithFeedbacks.get(foodItemId);
                    existingFeedback += " " + feedback; // Append with a space separator
                    foodItemsWithFeedbacks.put(foodItemId, existingFeedback);
                } else {
                    foodItemsWithFeedbacks.put(foodItemId, feedback);
                }
            }
        }
        return foodItemsWithFeedbacks;
    }

    private void storeRecommendations(Connection connection) {
        String sql = "INSERT INTO recommendedfood (foodId, sentimentScore, date) VALUES (?, ?, CURDATE()) " +
        "ON DUPLICATE KEY UPDATE sentimentScore = VALUES(sentimentScore)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (Map.Entry<String, Double> entry : recommendedFood.entrySet()) {
                String foodItemId = entry.getKey();
                double sentimentScore = entry.getValue();

                pstmt.setString(1, foodItemId);
                pstmt.setDouble(2, sentimentScore);

                pstmt.executeUpdate();
                
                System.out.println("Inserted into recommended_food table: Food Item ID: " + foodItemId + ", Sentiment Score: " + sentimentScore);
            }
        } catch (SQLException e) {
            System.err.println("Error inserting recommendations into database: " + e.getMessage());
        }
    }

    
}
