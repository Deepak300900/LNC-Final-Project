package recommendation.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class AddFoodItemToWillPrepareCommand implements ChefCommand {
    @Override
    public void execute(BufferedReader userInput, PrintWriter out, BufferedReader in) throws IOException {
        System.out.print("Enter Food Item ID to add to Will Prepare: ");
        out.println(Integer.parseInt(userInput.readLine()));
        out.flush();
    }
}