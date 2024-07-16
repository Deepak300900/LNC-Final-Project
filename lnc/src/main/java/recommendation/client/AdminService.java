package recommendation.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

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
            showMenuOptions();
            String input = userInput.readLine().trim();
            out.println(input);
            if ("6".equals(input)) {
                System.out.println("Log Out Successfully");
                break;
            }
            handleMenuCommand(input);
        }
    }

    private void showMenuOptions() {
        System.out.println("\n1. ADD_MENU_ITEM");
        System.out.println("2. UPDATE_MENU_ITEM");
        System.out.println("3. DELETE_MENU_ITEM");
        System.out.println("4. SHOW_MENU");
        System.out.println("5. VIEW_USER_ACTIVITY");
        System.out.println("6. EXIT");
        System.out.print("Enter your choice: ");
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
