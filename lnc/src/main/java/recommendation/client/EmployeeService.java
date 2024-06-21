package recommendation.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {
    private static final String FETCH_PREPARED_ITEMS = "FETCH_PREPARED_ITEMS";
    private static final String FETCH_VOTING_ITEMS = "FETCH_VOTING_ITEMS";
    private static final String CHECK_FEEDBACK = "CHECK_FEEDBACK";
    private static final String SAVE_FEEDBACK = "SAVE_FEEDBACK";
    private static final String SAVE_VOTE = "SAVE_VOTE";
    private static final String FETCH_DISCARDABLE_ITEMS = "FETCH_DISCARDABLE_ITEMS";
    private static final String SAVE_DISCARDABLE_FEEDBACK = "SAVE_DISCARDABLE_FEEDBACK";
    private static final String END_OF_ITEMS = "END_OF_ITEMS";
    private static final String HAS_FEEDBACK = "HAS_FEEDBACK";
    private static final String FEEDBACK_SAVED = "FEEDBACK_SAVED";
    private static final String VOTE_SAVED = "VOTE_SAVED";

    private final BufferedReader userInput;
    private final BufferedReader in;
    private final PrintWriter out;

    public EmployeeService(BufferedReader userInput, BufferedReader in, PrintWriter out) {
        this.userInput = userInput;
        this.in = in;
        this.out = out;
    }

    public void handleCommands() throws IOException {
        while (true) {
            displayMenu();
            String command = userInput.readLine().trim();
            if (command.equalsIgnoreCase("EXIT")) {
                System.out.println("Log Out Successfully.");
                break;
            }
            handleEmployeeCommand(command);
        }
    }

    private void displayMenu() {
        System.out.println("\nEmployee Service Menu:");
        System.out.println("1. Feedback for Today");
        System.out.println("2. Vote for Tomorrow");
        System.out.println("3. Discardable Items Feedback");
        System.out.println("Type 'EXIT' to quit");
        System.out.print("Select an option: ");
    }

    private void handleEmployeeCommand(String command) throws IOException {
        try {
            int choice = Integer.parseInt(command);
            switch (choice) {
                case 1 -> handleFeedbackForToday();
                case 2 -> handleVoteForTomorrow();
                case 3 -> handleDiscardableItemsFeedback();
                default -> System.out.println("Invalid command number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }

    private void handleFeedbackForToday() throws IOException {
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

    private void collectFeedback(List<Integer> foodItemIds, List<String> foodItemNames) throws IOException {
        while (true) {
            System.out.print("Enter the ID of the item you want to give feedback on (or enter 'done' to finish): ");
            String input = userInput.readLine().trim();
            if (input.equalsIgnoreCase("done")) {
                return;
            }
            handleFeedbackInput(input, foodItemIds, foodItemNames);
        }
    }

    private void handleFeedbackInput(String input, List<Integer> foodItemIds, List<String> foodItemNames) throws IOException {
        try {
            int id = Integer.parseInt(input);
            if (!foodItemIds.contains(id)) {
                System.out.println("Invalid ID. Please enter a valid ID from the list.");
                return;
            }

            out.println(CHECK_FEEDBACK);
            out.println(id);

            if (in.readLine().equalsIgnoreCase(HAS_FEEDBACK)) {
                System.out.println("You already gave feedback on this item.");
                return;
            }

            saveFeedback(id, foodItemNames.get(foodItemIds.indexOf(id)));

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number or 'done' to finish.");
        }
    }

    private void saveFeedback(int id, String foodItemName) throws IOException {
        System.out.print("Enter your feedback for " + foodItemName + ": ");
        String feedback = userInput.readLine();
        System.out.print("Enter your rating (1-5) for " + foodItemName + ": ");
        int rating = Integer.parseInt(userInput.readLine());

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

    private void handleVoteForTomorrow() throws IOException {
        System.out.println("Fetching food items for voting...");
        out.println(FETCH_VOTING_ITEMS);

        List<Integer> foodItemIds = new ArrayList<>();
        displayFoodItemsForVoting(foodItemIds);

        if (foodItemIds.isEmpty()) {
            System.out.println("No food items available for voting.");
            return;
        }

        collectVotes(foodItemIds);
    }

    private void displayFoodItemsForVoting(List<Integer> foodItemIds) throws IOException {
        System.out.println("Food Items for Tomorrow:");
        System.out.println("-------------------------------------------");
        System.out.printf("%-5s | %-20s | %-10s |%n", "ID", "Name", "Price");
        System.out.println("-------------------------------------------");

        String response;
        while (!(response = in.readLine()).equalsIgnoreCase(END_OF_ITEMS)) {
            String[] item = response.split(",");
            addFoodItemForVoting(foodItemIds, item);
        }
    }

    private void addFoodItemForVoting(List<Integer> foodItemIds, String[] item) {
        try {
            int id = Integer.parseInt(item[0]);
            String name = item[1];
            float price = Float.parseFloat(item[2]);
            System.out.printf("%-5d | %-20s | %-10.2f |%n", id, name, price);
            System.out.println("-------------------------------------------");
            foodItemIds.add(id);
        } catch (NumberFormatException e) {
            System.out.println("Invalid data format.");
        }
    }

    private void collectVotes(List<Integer> foodItemIds) throws IOException {
        System.out.print("Enter the IDs of the items you want to vote for (separated by commas): ");
        String[] ids = userInput.readLine().split(",");
        for (String idStr : ids) {
            handleVoteInput(idStr.trim(), foodItemIds);
        }
    }

    private void handleVoteInput(String input, List<Integer> foodItemIds) throws IOException {
        try {
            int id = Integer.parseInt(input);
            if (!foodItemIds.contains(id)) {
                System.out.println("Invalid ID: " + id + ". Skipping.");
                return;
            }
            out.println(SAVE_VOTE);
            out.println(id);
            if (in.readLine().equalsIgnoreCase(VOTE_SAVED)) {
                System.out.println("Vote for item ID " + id + " submitted successfully.");
            } else {
                System.out.println("Failed to submit vote for item ID " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input: " + input + ". Skipping.");
        }
    }

    private void handleDiscardableItemsFeedback() throws IOException {
        System.out.println("Fetching discardable food items...");
        out.println(FETCH_DISCARDABLE_ITEMS);

        List<Integer> foodItemIds = new ArrayList<>();
        List<String> foodItemNames = new ArrayList<>();

        displayDiscardableItems(foodItemIds, foodItemNames);

        if (foodItemIds.isEmpty()) {
            System.out.println("No discardable food items available.");
            return;
        }

        collectDiscardableFeedback(foodItemIds, foodItemNames);
    }

    private void displayDiscardableItems(List<Integer> foodItemIds, List<String> foodItemNames) throws IOException {
        System.out.println("Discardable Food Items: ");
        System.out.println("-------------------------------------------");
        System.out.printf("%-5s | %-20s | %-10s |%n", "ID", "Name", "Price");
        System.out.println("-------------------------------------------");

        String response;
        while (!(response = in.readLine()).equalsIgnoreCase(END_OF_ITEMS)) {
            String[] item = response.split(",");
            addFoodItemToLists(foodItemIds, foodItemNames, item);
        }
    }

    private void collectDiscardableFeedback(List<Integer> foodItemIds, List<String> foodItemNames) throws IOException {
        while (true) {
            System.out.print("Enter the ID of the item you want to give feedback on (or enter 'done' to finish): ");
            String input = userInput.readLine().trim();
            if (input.equalsIgnoreCase("done")) {
                return;
            }
            handleDiscardableFeedbackInput(input, foodItemIds, foodItemNames);
        }
    }

    private void handleDiscardableFeedbackInput(String input, List<Integer> foodItemIds, List<String> foodItemNames) throws IOException {
        try {
            int id = Integer.parseInt(input);
            if (!foodItemIds.contains(id)) {
                System.out.println("Invalid ID. Please enter a valid ID from the list.");
                return;
            }

            System.out.print("What is wrong with the food: ");
            String concern = userInput.readLine();
            System.out.print("How can we improve it: ");
            String improvement = userInput.readLine();
            System.out.print("Share your mom's recipe: ");
            String momRecipe = userInput.readLine();

            out.println(SAVE_DISCARDABLE_FEEDBACK);
            out.println(id);
            out.println(concern);
            out.println(improvement);
            out.println(momRecipe);

            if (in.readLine().equalsIgnoreCase(FEEDBACK_SAVED)) {
                System.out.println("Feedback submitted successfully.");
            } else {
                System.out.println("Failed to submit feedback.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number or 'done' to finish.");
        }
    }
}