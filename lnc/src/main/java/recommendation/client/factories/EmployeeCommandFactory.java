package recommendation.client.factories;

import java.io.BufferedReader;
import java.io.PrintWriter;

import recommendation.client.commands.DiscardableFoodFeedbackCommand;
import recommendation.client.commands.FoodFeedbackHandleCommand;
import recommendation.client.commands.FoodVoteHandleCommand;
import recommendation.client.commands.ShowNotificationCommand;
import recommendation.client.commands.UpdateProfileCommand;
import recommendation.client.exceptions.InvalidCommandException;
import recommendation.client.interfaces.EmployeeCommand;

public class EmployeeCommandFactory {
    private final BufferedReader in;
    private final PrintWriter out;
    private final BufferedReader userInput;

    public EmployeeCommandFactory(BufferedReader in, PrintWriter out, BufferedReader userInput) {
        this.in = in;
        this.out = out;
        this.userInput = userInput;
    }

    public EmployeeCommand getCommand(String input) throws InvalidCommandException {
        try {
            int choice = Integer.parseInt(input);
            return switch (choice) {
                case 1 -> new FoodFeedbackHandleCommand(in, out, userInput);
                case 2 -> new FoodVoteHandleCommand(in, out, userInput);
                case 3 -> new DiscardableFoodFeedbackCommand(in, out, userInput);
                case 4 -> new ShowNotificationCommand(in, out, userInput);
                case 5 -> new UpdateProfileCommand(in, out, userInput);
                default -> throw new InvalidCommandException("Invalid command number.");
            };
        } catch (NumberFormatException e) {
            throw new InvalidCommandException("Invalid input. Please enter a number.");
        }
    }
}
