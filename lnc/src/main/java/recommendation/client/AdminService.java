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
            if ("7".equals(input)) {
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
        System.out.println("6. Show Discardable Food");
        System.out.println("7. EXIT");
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
            case "6":
                handleDiscardableFoodOptions();
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

    private void handleDiscardableFoodOptions() throws IOException {

        String serverResponse;
        while (!(serverResponse = in.readLine()).equalsIgnoreCase("End of Response")) {
            System.out.println(serverResponse);
        }

        System.out.println("1. Delete Discardable Food");
        System.out.println("2. Store Discardable Food Data");
        System.out.print("Enter your choice: ");
        String choice = userInput.readLine();
        out.println(choice);
        out.flush();
        
        if ("1".equals(choice)) {
            System.out.print("Enter Food Item ID to delete: ");
            out.println(Integer.parseInt(userInput.readLine()));
            out.flush();
        } else if ("2".equals(choice)) {
            System.out.print("Enter Food Item ID to store in discardable table: ");
            out.println(Integer.parseInt(userInput.readLine()));
            out.flush();
        } else {
            System.out.println("Invalid option. Please try again.");
        }
        
        String serverResponse2;
        while (!(serverResponse2 = in.readLine()).equalsIgnoreCase("End of Response")) {
            System.out.println(serverResponse2);
        }
    }
}
