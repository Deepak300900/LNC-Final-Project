package recommendation.server.factory;

import java.io.PrintWriter;
import java.sql.Connection;

import recommendation.server.commands.CheckFeedbackCommand;
import recommendation.server.commands.FetchDiscardableItemsCommand;
import recommendation.server.commands.FetchPreparedItemsCommand;
import recommendation.server.commands.FetchVotingItemsCommand;
import recommendation.server.commands.SaveDiscardableFeedbackCommand;
import recommendation.server.commands.SaveFeedbackCommand;
import recommendation.server.commands.SaveVoteCommand;
import recommendation.server.commands.UpdateProfileCommand;
import recommendation.server.commands.ShowNotificationCommand;
import recommendation.server.handlers.Communicationhandler;
import recommendation.server.interfaces.EmployeeCommand;

public class EmployeeCommandFactory {
    public static EmployeeCommand getCommand(String command, Connection connection, PrintWriter out, Communicationhandler inputReader, String currentUser) {
        switch (command.toUpperCase()) {
            case "FETCH_PREPARED_ITEMS":
                return new FetchPreparedItemsCommand(connection, out);
            case "CHECK_FEEDBACK":
                return new CheckFeedbackCommand(connection, out, inputReader, currentUser);
            case "SAVE_FEEDBACK":
                return new SaveFeedbackCommand(connection, out, inputReader, currentUser);
            case "FETCH_VOTING_ITEMS":
                return new FetchVotingItemsCommand(connection, out, currentUser);
            case "SAVE_VOTE":
                return new SaveVoteCommand(connection, out, inputReader,currentUser);
            case "FETCH_DISCARDABLE_ITEMS":
                return new FetchDiscardableItemsCommand(connection, out);
            case "SAVE_DISCARDABLE_FEEDBACK":
                return new SaveDiscardableFeedbackCommand(connection, out, inputReader, currentUser);
            case "UPDATE_PROFILE":
                return new UpdateProfileCommand(connection, out, inputReader, currentUser);
            case "SHOW_NOTIFICATION":
                return new ShowNotificationCommand(connection, out, inputReader, currentUser);
            default:
                return null;
        }
    }
}
