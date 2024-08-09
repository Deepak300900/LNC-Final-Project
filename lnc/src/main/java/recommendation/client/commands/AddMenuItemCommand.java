package recommendation.client.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import recommendation.client.interfaces.AdminCommand;

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
        String name = getUserInput("Enter name: ");
        out.println(name);
        while (!isValidInput(name)) {
            name = getUserInput("Enter name: ");
            out.println(name);
        }

        String price = getUserInput("Enter price: ");
        out.println(price);
        while (!isValidInput(price)) {
            price = getUserInput("Enter price: ");
            out.println(price);
        }

        String category = getUserInput("Enter category: ");
        out.println(category);
        while (!isValidInput(category)) {
            category = getUserInput("Enter category: ");
            out.println(category);
        }

        String type = getUserInput("Enter type: ");
        out.println(type);
        while (!isValidInput(type)) {
            type = getUserInput("Enter type: ");
            out.println(type);
        }

        String test = getUserInput("Enter test: ");
        out.println(test);
        while (!isValidInput(test)) {
            test = getUserInput("Enter test: ");
            out.println(test);
        }

        System.out.println("=> " + in.readLine());
    }

    private String getUserInput(String prompt) throws IOException {
        System.out.print(prompt);
        return userInput.readLine();
    }

    private boolean isValidInput(String input) throws IOException {
        String response = in.readLine();
        if ("SUCCESS".equals(response)) {
            return true;
        } else {
            System.out.println(in.readLine());
            return false;
        }
    }
}
