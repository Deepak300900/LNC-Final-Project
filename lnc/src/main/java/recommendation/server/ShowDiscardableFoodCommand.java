package recommendation.server;

import java.io.*;
import java.sql.*;

public class ShowDiscardableFoodCommand implements ChefCommand {
    private final Connection connection;
    private final BufferedReader in;
    private final PrintWriter out;

    public ShowDiscardableFoodCommand(Connection connection, BufferedReader in, PrintWriter out) {
        this.connection = connection;
        this.in = in;
        this.out = out;
    }

    @Override
    public void execute() throws IOException, SQLException {
        DiscardableFoodHandler discardableFoodHandler = new DiscardableFoodHandler(connection, in, out);
        discardableFoodHandler.showDiscardableFood();
        discardableFoodHandler.handleDiscardableFoodCommands();
    }
}
