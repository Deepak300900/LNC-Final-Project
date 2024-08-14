package recommendation.server.helpers;

import java.io.PrintWriter;
import java.sql.*;

public class FoodMenuHelper {
    private static final String ADD_MENU_ITEM_SQL = "INSERT INTO foodMenu (name, price, category, Type, test ) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_MENU_ITEM_SQL = "UPDATE foodMenu SET name = ?, price = ?, category = ? WHERE id = ?";
    private static final String DELETE_MENU_ITEM_SQL = "DELETE FROM foodMenu WHERE id = ?";
    private static final String SHOW_MENU_SQL = "SELECT id, name, price, rating, category,Type , test FROM foodMenu";
    private static final String GET_FEEDBACK_RATINGS_SQL = "SELECT rating FROM UserfeedBack WHERE foodItemId = ?";
    private static final String UPDATE_FOOD_MENU_RATING_SQL = "UPDATE foodMenu SET rating = ? WHERE id = ?";

    private final Connection connection;

    public FoodMenuHelper(Connection connection) {
        this.connection = connection;
    }

    public boolean addMenuItem(String name, double price, String category, String type, String test) {
        try (PreparedStatement pstmt = connection.prepareStatement(ADD_MENU_ITEM_SQL)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setString(3, category);
            pstmt.setString(4, type);
            pstmt.setString(5, test);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding menu item: " + e.getMessage());
            return false;
        }
    }

    public boolean updateMenuItem(int id, String name, double price, String category) {
        try (PreparedStatement pstmt = connection.prepareStatement(UPDATE_MENU_ITEM_SQL)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setString(3, category);
            pstmt.setInt(4, id);
            if(price > 0){
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error updating menu item: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteMenuItem(int id) {
        try (PreparedStatement pstmt = connection.prepareStatement(DELETE_MENU_ITEM_SQL)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting menu item: " + e.getMessage());
            return false;
        }
    }

    public void showMenu(PrintWriter out) {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(SHOW_MENU_SQL)) {
    
            out.println("--------------------------------------------------------------------------------------");
            out.printf("| %-5s | %-15s | %-8s | %-6s | %-10s | %-10s | %-10s |%n", "ID", "Name", "Price", "Rating", "Category", "Type", "Test");
            out.println("--------------------------------------------------------------------------------------");
    
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                double rating = rs.getDouble("rating");
                String category = rs.getString("category");
                String type = rs.getString("type");
                String test = rs.getString("test");
    
                out.printf("| %-5d | %-15s | %-8.2f | %-6.2f | %-10s | %-10s | %-10s |%n", id, name, price, rating, category, type, test);
            }
    
            out.println("--------------------------------------------------------------------------------------");
            out.println("End of Menu");
            out.flush();
        } catch (SQLException e) {
            System.err.println("Error retrieving menu items: " + e.getMessage());
            out.println("Error retrieving menu items: " + e.getMessage());
            out.flush();
        }
    }
    

    public boolean updateAverageRating(int foodItemId) {
        try (PreparedStatement getRatingsStmt = connection.prepareStatement(GET_FEEDBACK_RATINGS_SQL)) {
            getRatingsStmt.setInt(1, foodItemId);
            ResultSet rs = getRatingsStmt.executeQuery();

            double totalRating = 0.0;
            int count = 0;

            while (rs.next()) {
                totalRating += rs.getDouble("rating");
                count++;
            }

            if (count == 0) {
                System.err.println("No ratings found for food item ID: " + foodItemId);
                return false;
            }

            double averageRating = totalRating / count;

            try (PreparedStatement updateRatingStmt = connection.prepareStatement(UPDATE_FOOD_MENU_RATING_SQL)) {
                updateRatingStmt.setDouble(1, averageRating);
                updateRatingStmt.setInt(2, foodItemId);
                int rowsAffected = updateRatingStmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error updating average rating: " + e.getMessage());
            return false;
        }
    }

    public String getFoodItemDetails(int foodItemId) throws SQLException {
        String query = "SELECT name, price, type, category, test FROM FoodMenu WHERE Id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, foodItemId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    double price = resultSet.getDouble("price");
                    String type = resultSet.getString("type");
                    String category = resultSet.getString("category");
                    String test = resultSet.getString("test");
                    return String.format("Item: %s, Price: %.2f, Type: %s, Category: %s, Test: %s",
                            name, price, type, category, test);
                } else {
                    return "Food item details not found.";
                }
            }
        }
    }
}
