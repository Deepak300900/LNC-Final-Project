package recommendation.server;

import java.io.*;
import java.sql.*;

public class MenuItemHandler {
    private final Connection connection;
    private final PrintWriter out;

    public MenuItemHandler(Connection connection, PrintWriter out) {
        this.connection = connection;
        this.out = out;
    }

    public void showFoodMenuItems() {
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

    private void handleException(SQLException e) {
        e.printStackTrace();
        out.println("An error occurred. Please try again later.");
        out.println("End of Response");
        out.flush();
    }
}
