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
        System.out.println("-------------------------------\n");
        System.out.println("Please Enter Food Item details: \n");
        String name = getUserInput("Enter name: ");
        out.println(name);
        while (!isValidInput(name)) {
            name = getUserInput("\nEnter name: ");
            out.println(name);
        }

        String price = getUserInput("\nEnter price: ");
        out.println(price);
        while (!isValidInput(price)) {
            price = getUserInput("\nEnter price: ");
            out.println(price);
        }

        String category = getUserInput("\nSelect category: \n  1. Vegeterian\n  2. Eggterian\n  3. NonVegeterian\nEnter Choice ");
        category = "1".equals(category) ? "Vegeterian" : "2".equals(category) ? "Eggterian" : "3".equals(category) ? "NonVegeterian" : "";
        out.println(category);
        while (!isValidInput(category)) {
                category = getUserInput("\nSelect category: \n  1. Vegeterian\n  2. Eggterian\n  3. NonVegeterian\nEnter Choice ");
                category = "1".equals(category) ? "Vegeterian" : "2".equals(category) ? "Eggterian" : "3".equals(category) ? "NonVegeterian" : "";
            out.println(category);
        }

        String type = getUserInput("\nEnter type: \n  1. Lunch\n  2. Breakfast\n  3. Dinner\nEnter Choice ");
        type = "1".equals(type) ? "Lunch" : "2".equals(type) ? "Breakfast" : "3".equals(type) ? "Dinner" : "";
        out.println(type);
        while (!isValidInput(type)) {
                type = getUserInput("\nEnter type: \n  1. Lunch\n  2. Breakfast\n  3. Dinner\nEnter Choice ");
                type = "1".equals(type) ? "Lunch" : "2".equals(type) ? "Breakfast" : "3".equals(type) ? "Dinner" : "";        
                out.println(type);
        }

        String test = getUserInput("\nEnter test: \n  1. Sweet\n  2. Spicy\n  3. Salty\nEnter Choice ");
        test =  "1".equals(test) ? "Sweet" : "2".equals(test) ? "Spicy" : "3".equals(test) ? "Salty" : "";
        out.println(test);
        while (!isValidInput(test)) {
                test = getUserInput("\nEnter test: \n  1. Sweet\n  2. Spicy\n  3. Salty\nEnter Choice ");
                test =  "1".equals(test) ? "Sweet" : "2".equals(test) ? "Spicy" : "3".equals(test) ? "Salty" : "";
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
