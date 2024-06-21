package recommendation.client;

import java.io.*;

public class ChefService {
    private final BufferedReader userInput;
    private final PrintWriter out;
    private final BufferedReader in;

    public ChefService(BufferedReader userInput, BufferedReader in, PrintWriter out) {
        this.userInput = userInput;
        this.out = out;
        this.in = in;
    }

    public void handleCommands() throws IOException {
        while (true) {
            showMenu();
            String choice = userInput.readLine();
            out.println(choice);
            out.flush();
            if ("exit".equalsIgnoreCase(choice)) {
                return;
            }
            handleChefCommand(choice);
        }
    }

    private void showMenu() {
        System.out.println("\n1. Show Food Menu Items");
        System.out.println("2. Show Recommended Food");
        System.out.println("3. Suggest Foods for Tomorrow");
        System.out.println("4. Show Voting on Suggested Foods for Tomorrow");
        System.out.println("5. Select Food Item for Tomorrow");
        System.out.println("6. Show Discardable Food");
        System.out.println("7. Show Destroyed Food Feedback"); // New option
        System.out.println("Type 'exit' to exit.");
        System.out.print("Enter your choice: ");
    }

    private void handleChefCommand(String choice) throws IOException {
        switch (choice) {
            case "1":
            case "2":
            case "4":
            case "6":
            case "7": // New case for showing destroyed food feedback
                handleServerResponse(choice);
                break;
            case "3":
                addFoodItemToWillPrepare();
                handleServerResponse(choice);
                break;
            case "5":
                addFoodItemToPrepared();
                handleServerResponse(choice);
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private void addFoodItemToWillPrepare() throws IOException {
        System.out.print("Enter Food Item ID to add to Will Prepare: ");
        out.println(Integer.parseInt(userInput.readLine()));
        out.flush();
    }

    private void addFoodItemToPrepared() throws IOException {
        System.out.print("Enter Food Item ID to add to Prepared Food Items: ");
        out.println(Integer.parseInt(userInput.readLine()));
        out.flush();
    }

    private void handleServerResponse(String choice) throws IOException {
        String serverResponse;
        while (!(serverResponse = in.readLine()).equalsIgnoreCase("End of Response")) {
            System.out.println(serverResponse);
        }
        
        // Handle additional commands for discardable food
        if ("6".equals(choice)) {
            handleDiscardableFoodOptions();
        }
    }

    private void handleDiscardableFoodOptions() throws IOException {
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
        
        String serverResponse;
        while (!(serverResponse = in.readLine()).equalsIgnoreCase("End of Response")) {
            System.out.println(serverResponse);
        }
    }
}
