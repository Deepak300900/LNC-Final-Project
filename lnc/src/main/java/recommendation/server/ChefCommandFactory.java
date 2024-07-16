package recommendation.server;

import java.io.*;
import java.sql.*;

public class ChefCommandFactory {
    private final Connection connection;
    private final BufferedReader in;
    private final PrintWriter out;

    public ChefCommandFactory(Connection connection, BufferedReader in, PrintWriter out) {
        this.connection = connection;
        this.in = in;
        this.out = out;
    }

    public ChefCommand getCommand(String command) {
        switch (command) {
            case "1":
                return new ShowFoodMenuItemsCommand(connection, out);
            case "2":
                return new ShowRecommendedFoodCommand(connection, out);
            case "3":
                return new AddFoodItemToWillPrepareCommand(connection, in, out);
            case "4":
                return new ShowAllWillPrepareFoodItemsCommand(connection, in, out);
            case "5":
                return new AddFoodItemToPreparedCommand(connection, in, out);
            case "6":
                return new ShowDiscardableFoodCommand(connection, in, out);
            case "7":
                return new ShowDestroyedFoodFeedbackCommand(connection, out);
            default:
                return new InvalidCommand(out);
        }
    }
}
