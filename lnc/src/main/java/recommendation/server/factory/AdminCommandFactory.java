package recommendation.server.factory;

import java.io.PrintWriter;
import java.sql.Connection;

import recommendation.server.commands.AddMenuItemCommand;
import recommendation.server.commands.DeleteMenuItemCommand;
import recommendation.server.commands.ShowMenuCommand;
import recommendation.server.commands.UpdateMenuItemCommand;
import recommendation.server.commands.ViewUserActivityCommand;
import recommendation.server.handlers.Communicationhandler;
import recommendation.server.interfaces.AdminCommand;

public class AdminCommandFactory {
    public static AdminCommand getCommand(String input, Communicationhandler inputReader, Connection connection, PrintWriter out) {
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
