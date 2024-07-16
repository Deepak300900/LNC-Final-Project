package recommendation.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

public class AdminHandler implements RoleHandler {
    private final Connection connection;
    private final InputReader inputReader;
    private final PrintWriter out;

    public AdminHandler(Connection connection, BufferedReader in, PrintWriter out) {
        this.connection = connection;
        this.inputReader = new InputReader(in);
        this.out = out;
    }

    @Override
    public void process() {
        System.out.println("Processing admin tasks...");
        try {
            handleAdminCommands();
        } catch (IOException e) {
            handleAdminError("Error handling admin commands: " + e.getMessage());
        }
    }

    private void handleAdminCommands() throws IOException {
        String command;
        while ((command = inputReader.readString("Enter command (1-5, 6 to exit):")) != null && !"6".equals(command)) {
            System.out.println("Received command: " + command);
            AdminCommand adminCommand = AdminCommandFactory.getCommand(command, inputReader, connection, out);
            if (adminCommand != null) {
                adminCommand.execute();
            } else {
                out.println("Invalid command");
                out.flush();
            }
        }
        System.out.println("Log Out Successfully.");
    }

    private void handleAdminError(String errorMessage) {
        System.err.println(errorMessage);
        out.println(errorMessage);
        out.flush();
    }
}
