package recommendation.server;

import java.io.*;
import java.sql.*;

public class ShowRecommendedFoodCommand implements ChefCommand {
    private final Connection connection;
    private final PrintWriter out;

    public ShowRecommendedFoodCommand(Connection connection, PrintWriter out) {
        this.connection = connection;
        this.out = out;
    }

    @Override
    public void execute() throws SQLException {
        new RecommendedFoodHandler(connection, out).showRecommendedFood();
    }
}
