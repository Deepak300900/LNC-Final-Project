package recommendation.client.interfaces;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public interface ChefCommand {
    void execute(BufferedReader userInput, PrintWriter out, BufferedReader in) throws IOException;
}
