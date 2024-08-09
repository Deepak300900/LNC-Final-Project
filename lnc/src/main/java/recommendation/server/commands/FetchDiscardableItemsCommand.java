package recommendation.server.commands;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import recommendation.server.interfaces.EmployeeCommand;

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
            ResultSet rs = stmt.executeQuery("SELECT id, name, price FROM Foodmenu WHERE id IN (SELECT FoodItemId FROM preparedFoodItem WHERE CreatedDate < DATE_SUB(CURDATE(), INTERVAL 30 DAY)) LIMIT 1");
            if (rs.next()) {
                sendItem(rs.getInt("id"), rs.getString("name"), rs.getFloat("price"));
            } else {
                sendEndOfItems();
            }
        } catch (SQLException e) {
            handleEmployeeError("Error fetching discardable items: " + e.getMessage());
        }
    }

    private void sendItem(int id, String name, float price) {
        out.println(id + "," + name + "," + price);
        out.flush();
    }

    private void sendEndOfItems() {
        out.println("End of Response");
        out.flush();
    }

    private void handleEmployeeError(String message) {
        System.err.println(message);
        out.println("ERROR");
        out.flush();
    }
}
