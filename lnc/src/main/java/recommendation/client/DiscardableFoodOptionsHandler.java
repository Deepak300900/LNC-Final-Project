package recommendation.client;

import java.io.*;

public class DiscardableFoodOptionsHandler {
    public static void handle(BufferedReader userInput, PrintWriter out, BufferedReader in) throws IOException {
        System.out.println("1. Delete Discardable Food");
        System.out.println("2. Store Discardable Food Data");
        System.out.print("Enter your choice: ");
        String choice = userInput.readLine();
        out.println(choice);
        out.flush();

        switch (choice) {
            case "1":
                handleDeleteFoodItem(userInput, out);
                break;
            case "2":
                handleStoreFoodItem(userInput, out);
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }

        ServerResponseHandler.printServerResponse(in);
    }

    private static void handleDeleteFoodItem(BufferedReader userInput, PrintWriter out) throws IOException {
        System.out.print("Enter Food Item ID to delete: ");
        out.println(Integer.parseInt(userInput.readLine()));
        out.flush();
    }

    private static void handleStoreFoodItem(BufferedReader userInput, PrintWriter out) throws IOException {
        System.out.print("Enter Food Item ID to store in discardable table: ");
        out.println(Integer.parseInt(userInput.readLine()));
        out.flush();
    }
}
