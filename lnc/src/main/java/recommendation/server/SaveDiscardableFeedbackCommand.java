package recommendation.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SaveDiscardableFeedbackCommand implements EmployeeCommand {
    private final Connection connection;
    private final PrintWriter out;
    private final InputReader inputReader;
    private final String currentUser;

    public SaveDiscardableFeedbackCommand(Connection connection, PrintWriter out, InputReader inputReader, String currentUser) {
        this.connection = connection;
        this.out = out;
        this.inputReader = inputReader;
        this.currentUser = currentUser;
    }

    @Override
    public void execute() {
        try {
            int foodItemId = inputReader.readInt("Enter food item ID:");
            String feedback = inputReader.readString("Enter feedback:");

            saveDiscardableFeedback(foodItemId, feedback);
        } catch (IOException | SQLException e) {
            handleEmployeeError("Error saving discardable feedback: " + e.getMessage());
        }
    }

    private void saveDiscardableFeedback(int foodItemId, String feedback) throws SQLException {
        String sql = "INSERT INTO DiscardableFeedback (foodItemId, feedback, user) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, foodItemId);
            pstmt.setString(2, feedback);
            pstmt.setString(3, currentUser);
            int rowsAffected = pstmt.executeUpdate();
            sendFeedbackSaveResult(rowsAffected);
        }
    }

    private void sendFeedbackSaveResult(int rowsAffected) {
        if (rowsAffected > 0) {
            out.println("FEEDBACK_SAVED");
        } else {
            out.println("FEEDBACK_SAVE_FAILED");
        }
        out.flush();
    }

    private void handleEmployeeError(String message) {
        System.err.println(message);
        out.println("ERROR");
        out.flush();
    }
}
