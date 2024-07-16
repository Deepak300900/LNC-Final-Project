package recommendation.server;

import java.io.*;
import java.sql.*;

public class ShowFoodMenuItemsCommand implements ChefCommand {
    private final Connection connection;
    private final PrintWriter out;

    public ShowFoodMenuItemsCommand(Connection connection, PrintWriter out) {
        this.connection = connection;
        this.out = out;
    }

    @Override
    public void execute() throws SQLException {
        new MenuItemHandler(connection, out).showFoodMenuItems();
    }
}
