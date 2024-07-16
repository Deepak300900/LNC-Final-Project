package recommendation.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class DefaultCommand implements ChefCommand {
    @Override
    public void execute(BufferedReader userInput, PrintWriter out, BufferedReader in) throws IOException {
        System.out.println("Invalid option. Please try again.");
    }
}