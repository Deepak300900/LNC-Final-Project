package recommendation.server.commands;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import recommendation.server.handlers.Communicationhandler;
import recommendation.server.handlers.NotificationHandler;
import recommendation.server.helpers.FoodMenuHelper;
import recommendation.server.interfaces.AdminCommand;

public class AddMenuItemCommand implements AdminCommand {
    private final Communicationhandler inputReader;
    private final Connection connection;
    private final PrintWriter out;

    public AddMenuItemCommand(Communicationhandler inputReader, Connection connection, PrintWriter out) {
        this.inputReader = inputReader;
        this.connection = connection;
        this.out = out;
    }

    @Override
    public void execute() throws IOException {
        String name = inputReader.readValidString("Enter name:", "Invalid Name.", out);
        double price = inputReader.readValidDouble("Enter price:", "Invalid price.", out);
        String category = inputReader.readValidString("Enter category:", "Invalid category.", out);
        String type = inputReader.readValidString("Enter type:", "Invalid type.", out);
        String test = inputReader.readValidString("Enter test:", "Invalid test.", out);

        FoodMenuHelper foodMenu = new FoodMenuHelper(connection);
        boolean result = foodMenu.addMenuItem(name, price, category, type, test);

        inputReader.printOperationResult(result, "Menu item added successfully", "Failed to add menu item", out);

        if (result) {
            NotificationHandler notificationHandler = new NotificationHandler(connection);
            boolean notificationResult = notificationHandler.addNotification("New Menu Item Added", "Item: " + name + ", " + price + ", " + category + ", " + type + ", " + test);
            notificationHandler.printNotificationStatus(notificationResult);
        }
    }
}
