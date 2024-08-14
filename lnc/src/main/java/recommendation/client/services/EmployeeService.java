package recommendation.client.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import recommendation.client.exceptions.InvalidCommandException;
import recommendation.client.exceptions.InvalidInputException;
import recommendation.client.factories.EmployeeCommandFactory;
import recommendation.client.interfaces.EmployeeCommand;

public class EmployeeService implements RoleService {
    private final BufferedReader userInput;
    private final EmployeeCommandFactory commandFactory;
    private final PrintWriter out;

    public EmployeeService(BufferedReader userInput, BufferedReader in, PrintWriter out) {
        this.userInput = userInput;
        this.out = out;
        this.commandFactory = new EmployeeCommandFactory(in, out, userInput);
    }

    public void handleCommands() throws IOException {
        while (true) {
            MenuDisplayService.showEmployeeMenu();
            String command = userInput.readLine().trim();
            if (command.equalsIgnoreCase("6")) {
                out.println("EXIT");
                System.out.println("Log Out Successfully.");
                break;
            }
            handleMenuCommand(command);
        }
    }

    private void handleMenuCommand(String input) {
        try {
            EmployeeCommand command = commandFactory.getCommand(input);
            if (command != null) {
                command.execute();
            } else {
                System.out.println("Invalid command");
            }
        } catch (InvalidCommandException | InvalidInputException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
