package recommendation.server;

import java.io.PrintWriter;
import java.sql.*;

public class FoodMenu {
    private static final String ADD_MENU_ITEM_SQL = "INSERT INTO foodMenu (name, price, rating, category) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_MENU_ITEM_SQL = "UPDATE foodMenu SET name = ?, price = ?, rating = ?, category = ? WHERE id = ?";
    private static final String DELETE_MENU_ITEM_SQL = "DELETE FROM foodMenu WHERE id = ?";
    private static final String SHOW_MENU_SQL = "SELECT id, name, price, rating, category FROM foodMenu";

    private final Connection connection;

    public FoodMenu(Connection connection) {
        this.connection = connection;
    }

    public boolean addMenuItem(String name, double price, double rating, String category) {
        try (PreparedStatement pstmt = connection.prepareStatement(ADD_MENU_ITEM_SQL)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setDouble(3, rating);
            pstmt.setString(4, category);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding menu item: " + e.getMessage());
            return false;
        }
    }

    public boolean updateMenuItem(int id, String name, double price, double rating, String category) {
        try (PreparedStatement pstmt = connection.prepareStatement(UPDATE_MENU_ITEM_SQL)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setDouble(3, rating);
            pstmt.setString(4, category);
            pstmt.setInt(5, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
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

            out.println("------------------------------------------------------------------------------------");
            out.printf("| %-5s | %-15s | %-8s | %-6s | %-20s |%n", "ID", "Name", "Price", "Rating", "Category");
            out.println("------------------------------------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                double rating = rs.getDouble("rating");
                String category = rs.getString("category");

                out.printf("| %-5d | %-15s | %-8.2f | %-6.2f | %-20s |%n", id, name, price, rating, category);
            }

            out.println("------------------------------------------------------------------------------------");
            out.println("End of Menu");
            out.flush();
        } catch (SQLException e) {
            System.err.println("Error retrieving menu items: " + e.getMessage());
            out.println("Error retrieving menu items: " + e.getMessage());
            out.flush();
        }
    }
}
