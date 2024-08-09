package recommendation.client.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import recommendation.client.services.ServerResponseService;
import recommendation.client.interfaces.ChefCommand;

public class SaveSuggestedFoodCommand implements ChefCommand {
    @Override
    public void execute(BufferedReader userInput, PrintWriter out, BufferedReader in) throws IOException {
        System.out.print("Enter Food Item ID to add to Will Prepare: ");
        out.println(Integer.parseInt(validateUserInput(userInput.readLine())));
        out.flush();
        ServerResponseService.printServerResponse(in);
    }

    private String validateUserInput(String input){
      return input.isBlank() || input.isEmpty() ? "-1" : input;
    }


}