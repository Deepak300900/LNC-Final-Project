package recommendation.server.commands;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.sql.PreparedStatement;
import java.util.List;

import recommendation.server.interfaces.EmployeeCommand;
import recommendation.server.models.FoodItem;

public class FetchVotingItemsCommand implements EmployeeCommand {
    private final Connection connection;
    private final PrintWriter out;

    public FetchVotingItemsCommand(Connection connection, PrintWriter out, String currentUser) {
        this.connection = connection;
        this.out = out;
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

        String sql = "SELECT id, name, price, Rating, category, test FROM Foodmenu WHERE id IN (SELECT foodItemId FROM rolloutfooditems WHERE DATE(createdDate) = CURDATE())";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                foodItems.add(new FoodItem(rs.getInt("id"), rs.getString("name"), rs.getFloat("price"), rs.getFloat("Rating"), rs.getString("category"), rs.getString("test")));
            }
        }

        foodItems.sort(Comparator.comparing(FoodItem::getAverageRating).reversed());
        for (FoodItem item : foodItems) {
            sendVotingItem(item);
        }  
        sendEndOfItems();
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

