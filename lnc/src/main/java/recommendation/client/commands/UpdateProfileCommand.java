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
        out.println("UPDATE_PROFILE");
        System.out.print("Enter new name: ");
        String newName = userInput.readLine().trim();
        out.println(newName);

        System.out.print("Enter new password: ");
        String newPassword = userInput.readLine().trim();
        out.println(newPassword);

        System.out.print("Enter new category: ");
        String newCategory = userInput.readLine().trim();
        out.println(newCategory);

        System.out.print("Enter new test: ");
        String newTest = userInput.readLine().trim();
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
