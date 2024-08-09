package recommendation.client.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import recommendation.client.exceptions.InvalidInputException;
import recommendation.client.interfaces.EmployeeCommand;

public class FoodFeedbackHandleCommand implements EmployeeCommand {
    private static final String FETCH_PREPARED_ITEMS = "FETCH_PREPARED_ITEMS";
    private static final String END_OF_ITEMS = "END_OF_ITEMS";
    private static final String CHECK_FEEDBACK = "CHECK_FEEDBACK";
    private static final String SAVE_FEEDBACK = "SAVE_FEEDBACK";
    private static final String HAS_FEEDBACK = "HAS_FEEDBACK";
    private static final String FEEDBACK_SAVED = "FEEDBACK_SAVED";

    private final BufferedReader in;
    private final PrintWriter out;
    private final BufferedReader userInput;

    public FoodFeedbackHandleCommand(BufferedReader in, PrintWriter out, BufferedReader userInput) {
        this.in = in;
        this.out = out;
        this.userInput = userInput;
    }

    @Override
    public void execute() throws IOException, InvalidInputException {
        System.out.println("Fetching today's prepared food items...");
        out.println(FETCH_PREPARED_ITEMS);

        List<Integer> foodItemIds = new ArrayList<>();
        List<String> foodItemNames = new ArrayList<>();

        displayFoodItems(foodItemIds, foodItemNames);

        if (foodItemIds.isEmpty()) {
            System.out.println("No prepared food items available today.");
            return;
        }

        collectFeedback(foodItemIds, foodItemNames);
    }

    private void displayFoodItems(List<Integer> foodItemIds, List<String> foodItemNames) throws IOException {
        System.out.println("Today Prepared Food Items: ");
        System.out.println("-------------------------------------------");
        System.out.printf("%-5s | %-20s | %-10s |%n", "ID", "Name", "Price");
        System.out.println("-------------------------------------------");

        String response;
        while (!(response = in.readLine()).equalsIgnoreCase(END_OF_ITEMS)) {
            String[] item = response.split(",");
            addFoodItemToLists(foodItemIds, foodItemNames, item);
        }
    }

    private void addFoodItemToLists(List<Integer> foodItemIds, List<String> foodItemNames, String[] item) {
        try {
            int id = Integer.parseInt(item[0]);
            String name = item[1];
            double price = Double.parseDouble(item[2]);
            System.out.printf("%-5d | %-20s | %-10.2f |%n", id, name, price);
            System.out.println("-------------------------------------------");
            foodItemIds.add(id);
            foodItemNames.add(name);
        } catch (NumberFormatException e) {
            System.out.println("Invalid data format.");
        }
    }

    private void collectFeedback(List<Integer> foodItemIds, List<String> foodItemNames) throws IOException, InvalidInputException {
        while (true) {
            System.out.print("Enter the ID of the item you want to give feedback on (or enter 'done' to finish): ");
            String input = userInput.readLine().trim();
            if (input.equalsIgnoreCase("done")) {
                return;
            }
            handleFeedbackInput(input, foodItemIds, foodItemNames);
        }
    }

    private void handleFeedbackInput(String input, List<Integer> foodItemIds, List<String> foodItemNames) throws IOException, InvalidInputException {
        try {
            int id = Integer.parseInt(input);
            if (!foodItemIds.contains(id)) {
                throw new InvalidInputException("Invalid ID. Please enter a valid ID from the list.");
            }

            out.println(CHECK_FEEDBACK);
            out.println(id);

            if (in.readLine().equalsIgnoreCase(HAS_FEEDBACK)) {
                System.out.println("You already gave feedback on this item.");
                return;
            }

            saveFeedback(id, foodItemNames.get(foodItemIds.indexOf(id)));

        } catch (NumberFormatException e) {
            throw new InvalidInputException("Invalid input. Please enter a valid number or 'done' to finish.");
        }
    }

    private void saveFeedback(int id, String foodItemName) throws IOException, InvalidInputException {
        System.out.print("Enter your feedback for " + foodItemName + ": ");
        String feedback = userInput.readLine();
        System.out.print("Enter your rating (1-5) for " + foodItemName + ": ");
        int rating;
        try {
            rating = Integer.parseInt(userInput.readLine());
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Invalid rating. Please enter a number between 1 and 5.");
        }

        out.println(SAVE_FEEDBACK);
        out.println(id);
        out.println(feedback);
        out.println(rating);

        if (in.readLine().equalsIgnoreCase(FEEDBACK_SAVED)) {
            System.out.println("Feedback and rating submitted successfully.");
        } else {
            System.out.println("Failed to submit feedback and rating.");
        }
    }
}
