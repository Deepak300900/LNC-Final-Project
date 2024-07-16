package recommendation.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ChefService implements RoleService {
    private final BufferedReader userInput;
    private final PrintWriter out;
    private final BufferedReader in;
    private final ChefCommandFactory commandFactory;

    public ChefService(BufferedReader userInput, BufferedReader in, PrintWriter out) {
        this.userInput = userInput;
        this.out = out;
        this.in = in;
        this.commandFactory = new ChefCommandFactory();
    }

    public void handleCommands() throws IOException {
        while (true) {
            Menu.display();
            String choice = userInput.readLine();
            out.println(choice);
            out.flush();
            if ("exit".equalsIgnoreCase(choice)) {
                return;
            }

            ChefCommand command = commandFactory.getCommand(choice);
            command.execute(userInput, out, in);
        }
    }
}
