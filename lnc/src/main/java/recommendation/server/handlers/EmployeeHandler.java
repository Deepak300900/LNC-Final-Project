package recommendation.server.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import recommendation.server.factory.EmployeeCommandFactory;
import recommendation.server.interfaces.EmployeeCommand;

public class EmployeeHandler implements RoleHandler {
    private final Connection connection;
    private final Communicationhandler inputReader;
    private final PrintWriter out;
    private final String currentUser;

    public EmployeeHandler(Connection connection, BufferedReader in, PrintWriter out, String email) {
        this.connection = connection;
        this.inputReader = new Communicationhandler(in);
        this.out = out;
        this.currentUser = email;
    }

    @Override
    public void process() {
        try {
            handleEmployeeCommands();
        } catch (IOException e) {
            handleEmployeeError("Error handling employee commands: " + e.getMessage());
        }
    }

    private void handleEmployeeCommands() throws IOException {
        String command;
        while ((command = inputReader.readString("Enter command:")) != null && !"EXIT".equalsIgnoreCase(command)) {
            EmployeeCommand employeeCommand = EmployeeCommandFactory.getCommand(command, connection, out, inputReader, currentUser);
            if (employeeCommand != null) {
                employeeCommand.execute();
            } else {
                out.println("INVALID_COMMAND");
                out.flush();
            }
        }
    }

    private void handleEmployeeError(String message) {
        System.err.println(message);
        out.println("ERROR");
        out.flush();
    }
}
