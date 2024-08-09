package recommendation.server.commands;

import java.io.*;
import java.sql.*;

import recommendation.server.helpers.DiscardableFoodHelper;
import recommendation.server.interfaces.ChefCommand;

public class ShowDiscardableFoodFeedbackCommand implements ChefCommand {
    private final Connection connection;
    private final PrintWriter out;

    public ShowDiscardableFoodFeedbackCommand(Connection connection, PrintWriter out) {
        this.connection = connection;
        this.out = out;
    }

    @Override
    public void execute() throws SQLException {
        new DiscardableFoodHelper(connection, null, out).showDiscardableFoodFeedback();
    }
}
