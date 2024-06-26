package recommendation.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class AdminService {
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
        switch (input) {
            case "1":
                handleAddMenuItem();
                break;
            case "2":
                handleUpdateMenuItem();
                break;
            case "3":
                handleDeleteMenuItem();
                break;
            case "4":
                handleShowMenu();
                break;
            case "5":
                handleViewUserActivity();
                break;
            default:
                System.out.println("Invalid command");
        }
    }

    private void handleAddMenuItem() throws IOException {
        System.out.print("Enter name: ");
        out.println(userInput.readLine());
        System.out.print("Enter price: ");
        out.println(userInput.readLine());
        System.out.print("Enter rating: ");
        out.println(userInput.readLine());
        System.out.print("Enter category: ");
        out.println(userInput.readLine());
        System.out.println("=> " + in.readLine());
    }

    private void handleUpdateMenuItem() throws IOException {
        System.out.print("Enter id: ");
        out.println(userInput.readLine());
        System.out.print("Enter name: ");
        out.println(userInput.readLine());
        System.out.print("Enter price: ");
        out.println(userInput.readLine());
        System.out.print("Enter rating: ");
        out.println(userInput.readLine());
        System.out.print("Enter category: ");
        out.println(userInput.readLine());
        System.out.println("=> " + in.readLine());
    }

    private void handleDeleteMenuItem() throws IOException {
        System.out.print("Enter id: ");
        out.println(userInput.readLine());
        System.out.println("=> " + in.readLine());
    }

    private void handleShowMenu() throws IOException {
        System.out.println("Fetching menu from server...");
        String serverResponse;
        while (!(serverResponse = in.readLine()).equalsIgnoreCase("End of Menu")) {
            System.out.println(serverResponse);
        }
    }

    private void handleViewUserActivity() throws IOException {
        System.out.println("Fetching user activity history from server...");
        String serverResponse;
        while (!(serverResponse = in.readLine()).equalsIgnoreCase("End of User Activity")) {
            System.out.println(serverResponse);
        }
    }
}
