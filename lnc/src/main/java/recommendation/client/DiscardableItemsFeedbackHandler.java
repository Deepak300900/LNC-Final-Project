package recommendation.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class DiscardableItemsFeedbackHandler implements EmployeeCommand {
    private static final String DISCARDABLE_ITEMS_FEEDBACK = "DISCARDABLE_ITEMS_FEEDBACK";
    private static final String FEEDBACK_SAVED = "FEEDBACK_SAVED";

    private final BufferedReader in;
    private final PrintWriter out;
    private final BufferedReader userInput;

    public DiscardableItemsFeedbackHandler(BufferedReader in, PrintWriter out, BufferedReader userInput) {
        this.in = in;
        this.out = out;
        this.userInput = userInput;
    }

    @Override
    public void execute() throws IOException, InvalidInputException {
        System.out.print("Enter the name of the discardable item: ");
        String itemName = userInput.readLine().trim();
        if (itemName.isEmpty()) {
            throw new InvalidInputException("Item name cannot be empty.");
        }

        out.println(DISCARDABLE_ITEMS_FEEDBACK);
        out.println(itemName);

        if (in.readLine().equalsIgnoreCase(FEEDBACK_SAVED)) {
            System.out.println("Feedback on discardable item submitted successfully.");
        } else {
            System.out.println("Failed to submit feedback on discardable item.");
        }
    }
}
