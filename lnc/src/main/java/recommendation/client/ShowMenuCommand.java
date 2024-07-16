package recommendation.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ShowMenuCommand implements AdminCommand {
    private final BufferedReader in;
    public ShowMenuCommand(BufferedReader in, PrintWriter out) {
        this.in = in;
    }

    @Override
    public void execute() throws IOException {
        System.out.println("Fetching menu from server...");
        String serverResponse;
        while (!(serverResponse = in.readLine()).equalsIgnoreCase("End of Menu")) {
            System.out.println(serverResponse);
        }
    }
}
