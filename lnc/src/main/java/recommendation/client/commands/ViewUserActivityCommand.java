package recommendation.client.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import recommendation.client.interfaces.AdminCommand;

public class ViewUserActivityCommand implements AdminCommand {
    private final BufferedReader in;

    public ViewUserActivityCommand(BufferedReader in, PrintWriter out) {
        this.in = in;
    }

    @Override
    public void execute() throws IOException {
        System.out.println("Fetching user activity history from server...");
        String serverResponse;
        while (!(serverResponse = in.readLine()).equalsIgnoreCase("End of User Activity")) {
            System.out.println(serverResponse);
        }
    }
}
