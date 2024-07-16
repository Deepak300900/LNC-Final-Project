package recommendation.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SaveVoteCommand implements EmployeeCommand {
    private final Connection connection;
    private final PrintWriter out;
    private final InputReader inputReader;
    private final String currentUser;

    public SaveVoteCommand(Connection connection, PrintWriter out, InputReader inputReader, String currentUser) {
        this.connection = connection;
        this.out = out;
        this.inputReader = inputReader;
        this.currentUser = currentUser;
    }

    @Override
    public void execute() {
        try {
            int foodItemId = inputReader.readInt("Enter food item ID:");
            int vote = inputReader.readInt("Enter vote (1 for UP, -1 for DOWN):");

            saveVote(foodItemId, vote);
        } catch (IOException | SQLException e) {
            handleEmployeeError("Error saving vote: " + e.getMessage());
        }
    }

    private void saveVote(int foodItemId, int vote) throws SQLException {
        String sql = "INSERT INTO Vote (foodItemId, vote, user) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, foodItemId);
            pstmt.setInt(2, vote);
            pstmt.setString(3, currentUser);
            int rowsAffected = pstmt.executeUpdate();
            sendVoteSaveResult(rowsAffected);
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
