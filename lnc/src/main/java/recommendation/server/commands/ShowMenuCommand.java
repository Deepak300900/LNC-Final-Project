package recommendation.server.commands;

import java.io.PrintWriter;
import java.sql.Connection;

import recommendation.server.helpers.FoodMenuHelper;
import recommendation.server.interfaces.AdminCommand;

public class ShowMenuCommand implements AdminCommand {
    private final Connection connection;
    private final PrintWriter out;

    public ShowMenuCommand(Connection connection, PrintWriter out) {
        this.connection = connection;
        this.out = out;
    }

    @Override
    public void execute() {
        FoodMenuHelper foodMenu = new FoodMenuHelper(connection);
        out.println("Current Menu:");
        out.flush();
        foodMenu.showMenu(out);
    }
}
