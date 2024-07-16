package recommendation.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class AddMenuItemCommand implements AdminCommand {
    private final BufferedReader userInput;
    private final BufferedReader in;
    private final PrintWriter out;

    public AddMenuItemCommand(BufferedReader userInput, BufferedReader in, PrintWriter out) {
        this.userInput = userInput;
        this.in = in;
        this.out = out;
    }

    @Override
    public void execute() throws IOException {
        System.out.print("Enter name: ");
        out.println(userInput.readLine());
        System.out.print("Enter price: ");
        out.println(userInput.readLine());
        System.out.print("Enter category: ");
        out.println(userInput.readLine());
        System.out.println("=> " + in.readLine());
    }
}