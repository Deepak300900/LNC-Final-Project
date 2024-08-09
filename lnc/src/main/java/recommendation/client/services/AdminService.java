package recommendation.client.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import recommendation.client.factories.AdminCommandFactory;
import recommendation.client.interfaces.AdminCommand;

public class AdminService implements RoleService {
    private final BufferedReader userInput;
    private final BufferedReader in;
    private final PrintWriter out;

    public AdminService(BufferedReader userInput, BufferedReader in, PrintWriter out) {
        this.userInput = userInput;
        this.in = in;
        this.out = out;
    }

    public void handleCommands() throws IOException {
        while (true) {
            MenuDisplayService.showAdminMenu();
            String input = userInput.readLine().trim();
            out.println(input);
            if ("6".equals(input)) {
                System.out.println("Log Out Successfully");
                break;
            }
            handleMenuCommand(input);
        }
    }

    private void handleMenuCommand(String input) throws IOException {
        AdminCommand command = AdminCommandFactory.getCommand(input, userInput, in, out);
        if (command != null) {
            command.execute();
        } else {
            System.out.println("Invalid command");
        }
    }
}
