package recommendation.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminHandler implements RoleHandler {
    private final Connection connection;
    private final InputReader inputReader;
    private final PrintWriter out;

    public AdminHandler(Connection connection, BufferedReader in, PrintWriter out) {
        this.connection = connection;
        this.inputReader = new InputReader(in);
        this.out = out;
    }

    @Override
    public void process() {
        System.out.println("Processing admin tasks...");
        try {
            handleAdminCommands();
        } catch (IOException e) {
            handleAdminError("Error handling admin commands: " + e.getMessage());
        }
    }

    private void handleAdminCommands() throws IOException {
        FoodMenu foodMenu = new FoodMenu(connection);
        String command;
        while ((command = inputReader.readString("Enter command (1-5, 6 to exit):")) != null && !"6".equals(command)) {
            System.out.println("Received command: " + command);
            switch (command) {
                case "1":
                    handleAddMenuItem(foodMenu);
                    break;
                case "2":
                    handleUpdateMenuItem(foodMenu);
                    break;
                case "3":
                    handleDeleteMenuItem(foodMenu);
                    break;
                case "4":
                    handleShowMenu(foodMenu);
                    break;
                case "5":
                    handleViewUserActivity();
                    break;
                default:
                    out.println("Invalid command");
                    out.flush();
            }
        }
        System.out.println("Log Out Successfully.");
    }

    private void handleAddMenuItem(FoodMenu foodMenu) throws IOException {
        String name = inputReader.readString("Enter name:");
        double price = inputReader.readDouble("Enter price:");
        double rating = inputReader.readDouble("Enter rating:");
        String category = inputReader.readString("Enter category:");

        boolean result = foodMenu.addMenuItem(name, price, rating, category);
        printOperationResult(result, "Menu item added successfully", "Failed to add menu item");
    }

    private void handleUpdateMenuItem(FoodMenu foodMenu) throws IOException {
        int id = inputReader.readInt("Enter ID:");
        String name = inputReader.readString("Enter name:");
        double price = inputReader.readDouble("Enter price:");
        double rating = inputReader.readDouble("Enter rating:");
        String category = inputReader.readString("Enter category:");

        boolean result = foodMenu.updateMenuItem(id, name, price, rating, category);
        printOperationResult(result, "Menu item updated successfully", "Failed to update menu item");
    }

    private void handleDeleteMenuItem(FoodMenu foodMenu) throws IOException {
        int id = inputReader.readInt("Enter ID:");

        boolean result = foodMenu.deleteMenuItem(id);
        printOperationResult(result, "Menu item deleted successfully", "Failed to delete menu item");
    }

    private void handleShowMenu(FoodMenu foodMenu) {
        out.println("Current Menu:");
        out.flush();
        printMenuHeader();
        foodMenu.showMenu(out);
    }

    private void handleViewUserActivity() {
        String query = "SELECT email, logInTime, logOutTime FROM UserActivity";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            printUserActivityTable(rs);
        } catch (SQLException e) {
            handleAdminError("Failed to retrieve user activity: " + e.getMessage());
        }
    }
    
    private void printUserActivityTable(ResultSet rs) throws SQLException {
        out.println("+--------------------------------+---------------------+---------------------+");
        out.println("| Email                          | Log In Time         | Log Out Time        |");
        out.println("+--------------------------------+---------------------+---------------------+");
    
        while (rs.next()) {
            String email = rs.getString("email");
            String logInTime = rs.getString("logInTime");
            String logOutTime = rs.getString("logOutTime");
            out.printf("| %-30s | %-19s | %-19s |%n", email, logInTime, logOutTime);
        }
    
        out.println("+--------------------------------+---------------------+---------------------+");
        out.println("End of User Activity");
        out.flush();
    }

    private void printOperationResult(boolean result, String successMessage, String failureMessage) {
        if (result) {
            out.println(successMessage);
            System.out.println(successMessage);
        } else {
            out.println(failureMessage);
            System.out.println(failureMessage);
        }
        out.flush();
    }

    private void printMenuHeader() {
        out.println("-------------------------------------------");
        out.printf("%-5s | %-20s | %-10s | %-6s | %-20s |%n", "ID", "Name", "Price", "Rating", "Category");
        out.println("-------------------------------------------");
        out.flush();
    }

    private void handleAdminError(String errorMessage) {
        System.err.println(errorMessage);
        out.println(errorMessage);
        out.flush();
    }
}
