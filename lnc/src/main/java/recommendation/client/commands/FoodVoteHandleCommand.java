package recommendation.client.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import recommendation.client.exceptions.InvalidInputException;
import recommendation.client.interfaces.EmployeeCommand;

public class FoodVoteHandleCommand implements EmployeeCommand {
    private static final String FETCH_VOTING_ITEMS = "FETCH_VOTING_ITEMS";
    private static final String END_OF_ITEMS = "END_OF_ITEMS";
    private static final String SAVE_VOTE = "SAVE_VOTE";
    private static final String VOTE_SAVED = "VOTE_SAVED";

    private final BufferedReader in;
    private final PrintWriter out;
    private final BufferedReader userInput;

    public FoodVoteHandleCommand(BufferedReader in, PrintWriter out, BufferedReader userInput) {
        this.in = in;
        this.out = out;
        this.userInput = userInput;
    }

    @Override
    public void execute() throws IOException, InvalidInputException {
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
            double price = Double.parseDouble(item[2]);
            System.out.printf("%-5d | %-20s | %-10.2f |%n", id, name, price);
            System.out.println("-------------------------------------------");
            foodItemIds.add(id);
        } catch (NumberFormatException e) {
            System.out.println("Invalid data format." + e);
        }
    }

    private void collectVotes(List<Integer> foodItemIds) throws IOException, InvalidInputException {
        while (true) {
            System.out.print("Enter the ID of the item you want to vote for (or enter 'done' to finish): ");
            String input = userInput.readLine().trim();
            if (input.equalsIgnoreCase("done")) {
                return;
            }
            handleVoteInput(input, foodItemIds);
        }
    }

    private void handleVoteInput(String input, List<Integer> foodItemIds) throws IOException, InvalidInputException {
        try {
            int id = Integer.parseInt(input);
            if (!foodItemIds.contains(id)) {
                throw new InvalidInputException("Invalid ID. Please enter a valid ID from the list.");
            }

            out.println(SAVE_VOTE);
            out.println(id);

            if (in.readLine().equalsIgnoreCase(VOTE_SAVED)) {
                System.out.println("Vote submitted successfully.");
            } else {
                System.out.println("Failed to submit vote.");
            }
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Invalid input. Please enter a valid number or 'done' to finish.");
        }
    }
}
