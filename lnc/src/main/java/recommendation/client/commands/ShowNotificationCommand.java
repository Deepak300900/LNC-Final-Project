package recommendation.client.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import recommendation.client.exceptions.InvalidInputException;
import recommendation.client.interfaces.EmployeeCommand;
import recommendation.client.services.ServerResponseService;

public class ShowNotificationCommand implements EmployeeCommand {
    private final BufferedReader in;
    private final PrintWriter out;

    public ShowNotificationCommand(BufferedReader in, PrintWriter out, BufferedReader userInput) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void execute() throws IOException, InvalidInputException {
        out.println("SHOW_NOTIFICATION");
        out.flush();
        ServerResponseService.printServerResponse(in);
    }
}
