package recommendation.server.handlers;

import java.io.*;
import java.sql.*;

import recommendation.server.factory.ChefCommandFactory;
import recommendation.server.interfaces.ChefCommand;

public class ChefCommandHandler {
    private final ChefCommandFactory commandFactory;

    public ChefCommandHandler(ChefCommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    public void handle(String command) throws IOException, SQLException {
        ChefCommand chefCommand = commandFactory.getCommand(command);
        chefCommand.execute();
    }
}
