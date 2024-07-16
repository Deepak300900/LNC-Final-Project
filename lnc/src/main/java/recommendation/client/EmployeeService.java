package recommendation.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class EmployeeService implements RoleService {
    private final BufferedReader userInput;
    private final MenuDisplay menuDisplay;
    private final EmployeeCommandFactory commandFactory;

    public EmployeeService(BufferedReader userInput, BufferedReader in, PrintWriter out) {
        this.userInput = userInput;
        this.menuDisplay = new MenuDisplay();
        this.commandFactory = new EmployeeCommandFactory(in, out, userInput);
    }

    public void handleCommands() throws IOException {
        while (true) {
            menuDisplay.showMenu();
            String command = userInput.readLine().trim();
            if (command.equalsIgnoreCase("EXIT")) {
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
