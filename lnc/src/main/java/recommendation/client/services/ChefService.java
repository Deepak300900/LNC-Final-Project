package recommendation.client.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import recommendation.client.factories.ChefCommandFactory;
import recommendation.client.interfaces.ChefCommand;

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
            MenuDisplayService.showChefMenu();
            String choice = userInput.readLine();
            if ("8".equalsIgnoreCase(choice)) {
                System.out.println("Log out Successfully");
                out.println("Exit");
                return;
            }
            out.println(choice);
            out.flush();

            ChefCommand command = commandFactory.getCommand(choice);
            command.execute(userInput, out, in);
            
        }
    }
}
