package recommendation.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EmployeeHandler implements RoleHandler {
    private final Connection connection;
    private final BufferedReader in;
    private final PrintWriter out;
    private static final String CURRENT_USER = "garvit.tambi@intimetec.com";

    public EmployeeHandler(Connection connection, BufferedReader in, PrintWriter out) {
        this.connection = connection;
        this.in = in;
        this.out = out;
    }

    @Override
    public void process() {
        System.out.println("Processing employee tasks...");
        try {
            handleEmployeeCommands();
        } catch (IOException e) {
            handleEmployeeError("Error handling employee commands: " + e.getMessage());
        }
    }

    private void handleEmployeeCommands() throws IOException {
        while (true) {
            String command = in.readLine();
            System.out.println("Received command: " + command);

            if (command == null || "EXIT".equalsIgnoreCase(command)) {
                System.out.println("Exiting employee commands.");
                break;
            }

            switch (command.toUpperCase()) {
                case "FETCH_PREPARED_ITEMS":
                    fetchPreparedItems();
                    break;
                case "CHECK_FEEDBACK":
                    checkFeedback();
                    break;
                case "SAVE_FEEDBACK":
                    saveFeedback();
                    break;
                case "FETCH_VOTING_ITEMS":
                    fetchVotingItems();
                    break;
                case "SAVE_VOTE":
                    saveVote();
                    break;
                case "FETCH_DISCARDABLE_ITEMS":
                    fetchDiscardableItems();
                    break;
                case "SAVE_DISCARDABLE_FEEDBACK":
                    saveDiscardableFeedback();
                    break;
                default:
                    out.println("INVALID_COMMAND");
                    out.flush();
            }
        }
    }

    private void fetchPreparedItems() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT id, name, price FROM Foodmenu WHERE id IN (SELECT FoodItemId FROM preparedFoodItem WHERE CreatedDate = CURDATE())");
            while (rs.next()) {
                sendItem(rs.getInt("id"), rs.getString("name"), rs.getFloat("price"));
            }
            sendEndOfItems();
        } catch (SQLException e) {
            handleEmployeeError("Error fetching prepared items: " + e.getMessage());
        }
    }

    private void checkFeedback() throws IOException {
        int foodItemId = Integer.parseInt(in.readLine());

        String sql = "SELECT COUNT(*) FROM UserFeedBack WHERE foodItemId = ? AND user = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, foodItemId);
            pstmt.setString(2, CURRENT_USER);
            ResultSet rs = pstmt.executeQuery();
            sendFeedbackCheckResult(rs);
        } catch (SQLException e) {
            handleEmployeeError("Error checking feedback: " + e.getMessage());
        }
    }

    private void saveFeedback() throws IOException {
        int foodItemId = Integer.parseInt(in.readLine());
        String feedback = in.readLine();
        int rating = Integer.parseInt(in.readLine());

        String sql = "INSERT INTO UserFeedBack (foodItemId, feedback, rating, user) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, foodItemId);
            pstmt.setString(2, feedback);
            pstmt.setInt(3, rating);
            pstmt.setString(4, CURRENT_USER);
            int rowsAffected = pstmt.executeUpdate();
            sendFeedbackSaveResult(rowsAffected);
        } catch (SQLException e) {
            handleEmployeeError("Error saving feedback: " + e.getMessage());
        }
    }

    private void fetchVotingItems() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT id, name, price FROM foodmenu WHERE id IN (SELECT foodItemId FROM RolloutFoodItems WHERE createdDate = CURDATE())");
            while (rs.next()) {
                sendItem(rs.getInt("id"), rs.getString("name"), rs.getFloat("price"));
            }
            sendEndOfItems();
        } catch (SQLException e) {
            handleEmployeeError("Error fetching voting items: " + e.getMessage());
        }
    }

    private void saveVote() throws IOException {
        int foodItemId = Integer.parseInt(in.readLine());

        String updateSql = "UPDATE RolloutFoodItems SET VotingCount = VotingCount + 1 WHERE foodItemId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(updateSql)) {
            pstmt.setInt(1, foodItemId);
            int rowsAffected = pstmt.executeUpdate();
            sendVoteSaveResult(rowsAffected);
        } catch (SQLException e) {
            handleEmployeeError("Error saving vote: " + e.getMessage());
        }
    }

    private void fetchDiscardableItems() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT id, name, price FROM Foodmenu WHERE id IN (SELECT foodItemId FROM discardableFood)");
            while (rs.next()) {
                sendItem(rs.getInt("id"), rs.getString("name"), rs.getFloat("price"));
            }
            sendEndOfItems();
        } catch (SQLException e) {
            handleEmployeeError("Error fetching discardable items: " + e.getMessage());
        }
    }

    private void saveDiscardableFeedback() throws IOException {
        try {
            int foodItemId = Integer.parseInt(in.readLine());
            String concern = in.readLine();
            String improvement = in.readLine();
            String momRecipe = in.readLine();

            String sql = "INSERT INTO DiscardableFoodFeedback (foodItemId, userEmail, foodConcern, improvement, momRecipe) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, foodItemId);
                pstmt.setString(2, CURRENT_USER);
                pstmt.setString(3, concern);
                pstmt.setString(4, improvement);
                pstmt.setString(5, momRecipe);
                int rowsAffected = pstmt.executeUpdate();
                sendFeedbackSaveResult(rowsAffected);
            }
        } catch (SQLException e) {
            handleEmployeeError("Error saving discardable feedback: " + e.getMessage());
        }
    }

    private void sendItem(int id, String name, float price) {
        out.println(id + "," + name + "," + price);
        out.flush();
    }

    private void sendEndOfItems() {
        out.println("END_OF_ITEMS");
        out.flush();
    }

    private void sendFeedbackSaveResult(int rowsAffected) {
        if (rowsAffected > 0) {
            out.println("FEEDBACK_SAVED");
        } else {
            out.println("FEEDBACK_SAVE_FAILED");
        }
        out.flush();
    }

    private void sendFeedbackCheckResult(ResultSet rs) throws SQLException {
        if (rs.next() && rs.getInt(1) > 0) {
            out.println("HAS_FEEDBACK");
        } else {
            out.println("NO_FEEDBACK");
        }
        out.flush();
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
