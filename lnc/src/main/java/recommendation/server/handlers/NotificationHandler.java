package recommendation.server.handlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NotificationHandler {
    private final Connection connection;

    public NotificationHandler(Connection connection) {
        this.connection = connection;
    }

    public boolean addNotification(String subject, String detail) {
        String query = "INSERT INTO Notification (Subject, Detail) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, subject);
            statement.setString(2, detail);
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void printNotificationStatus(boolean status){
          if(status){
                System.out.println("Successfully added notification");
          }else{
                System.out.println("Failed to add notification");

          }
    }
}
