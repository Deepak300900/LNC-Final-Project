package recommendation.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

public class DeleteMenuItemCommand implements AdminCommand {
    private final InputReader inputReader;
    private final Connection connection;
    private final PrintWriter out;

    public DeleteMenuItemCommand(InputReader inputReader, Connection connection, PrintWriter out) {
        this.inputReader = inputReader;
        this.connection = connection;
        this.out = out;
    }

    @Override
    public void execute() throws IOException {
        int id = inputReader.readInt("Enter ID:");

        FoodMenu foodMenu = new FoodMenu(connection);
        boolean result = foodMenu.deleteMenuItem(id);
        printOperationResult(result, "Menu item deleted successfully", "Failed to delete menu item");
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
