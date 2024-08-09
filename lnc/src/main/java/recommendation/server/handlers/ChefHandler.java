package recommendation.server.handlers;

import java.io.*;
import java.sql.*;

import recommendation.server.factory.ChefCommandFactory;

public class ChefHandler implements RoleHandler {
    private final Connection connection;
    private final BufferedReader in;
    private final PrintWriter out;

    public ChefHandler(Connection connection, BufferedReader in, PrintWriter out) {
        this.connection = connection;
        this.in = in;
        this.out = out;
    }

    @Override
    public void process() {
        try {
            new RecommendationHandler().calculateSentiment(connection);
            System.out.println("Calculated sentiment");
            handleCommands();
        } catch (IOException | SQLException e) {
            handleException(e);
        }
    }

    private void handleCommands() throws IOException, SQLException {
        ChefCommandFactory commandFactory = new ChefCommandFactory(connection, in, out);
        ChefCommandHandler commandHandler = new ChefCommandHandler(commandFactory);
        while (true) {
            String command = in.readLine();
            if ( "exit".equalsIgnoreCase(command)) {
                break;
            }
            commandHandler.handle(command);
        }
    }

    private void handleException(Exception e) {
        e.printStackTrace();
        out.println("An error occurred. Please try again later.");
        out.println("End of Response");
        out.flush();
    }
}
