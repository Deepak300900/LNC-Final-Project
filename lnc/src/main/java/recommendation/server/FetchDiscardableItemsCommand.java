package recommendation.server;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FetchDiscardableItemsCommand implements EmployeeCommand {
    private final Connection connection;
    private final PrintWriter out;

    public FetchDiscardableItemsCommand(Connection connection, PrintWriter out) {
        this.connection = connection;
        this.out = out;
    }

    @Override
    public void execute() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT id, name, price FROM Foodmenu WHERE id IN (SELECT FoodItemId FROM preparedFoodItem WHERE CreatedDate = CURDATE())");
            while (rs.next()) {
                sendItem(rs.getInt("id"), rs.getString("name"), rs.getFloat("price"));
            }
            sendEndOfItems();
        } catch (SQLException e) {
            handleEmployeeError("Error fetching discardable items: " + e.getMessage());
        }
    }

    private void sendItem(int id, String name, float price) {
        out.println(id + "," + name + "," + price);
        out.flush();
    }

    private void sendEndOfItems() {
        out.println("END_OF_ITEMS");
        out.flush();
    }

    private void handleEmployeeError(String message) {
        System.err.println(message);
        out.println("ERROR");
        out.flush();
    }
}
