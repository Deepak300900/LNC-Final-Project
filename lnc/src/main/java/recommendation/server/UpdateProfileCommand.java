package recommendation.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateProfileCommand implements EmployeeCommand {
    private final Connection connection;
    private final PrintWriter out;
    private final InputReader inputReader;
    private final String currentUser;

    public UpdateProfileCommand(Connection connection, PrintWriter out, InputReader inputReader, String currentUser) {
        this.connection = connection;
        this.out = out;
        this.inputReader = inputReader;
        this.currentUser = currentUser;
    }

    @Override
    public void execute() {
        try {
            String newName = inputReader.readString("Enter new name:");
            String newCuisine = inputReader.readString("Enter new cuisine preference:");

            updateProfile(newName, newCuisine);
        } catch (IOException | SQLException e) {
            handleEmployeeError("Error updating profile: " + e.getMessage());
        }
    }

    private void updateProfile(String newName, String newCuisine) throws SQLException {
        String sql = "UPDATE Users SET name = ?, cuisine = ? WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setString(2, newCuisine);
            pstmt.setString(3, currentUser);
            int rowsAffected = pstmt.executeUpdate();
            sendProfileUpdateResult(rowsAffected);
        }
    }

    private void sendProfileUpdateResult(int rowsAffected) {
        if (rowsAffected > 0) {
            out.println("PROFILE_UPDATED");
        } else {
            out.println("PROFILE_UPDATE_FAILED");
        }
        out.flush();
    }

    private void handleEmployeeError(String message) {
        System.err.println(message);
        out.println("ERROR");
        out.flush();
    }
}
