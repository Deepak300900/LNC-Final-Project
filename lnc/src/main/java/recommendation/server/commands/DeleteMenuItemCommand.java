package recommendation.server.commands;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import recommendation.server.handlers.Communicationhandler;
import recommendation.server.handlers.NotificationHandler;
import recommendation.server.helpers.FoodMenuHelper;
import recommendation.server.interfaces.AdminCommand;

public class DeleteMenuItemCommand implements AdminCommand {
    private final Communicationhandler inputReader;
    private final Connection connection;
    private final PrintWriter out;

    public DeleteMenuItemCommand(Communicationhandler inputReader, Connection connection, PrintWriter out) {
        this.inputReader = inputReader;
        this.connection = connection;
        this.out = out;
    }

    @Override
    public void execute() throws IOException {
        int id = inputReader.readInt("Enter ID:");

        FoodMenuHelper foodMenu = new FoodMenuHelper(connection);
                try {
                        String foodDetails = foodMenu.getFoodItemDetails(id);
        boolean result = foodMenu.deleteMenuItem(id);
                        inputReader.printOperationResult(result, "Menu item deleted successfully",
                                        "Failed to delete menu item. Please enter valid ID.", out);
                        if (result) {
                                NotificationHandler notificationHandler = new NotificationHandler(connection);
                                boolean notificationResult = notificationHandler.addNotification("Menu Item Deleted",
                                                foodDetails);
                                notificationHandler.printNotificationStatus(notificationResult);
                        }
                } catch (SQLException e) {
                        System.out.println("Failed to retrieve food item details for notification.");
                }

        }

}
