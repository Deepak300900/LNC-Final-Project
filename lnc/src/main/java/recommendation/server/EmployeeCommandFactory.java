package recommendation.server;

import java.io.PrintWriter;
import java.sql.Connection;

public class EmployeeCommandFactory {
    public static EmployeeCommand getCommand(String command, Connection connection, PrintWriter out, InputReader inputReader, String currentUser) {
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
            default:
                return null;
        }
    }
}
