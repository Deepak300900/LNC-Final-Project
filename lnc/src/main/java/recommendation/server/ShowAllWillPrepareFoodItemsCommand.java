package recommendation.server;

import java.io.*;
import java.sql.*;

public class ShowAllWillPrepareFoodItemsCommand implements ChefCommand {
    private final Connection connection;
    private final BufferedReader in;
    private final PrintWriter out;

    public ShowAllWillPrepareFoodItemsCommand(Connection connection, BufferedReader in, PrintWriter out) {
        this.connection = connection;
        this.in = in;
        this.out = out;
    }

    @Override
    public void execute() throws IOException, SQLException {
        new WillPrepareHandler(connection, in, out).showAllWillPrepareFoodItems();
    }
}
