package recommendation.server;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.sql.PreparedStatement;
import java.util.List;

public class FetchVotingItemsCommand implements EmployeeCommand {
    private final Connection connection;
    private final PrintWriter out;
    private final String currentUser;

    public FetchVotingItemsCommand(Connection connection, PrintWriter out, String currentUser) {
        this.connection = connection;
        this.out = out;
        this.currentUser = currentUser;
    }

    @Override
    public void execute() {
        try {
            fetchVotingItems();
        } catch (SQLException e) {
            handleEmployeeError("Error fetching voting items: " + e.getMessage());
        }
    }

    private void fetchVotingItems() throws SQLException {
        List<FoodItem> foodItems = new ArrayList<>();
        UserInfo userInfo = getUserInfo(currentUser);
        String cuisine = userInfo.cuisine;

        String sql = "SELECT id, name, price, averageRating FROM Foodmenu WHERE cuisine = ? AND id NOT IN (SELECT foodItemId FROM Vote WHERE user = ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, cuisine);
            pstmt.setString(2, currentUser);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                foodItems.add(new FoodItem(rs.getInt("id"), rs.getString("name"), rs.getFloat("price"), rs.getFloat("averageRating")));
            }
        }

        foodItems.sort(Comparator.comparing(FoodItem::getAverageRating).reversed());
        for (FoodItem item : foodItems) {
            sendVotingItem(item);
        }
        sendEndOfItems();
    }

    private UserInfo getUserInfo(String email) throws SQLException {
        String sql = "SELECT id, cuisine FROM Users WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new UserInfo(rs.getInt("id"), rs.getString("cuisine"));
            }
            throw new SQLException("User not found");
        }
    }

    private void sendVotingItem(FoodItem item) {
        out.println(item.getId() + "," + item.getName() + "," + item.getPrice() + "," + item.getAverageRating());
        out.flush();
    }

    private void sendEndOfItems() {
        out.println("END_OF_ITEMS");
        out.flush();
    }

    private void handleEmployeeError(String message) {
        System.err.println(message);
        out.println("ERROR");
        out.flush();
    }
}

class FoodItem {
    private final int id;
    private final String name;
    private final float price;
    private final float averageRating;

    public FoodItem(int id, String name, float price, float averageRating) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.averageRating = averageRating;
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

    public float getAverageRating() {
        return averageRating;
    }
}

class UserInfo {
    public final int id;
    public final String cuisine;

    public UserInfo(int id, String cuisine) {
        this.id = id;
        this.cuisine = cuisine;
    }
}
