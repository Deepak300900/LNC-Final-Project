package recommendation.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

public class UpdateMenuItemCommand implements AdminCommand {
    private final InputReader inputReader;
    private final Connection connection;
    private final PrintWriter out;

    public UpdateMenuItemCommand(InputReader inputReader, Connection connection, PrintWriter out) {
        this.inputReader = inputReader;
        this.connection = connection;
        this.out = out;
    }

    @Override
    public void execute() throws IOException {
        int id = inputReader.readInt("Enter ID:");
        String name = inputReader.readString("Enter name:");
        double price = inputReader.readDouble("Enter price:");
        String category = inputReader.readString("Enter category:");

        FoodMenu foodMenu = new FoodMenu(connection);
        boolean result = foodMenu.updateMenuItem(id, name, price, category);
        printOperationResult(result, "Menu item updated successfully", "Failed to update menu item");
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
}
