package recommendation.client.commands;

import java.io.*;

import recommendation.client.services.ServerResponseService;
import recommendation.client.interfaces.ChefCommand;

public class DiscardableFoodProcessCommand implements ChefCommand{

   @Override
    public void execute(BufferedReader userInput, PrintWriter out, BufferedReader in) throws IOException {
        ServerResponseService.printServerResponse(in);
        if("FOOD_AVAILABLE".equals(in.readLine())){
        handle(userInput, out, in);
        }
    }


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
                handleStoreFoodItem(userInput, out, in);
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }

        ServerResponseService.printServerResponse(in);
    }

    private static void handleDeleteFoodItem(BufferedReader userInput, PrintWriter out) throws IOException {
        System.out.print("Enter Food Item ID to delete: ");
        out.println(Integer.parseInt(userInput.readLine()));
        out.flush();
    }
                
    private static void handleStoreFoodItem(BufferedReader userInput, PrintWriter out, BufferedReader in) throws IOException {
        if("NOT_AVAILABLE".equals(in.readLine())){
          return;
        }
        System.out.print("Enter Food Item ID to store in discardable table: ");
        out.println(Integer.parseInt(userInput.readLine()));
        out.flush();
    }
}
