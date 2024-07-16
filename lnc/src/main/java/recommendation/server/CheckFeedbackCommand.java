package recommendation.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckFeedbackCommand implements EmployeeCommand {
    private final Connection connection;
    private final PrintWriter out;
    private final InputReader inputReader;
    private final String currentUser;

    public CheckFeedbackCommand(Connection connection, PrintWriter out, InputReader inputReader, String currentUser) {
        this.connection = connection;
        this.out = out;
        this.inputReader = inputReader;
        this.currentUser = currentUser;
    }

    @Override
    public void execute() {
        try {
            int foodItemId = inputReader.readInt("Enter food item ID:");
            checkFeedback(foodItemId);
        } catch (IOException | SQLException e) {
            handleEmployeeError("Error checking feedback: " + e.getMessage());
        }
    }

    private void checkFeedback(int foodItemId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM UserFeedBack WHERE foodItemId = ? AND user = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, foodItemId);
            pstmt.setString(2, currentUser);
            ResultSet rs = pstmt.executeQuery();
            sendFeedbackCheckResult(rs);
        }
    }

    private void sendFeedbackCheckResult(ResultSet rs) throws SQLException {
        if (rs.next() && rs.getInt(1) > 0) {
            out.println("HAS_FEEDBACK");
        } else {
            out.println("NO_FEEDBACK");
        }
        out.flush();
    }

    private void handleEmployeeError(String message) {
        System.err.println(message);
        out.println("ERROR");
        out.flush();
    }
}
