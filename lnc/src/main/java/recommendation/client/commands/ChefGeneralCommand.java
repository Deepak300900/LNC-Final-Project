package recommendation.client.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import recommendation.client.services.ServerResponseService;
import recommendation.client.interfaces.ChefCommand;

public class ChefGeneralCommand implements ChefCommand {
    @Override
    public void execute(BufferedReader userInput, PrintWriter out, BufferedReader in) throws IOException {
        ServerResponseService.printServerResponse(in);
    }
}