package recommendation.client;

import java.io.BufferedReader;
import java.io.PrintWriter;

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
                case 1 -> new FeedbackForTodayHandler(in, out, userInput);
                case 2 -> new VoteForTomorrowHandler(in, out, userInput);
                case 3 -> new DiscardableItemsFeedbackHandler(in, out, userInput);
                default -> throw new InvalidCommandException("Invalid command number.");
            };
        } catch (NumberFormatException e) {
            throw new InvalidCommandException("Invalid input. Please enter a number.");
        }
    }
}
