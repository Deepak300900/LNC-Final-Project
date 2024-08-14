package recommendation.server.commands;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import recommendation.server.handlers.Communicationhandler;
import recommendation.server.interfaces.EmployeeCommand;

public class SaveVoteCommand implements EmployeeCommand {
        private final Connection connection;
        private final PrintWriter out;
        private final Communicationhandler inputReader;
        private final String currentUser;

        public SaveVoteCommand(Connection connection, PrintWriter out, Communicationhandler inputReader,
                        String currentUser) {
                this.connection = connection;
                this.out = out;
                this.inputReader = inputReader;
                this.currentUser = currentUser;
        }

        @Override
        public void execute() {
                try {
                        int foodItemId = inputReader.readInt("Enter food item ID:");
                        System.out.println("foodItemId: " + foodItemId);
                        if (isValidateVote(foodItemId)) {
                                saveVote(foodItemId);
                        } else {
                                out.println("INVALID_VOTE");
                        }

                } catch (IOException | SQLException e) {
                        handleEmployeeError("Error saving vote: " + e.getMessage());
                }
        }

        private boolean isValidateVote(int foodId) {
                LocalDate today = LocalDate.now();
                String query = "SELECT COUNT(*) FROM FoodVote WHERE foodId = ? AND user = ? AND date = ?";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setInt(1, foodId);
                        stmt.setString(2, this.currentUser);
                        stmt.setDate(3, Date.valueOf(today));
                        try (ResultSet rs = stmt.executeQuery()) {
                                if (rs.next() && rs.getInt(1) > 0) {
                                        return false;
                                }
                        }
                } catch (SQLException e) {
                        e.printStackTrace();
                }
                return true;
        }

        private void saveVote(int foodItemId) throws SQLException {
                LocalDate today = LocalDate.now();

                String insertQuery = "INSERT INTO FoodVote (foodId, user, date) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {

                        stmt.setInt(1, foodItemId);
                        stmt.setString(2, currentUser);
                        stmt.setDate(3, Date.valueOf(today));

                        stmt.executeUpdate();
                } catch (SQLException e) {
                        e.printStackTrace();
                        throw e;
                }

                String sql = "UPDATE rolloutfooditems SET VotingCount = VotingCount + 1 WHERE createdDate = CURDATE() AND foodItemId = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                        pstmt.setInt(1, foodItemId);
                        int rowsAffected = pstmt.executeUpdate();
                        sendVoteSaveResult(rowsAffected);
                } catch (SQLException e) {
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
