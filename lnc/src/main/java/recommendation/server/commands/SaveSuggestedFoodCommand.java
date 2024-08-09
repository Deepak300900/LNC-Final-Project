package recommendation.server.commands;

import java.io.*;
import java.sql.*;

import recommendation.server.helpers.SuggestedFoodHelper;
import recommendation.server.interfaces.ChefCommand;

public class SaveSuggestedFoodCommand implements ChefCommand {
    private final Connection connection;
    private final BufferedReader in;
    private final PrintWriter out;

    public SaveSuggestedFoodCommand(Connection connection, BufferedReader in, PrintWriter out) {
        this.connection = connection;
        this.in = in;
        this.out = out;
    }

    @Override
    public void execute() throws IOException, SQLException {
        new SuggestedFoodHelper(connection, in, out).addFoodItemToWillPrepare();
    }
}
