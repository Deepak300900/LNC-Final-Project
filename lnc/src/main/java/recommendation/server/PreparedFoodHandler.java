package recommendation.server;

import java.io.*;
import java.sql.*;

public class PreparedFoodHandler {
    private final Connection connection;
    private final BufferedReader in;
    private final PrintWriter out;

    public PreparedFoodHandler(Connection connection, BufferedReader in, PrintWriter out) {
        this.connection = connection;
        this.in = in;
        this.out = out;
    }

    public void addFoodItemToPrepared() throws IOException, SQLException {
        try {
            int preparedFoodItemId = Integer.parseInt(in.readLine());
            String query = "INSERT INTO preparedFoodItem (foodItemId, CreatedDate) VALUES (?, CURDATE())";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, preparedFoodItemId);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    out.println("Food item added to preparedFoodItem successfully.");
                } else {
                    out.println("Failed to add food item.");
                }
                out.println("End of Response");
                out.flush();
            }
        } catch (NumberFormatException e) {
            out.println("Invalid input format.");
            out.println("End of Response");
            out.flush();
        }
    }
}
