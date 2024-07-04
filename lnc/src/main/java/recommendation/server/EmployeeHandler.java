package recommendation.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EmployeeHandler implements RoleHandler {
    private final Connection connection;
    private final BufferedReader in;
    private final PrintWriter out;
    private final String CURRENT_USER;

    public EmployeeHandler(Connection connection, BufferedReader in, PrintWriter out, String email) {
        this.connection = connection;
        this.in = in;
        this.out = out;
        this.CURRENT_USER = email;
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
                    fetchVotingItems(CURRENT_USER);
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
                case "UPDATE_PROFILE":
                    updateProfile();
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
            new FoodMenu(connection).updateAverageRating(foodItemId);
        } catch (SQLException e) {
            handleEmployeeError("Error saving feedback: " + e.getMessage());
        }
    }

     public void fetchVotingItems(String currentUserEmail) {
        List<FoodItem> foodItems = new ArrayList<>();
        UserInfo userInfo = getUserInfo(currentUserEmail);

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT id, name, price, category, test FROM foodmenu WHERE id IN (SELECT foodItemId FROM RolloutFoodItems WHERE createdDate = CURDATE())");
            while (rs.next()) {
                FoodItem foodItem = new FoodItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getFloat("price"),
                        rs.getString("category"),
                        rs.getString("test")
                );
                foodItems.add(foodItem);
            }
        } catch (SQLException e) {
            handleEmployeeError("Error fetching voting items: " + e.getMessage());
            return;
        }

        foodItems.sort(Comparator
                .comparing((FoodItem item) -> item.getCategory().equals(userInfo.getCategory()) ? 0 : 1)
                .thenComparing(item -> item.getTest().equals(userInfo.getTest()) ? 0 : 1));

        for (FoodItem item : foodItems) {
            sendItem(item.getId(), item.getName(), item.getPrice());
        }
        sendEndOfItems();
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


    private void updateProfile() throws IOException {
        String updateField = in.readLine();
        switch (updateField) {
            case "UPDATE_NAME":
                updateName();
                break;
            case "UPDATE_EMAIL":
                updateEmail();
                break;
            case "UPDATE_PASSWORD":
                updatePassword();
                break;
            case "UPDATE_CATEGORY":
                updateCategory();
                break;
            case "UPDATE_TEST":
                updateTest();
                break;
            default:
                out.println("INVALID_UPDATE_FIELD");
                out.flush();
        }
    }

    private void updateName() throws IOException {
        String newName = in.readLine();
        String sql = "UPDATE UserInfo SET username = ? WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setString(2, CURRENT_USER);
            int rowsAffected = pstmt.executeUpdate();
            out.println(rowsAffected > 0 ? "Name updated successfully." : "Failed to update name.");
        } catch (SQLException e) {
            out.println("Error updating name: " + e.getMessage());
        }
        out.flush();
    }

    private void updateEmail() throws IOException {
        String newEmail = in.readLine();
        String sql = "UPDATE UserInfo SET email = ? WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newEmail);
            pstmt.setString(2, CURRENT_USER);
            int rowsAffected = pstmt.executeUpdate();
            out.println(rowsAffected > 0 ? "Email updated successfully." : "Failed to update email.");
        } catch (SQLException e) {
            out.println("Error updating email: " + e.getMessage());
        }
        out.flush();
    }

    private void updatePassword() throws IOException {
        String newPassword = in.readLine();
        String sql = "UPDATE UserInfo SET password = ? WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, CURRENT_USER);
            int rowsAffected = pstmt.executeUpdate();
            out.println(rowsAffected > 0 ? "Password updated successfully." : "Failed to update password.");
        } catch (SQLException e) {
            out.println("Error updating password: " + e.getMessage());
        }
        out.flush();
    }

    private void updateCategory() throws IOException {
        String newCategory = in.readLine();
        String sql = "UPDATE UserInfo SET category = ? WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newCategory);
            pstmt.setString(2, CURRENT_USER);
            int rowsAffected = pstmt.executeUpdate();
            out.println(rowsAffected > 0 ? "Category updated successfully." : "Failed to update category.");
        } catch (SQLException e) {
            out.println("Error updating category: " + e.getMessage());
        }
        out.flush();
    }

    private void updateTest() throws IOException {
        String newTest = in.readLine();
        String sql = "UPDATE UserInfo SET test = ? WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newTest);
            pstmt.setString(2, CURRENT_USER);
            int rowsAffected = pstmt.executeUpdate();
            out.println(rowsAffected > 0 ? "Test updated successfully." : "Failed to update test.");
        } catch (SQLException e) {
            out.println("Error updating test: " + e.getMessage());
        }
        out.flush();
    }

    private void handleEmployeeError(String message) {
        System.err.println(message);
        out.println("ERROR");
        out.flush();
    }

    private UserInfo getUserInfo(String userEmail) {
        String query = "SELECT category, test FROM userInfo WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, userEmail);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new UserInfo(rs.getString("category"), rs.getString("test"));
            }
        } catch (SQLException e) {
            handleEmployeeError("Error fetching user info: " + e.getMessage());
        }
        return new UserInfo("", ""); // Default values if user not found
    }


    private static class FoodItem {
        private final int id;
        private final String name;
        private final float price;
        private final String category;
        private final String test;

        public FoodItem(int id, String name, float price, String category, String test) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.category = category;
            this.test = test;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public float getPrice() {
            return price;
        }

        public String getCategory() {
            return category;
        }

        public String getTest() {
            return test;
        }
    }

    private static class UserInfo {
        private final String category;
        private final String test;

        public UserInfo(String category, String test) {
            this.category = category;
            this.test = test;
        }

        public String getCategory() {
            return category;
        }

        public String getTest() {
            return test;
        }
    }
}
