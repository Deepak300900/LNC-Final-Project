package recommendation.server.commands;

import java.io.*;
import java.sql.*;

import recommendation.server.helpers.FoodMenuHelper;
import recommendation.server.interfaces.ChefCommand;

public class ShowFoodMenuItemsCommand implements ChefCommand {
    private final Connection connection;
    private final PrintWriter out;

    public ShowFoodMenuItemsCommand(Connection connection, PrintWriter out) {
        this.connection = connection;
        this.out = out;
    }

    @Override
    public void execute() throws SQLException {
        new FoodMenuHelper(connection).showMenu(out);
    }
}
