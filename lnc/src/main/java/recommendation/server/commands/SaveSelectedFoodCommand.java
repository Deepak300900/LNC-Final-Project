package recommendation.server.commands;

import java.io.*;
import java.sql.*;

import recommendation.server.helpers.SaveSelectedFoodHelper;
import recommendation.server.interfaces.ChefCommand;

public class SaveSelectedFoodCommand implements ChefCommand {
    private final Connection connection;
    private final BufferedReader in;
    private final PrintWriter out;

    public SaveSelectedFoodCommand(Connection connection, BufferedReader in, PrintWriter out) {
        this.connection = connection;
        this.in = in;
        this.out = out;
    }

    @Override
    public void execute() throws IOException, SQLException {
        new SaveSelectedFoodHelper(connection, in, out).addFoodItemToPrepared();
    }
}
