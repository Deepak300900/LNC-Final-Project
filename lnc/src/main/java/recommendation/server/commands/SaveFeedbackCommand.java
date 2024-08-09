package recommendation.server.commands;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import recommendation.server.handlers.Communicationhandler;
import recommendation.server.helpers.FoodMenuHelper;
import recommendation.server.interfaces.EmployeeCommand;

public class SaveFeedbackCommand implements EmployeeCommand {
    private final Connection connection;
    private final PrintWriter out;
    private final Communicationhandler inputReader;
    private final String currentUser;

    public SaveFeedbackCommand(Connection connection, PrintWriter out, Communicationhandler inputReader, String currentUser) {
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
            int rating = inputReader.readInt("Enter rating:");

            saveFeedback(foodItemId, feedback, rating);
        } catch (IOException | SQLException e) {
            handleEmployeeError("Error saving feedback: " + e.getMessage());
        }
    }

    private void saveFeedback(int foodItemId, String feedback, int rating) throws SQLException {
        String sql = "INSERT INTO UserFeedBack (foodItemId, feedback, rating, user) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, foodItemId);
            pstmt.setString(2, feedback);
            pstmt.setInt(3, rating);
            pstmt.setString(4, currentUser);
            int rowsAffected = pstmt.executeUpdate();
            sendFeedbackSaveResult(rowsAffected);
            new FoodMenuHelper(connection).updateAverageRating(foodItemId);
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
