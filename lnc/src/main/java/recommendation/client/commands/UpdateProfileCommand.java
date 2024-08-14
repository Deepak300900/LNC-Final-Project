package recommendation.client.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import recommendation.client.exceptions.InvalidInputException;
import recommendation.client.interfaces.EmployeeCommand;

public class UpdateProfileCommand implements EmployeeCommand {
    private final BufferedReader in;
    private final PrintWriter out;
    private final BufferedReader userInput;

    public UpdateProfileCommand(BufferedReader in, PrintWriter out, BufferedReader userInput) {
        this.in = in;
        this.out = out;
        this.userInput = userInput;
    }

    @Override
    public void execute() throws IOException, InvalidInputException {
        System.out.println("\n-----------------------------------------------------------------------\n");
        System.out.println("Enter your new data if you dont want to update any thing then enter same thing:");
        out.println("UPDATE_PROFILE");
        System.out.print("Enter new name: ");
        String newName = userInput.readLine().trim();
        out.println(newName);

        System.out.print("Enter new password: ");
        String newPassword = userInput.readLine().trim();
        out.println(newPassword);

        System.out.print("Select new category: \n  1. Vegeterian\n  2. Eggeterian\n  3. NonVegeterian\nEnter choice: ");
        String newCategory = userInput.readLine().trim();
        newCategory = "1".equals(newCategory) ? "Vegeterian" : "2".equals(newCategory) ? "Eggeterian" : "3".equals(newCategory) ? "NonVegeterian" : "";
        out.println(newCategory);

        System.out.print("Select new test: \n  1. Spicy\n  2. Sweet\n  3. Salty\nEnter choice: ");
        String newTest = userInput.readLine().trim();
        newTest = "1".equals(newTest) ? "Spicy" : "2".equals(newTest) ? "Sweet" : "3".equals(newTest) ? "Salty" : "";
        out.println(newTest);
        out.flush();

        String response = in.readLine();
        if (response.equals("SUCCESS")) {
            System.out.println("Profile updated successfully.");
        } else {
            System.out.println("Failed to update profile: " + response);
        }
    }
}
