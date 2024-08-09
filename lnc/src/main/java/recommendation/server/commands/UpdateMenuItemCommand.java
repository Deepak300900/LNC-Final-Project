package recommendation.server.commands;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import recommendation.server.handlers.Communicationhandler;
import recommendation.server.handlers.NotificationHandler;
import recommendation.server.helpers.FoodMenuHelper;
import recommendation.server.interfaces.AdminCommand;


public class UpdateMenuItemCommand implements AdminCommand {
    private final Communicationhandler inputReader;
    private final Connection connection;
    private final PrintWriter out;

    public UpdateMenuItemCommand(Communicationhandler inputReader, Connection connection, PrintWriter out) {
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

        FoodMenuHelper foodMenu = new FoodMenuHelper(connection);
        boolean result = foodMenu.updateMenuItem(id, name, price, category);
        inputReader.printOperationResult(result, "Menu item updated successfully", "Failed to update menu item", out);
        if (result) {
                try {
                    String foodDetails = foodMenu.getFoodItemDetails(id);
                    NotificationHandler notificationHandler = new NotificationHandler(connection);
                    notificationHandler.addNotification("Menu Item Updated", foodDetails);
                } catch (SQLException e) {
                    out.println("Failed to retrieve food item details for notification.");
                    out.flush();
                    e.printStackTrace();
                }
            }
        }

}
