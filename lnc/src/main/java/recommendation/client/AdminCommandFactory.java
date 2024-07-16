package recommendation.client;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class AdminCommandFactory {
    public static AdminCommand getCommand(String input, BufferedReader userInput, BufferedReader in, PrintWriter out) {
        switch (input) {
            case "1":
                return new AddMenuItemCommand(userInput, in, out);
            case "2":
                return new UpdateMenuItemCommand(userInput, in, out);
            case "3":
                return new DeleteMenuItemCommand(userInput, in, out);
            case "4":
                return new ShowMenuCommand(in, out);
            case "5":
                return new ViewUserActivityCommand(in, out);
            default:
                return null;
        }
    }
}
