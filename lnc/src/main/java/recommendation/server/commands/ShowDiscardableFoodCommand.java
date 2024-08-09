package recommendation.server.commands;

import java.io.*;
import java.sql.*;

import recommendation.server.helpers.DiscardableFoodHelper;
import recommendation.server.interfaces.ChefCommand;

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
        DiscardableFoodHelper discardableFoodHandler = new DiscardableFoodHelper(connection, in, out);
        discardableFoodHandler.showDiscardableFood();
        discardableFoodHandler.handleDiscardableFoodCommands();
    }
}
