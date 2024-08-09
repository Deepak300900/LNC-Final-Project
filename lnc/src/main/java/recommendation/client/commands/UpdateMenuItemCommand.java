package recommendation.client.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import recommendation.client.interfaces.AdminCommand;

public class UpdateMenuItemCommand implements AdminCommand {
    private final BufferedReader userInput;
    private final BufferedReader in;
    private final PrintWriter out;

    public UpdateMenuItemCommand(BufferedReader userInput, BufferedReader in, PrintWriter out) {
        this.userInput = userInput;
        this.in = in;
        this.out = out;
    }

    @Override
    public void execute() throws IOException {
        System.out.print("Enter id: ");
        out.println(validateString(userInput.readLine()));
        System.out.print("Enter name: ");
        out.println(validateString(userInput.readLine()));
        System.out.print("Enter price: ");
        out.println(validateString(userInput.readLine()));
        System.out.print("Enter category: ");
        out.println(validateString(userInput.readLine()));
        System.out.println("=> " + in.readLine());
    }

    private String validateString(String str){
        return str.isBlank() || str.isEmpty() ? "-1" : str;
    }
}
