package recommendation.client.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import recommendation.client.exceptions.InvalidInputException;
import recommendation.client.interfaces.EmployeeCommand;

public class DiscardableFoodFeedbackCommand implements EmployeeCommand {
    private static final String DISCARDABLE_ITEMS_FEEDBACK = "SAVE_DISCARDABLE_FEEDBACK";
    private static final String FEEDBACK_SAVED = "FEEDBACK_SAVED";
    private static final String FETCH_DISCARDABLE_ITEMS = "FETCH_DISCARDABLE_ITEMS";
    
    private final BufferedReader in;
    private final PrintWriter out;
    private final BufferedReader userInput;

    public DiscardableFoodFeedbackCommand(BufferedReader in, PrintWriter out, BufferedReader userInput) {
        this.in = in;
        this.out = out;
        this.userInput = userInput;
    }

    @Override
    public void execute() throws IOException, InvalidInputException {
        out.println(FETCH_DISCARDABLE_ITEMS);

        String discardableFoodItem = in.readLine();

        if (discardableFoodItem.equals("End of Response")) {
            System.out.println("No discardable items available.");
            return;
        }

        String[] foodItemDetails = discardableFoodItem.split(",");
        int foodItemId = Integer.parseInt(foodItemDetails[0]);
        printFoodItemDetail(foodItemDetails);

        out.println(DISCARDABLE_ITEMS_FEEDBACK);
        out.println(foodItemId);
        String response = in.readLine();
        if("INVALID_FEEDBACK".equals(response)){
                System.out.println("Feedback has been already you gaved.");
                return;
        }
        
        System.out.println("What is wrong with this item? ");
        out.println(userInput.readLine());
        System.out.println("What can we improve in this? ");
        out.println(userInput.readLine());
        System.out.println("Please share your mom's recipe: ");
        out.println(userInput.readLine());

        if (in.readLine().equalsIgnoreCase(FEEDBACK_SAVED)) {
            System.out.println("Feedback on discardable item submitted successfully.");
        } else {
            System.out.println("Failed to submit feedback. Please try again..");
        }
    }

    private static void printFoodItemDetail(String[] foodItemDetails) {
        System.out.println("\n-----------------------------------------------------\n");
        System.out.println("Discardable Food Item Details:");
        System.out.println("Food Item Id: " + foodItemDetails[0]);
        System.out.println("Name: " + foodItemDetails[1]);
        System.out.println("Price: $" + foodItemDetails[2] + "\n");
    }
}
