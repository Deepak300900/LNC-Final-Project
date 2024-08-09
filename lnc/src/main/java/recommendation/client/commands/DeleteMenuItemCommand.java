package recommendation.client.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import recommendation.client.interfaces.AdminCommand;

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
        String input = userInput.readLine();
        input = input.isEmpty() || input.isBlank() ? "0" : input;
        out.println(input);
        String response = in.readLine();
        System.out.println("=> " +response);
    }
}
