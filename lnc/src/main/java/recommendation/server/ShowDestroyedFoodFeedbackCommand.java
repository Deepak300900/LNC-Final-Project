package recommendation.server;

import java.io.*;
import java.sql.*;

public class ShowDestroyedFoodFeedbackCommand implements ChefCommand {
    private final Connection connection;
    private final PrintWriter out;

    public ShowDestroyedFoodFeedbackCommand(Connection connection, PrintWriter out) {
        this.connection = connection;
        this.out = out;
    }

    @Override
    public void execute() throws SQLException {
        new DestroyedFoodFeedbackHandler(connection, out).showDestroyedFoodFeedback();
    }
}
