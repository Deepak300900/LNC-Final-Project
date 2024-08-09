package recommendation.server.commands;

import java.io.*;

import recommendation.server.interfaces.ChefCommand;

public class InvalidCommand implements ChefCommand {
    private final PrintWriter out;

    public InvalidCommand(PrintWriter out) {
        this.out = out;
    }

    @Override
    public void execute() {
        out.println("Invalid command");
        out.println("End of Response");
        out.flush();
    }
}
