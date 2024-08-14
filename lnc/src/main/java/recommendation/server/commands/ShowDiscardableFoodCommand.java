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
        DiscardableFoodHelper discardableFoodHelper = new DiscardableFoodHelper(connection, in, out);
        boolean isFoodAvailabel = discardableFoodHelper.showDiscardableFood();
        out.println(isFoodAvailabel== true ? "FOOD_AVAILABLE": "");
        if(isFoodAvailabel){
        discardableFoodHelper.handleDiscardableFoodCommands();
        }    
    }
}
