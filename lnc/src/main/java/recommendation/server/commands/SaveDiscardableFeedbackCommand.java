package recommendation.server.commands;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import recommendation.server.handlers.Communicationhandler;
import recommendation.server.interfaces.EmployeeCommand;

public class SaveDiscardableFeedbackCommand implements EmployeeCommand {
    private final Connection connection;
    private final PrintWriter out;
    private final Communicationhandler inputReader;
    private final String currentUser;

    public SaveDiscardableFeedbackCommand(Connection connection, PrintWriter out, Communicationhandler inputReader, String currentUser) {
        this.connection = connection;
        this.out = out;
        this.inputReader = inputReader;
        this.currentUser = currentUser;
    }

    @Override
    public void execute() {
        try {
           int foodItemId = inputReader.readInt("food item Id");
           String concern = inputReader.readString("User Concern:");
           String improvement = inputReader.readString("User Suggested improvement");
           String momRecipe = inputReader.readString("User's Mom recipe");

          saveDiscardableFeedback(foodItemId, concern, improvement, momRecipe);
        } catch (IOException | SQLException e) {
            handleEmployeeError("Error saving discardable feedback: " + e.getMessage());
        }
    }

    private void saveDiscardableFeedback(int foodItemId, String concern, String improvement, String momRecipe) throws SQLException {
        String sql = "INSERT INTO discardablefoodfeedback (foodItemId, userEmail, foodConcern, improvement, momRecipe) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, foodItemId);
            pstmt.setString(2, currentUser);
            pstmt.setString(3, concern);
            pstmt.setString(4, improvement);
            pstmt.setString(5, momRecipe);

            int rowsAffected = pstmt.executeUpdate();
            sendFeedbackSaveResult(rowsAffected);
        }
        catch(SQLException e){
                System.out.println("Failed to save feedback due to " + e);
                handleEmployeeError(e.getMessage());
        }
        System.out.println(foodItemId + ", " + concern + ", " + improvement + ", " + momRecipe);
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
