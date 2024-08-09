package recommendation.server.factory;

import java.io.*;
import java.sql.*;

import recommendation.server.commands.InvalidCommand;
import recommendation.server.commands.SaveSelectedFoodCommand;
import recommendation.server.commands.SaveSuggestedFoodCommand;
import recommendation.server.commands.ShowDiscardableFoodCommand;
import recommendation.server.commands.ShowDiscardableFoodFeedbackCommand;
import recommendation.server.commands.ShowFoodMenuItemsCommand;
import recommendation.server.commands.ShowRecommendedFoodCommand;
import recommendation.server.commands.ShowSuggestedFoodCommand;
import recommendation.server.interfaces.ChefCommand;

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
                return new SaveSuggestedFoodCommand(connection, in, out);
            case "4":
                return new ShowSuggestedFoodCommand(connection, in, out);
            case "5":
                return new SaveSelectedFoodCommand(connection, in, out);
            case "6":
                return new ShowDiscardableFoodCommand(connection, in, out);
            case "7":
                return new ShowDiscardableFoodFeedbackCommand(connection, out);
            default:
                return new InvalidCommand(out);
        }
    }
}
