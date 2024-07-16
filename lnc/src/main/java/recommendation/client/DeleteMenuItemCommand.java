package recommendation.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class DeleteMenuItemCommand implements AdminCommand {
    private final BufferedReader userInput;
    private final BufferedReader in;
    private final PrintWriter out;

    public DeleteMenuItemCommand(BufferedReader userInput, BufferedReader in, PrintWriter out) {
        this.userInput = userInput;
        this.in = in;
        this.out = out;
    }

    @Override
    public void execute() throws IOException {
        System.out.print("Enter id: ");
        out.println(userInput.readLine());
        System.out.println("=> " + in.readLine());
    }
}
