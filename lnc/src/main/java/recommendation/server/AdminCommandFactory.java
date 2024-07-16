package recommendation.server;

import java.io.PrintWriter;
import java.sql.Connection;

public class AdminCommandFactory {
    public static AdminCommand getCommand(String input, InputReader inputReader, Connection connection, PrintWriter out) {
        switch (input) {
            case "1":
                return new AddMenuItemCommand(inputReader, connection, out);
            case "2":
                return new UpdateMenuItemCommand(inputReader, connection, out);
            case "3":
                return new DeleteMenuItemCommand(inputReader, connection, out);
            case "4":
                return new ShowMenuCommand(connection, out);
            case "5":
                return new ViewUserActivityCommand(connection, out);
            default:
                return null;
        }
    }
}
