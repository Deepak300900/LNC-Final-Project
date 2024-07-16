package recommendation.server;

import java.io.PrintWriter;
import java.sql.Connection;

public class ShowMenuCommand implements AdminCommand {
    private final Connection connection;
    private final PrintWriter out;

    public ShowMenuCommand(Connection connection, PrintWriter out) {
        this.connection = connection;
        this.out = out;
    }

    @Override
    public void execute() {
        FoodMenu foodMenu = new FoodMenu(connection);
        out.println("Current Menu:");
        out.flush();
        printMenuHeader();
        foodMenu.showMenu(out);
    }

    private void printMenuHeader() {
        out.println("-------------------------------------------");
        out.printf("%-5s | %-20s | %-10s | %-6s | %-20s |%n", "ID", "Name", "Price", "Rating", "Category");
        out.println("-------------------------------------------");
        out.flush();
    }
}
