package recommendation.server.commands;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import recommendation.server.handlers.Communicationhandler;
import recommendation.server.interfaces.EmployeeCommand;

public class SaveVoteCommand implements EmployeeCommand {
    private final Connection connection;
    private final PrintWriter out;
    private final Communicationhandler inputReader;

    public SaveVoteCommand(Connection connection, PrintWriter out, Communicationhandler inputReader, String currentUser) {
        this.connection = connection;
        this.out = out;
        this.inputReader = inputReader;
    }

    @Override
    public void execute() {
        try {
            int foodItemId = inputReader.readInt("Enter food item ID:");
           // int vote = inputReader.readInt("Enter vote (1 for UP, -1 for DOWN):");
            System.out.println("foodItemId: " + foodItemId);
            saveVote(foodItemId);
        } catch (IOException | SQLException e) {
            handleEmployeeError("Error saving vote: " + e.getMessage());
        }
    }

    private void saveVote(int foodItemId) throws SQLException {
        String sql = "UPDATE rolloutfooditems SET VotingCount = VotingCount + 1 WHERE createdDate = CURDATE() AND foodItemId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, foodItemId);
            int rowsAffected = pstmt.executeUpdate();
            sendVoteSaveResult(rowsAffected);
        }
        catch(SQLException e){
                System.out.println("Failed to Save Vote" + e);
        }
    }

    private void sendVoteSaveResult(int rowsAffected) {
        if (rowsAffected > 0) {
            out.println("VOTE_SAVED");
        } else {
            out.println("VOTE_SAVE_FAILED");
        }
        out.flush();
    }

    private void handleEmployeeError(String message) {
        System.err.println(message);
        out.println("ERROR");
        out.flush();
    }
}
