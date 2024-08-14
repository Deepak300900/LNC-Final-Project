package recommendation.server.commands;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import recommendation.server.handlers.Communicationhandler;
import recommendation.server.interfaces.EmployeeCommand;

public class ShowNotificationCommand implements EmployeeCommand {
    private final Connection connection;
    private final PrintWriter out;
    private final String currentUser;

    public ShowNotificationCommand(Connection connection, PrintWriter out, Communicationhandler inputReader, String currentUser) {
        this.connection = connection;
        this.out = out;
        this.currentUser = currentUser;
    }

    @Override
    public void execute() {
        showNotification();
    }

    private void showNotification() {
        final String userTest;
        final String userCategory;
        String userQuery = "SELECT test, category FROM UserInfo WHERE email = ?";

        try (PreparedStatement userStmt = connection.prepareStatement(userQuery)) {
            userStmt.setString(1, currentUser);
            try (ResultSet rs = userStmt.executeQuery()) {
                if (rs.next()) {
                    userTest =  rs.getString("test");
                    userCategory = " " + rs.getString("category");
                } else {
                    out.println("ERROR: User not found.");
                    out.flush();
                    return;
                }
            }
        } catch (SQLException e) {
            out.println("ERROR: Unable to fetch user details.");
            e.printStackTrace(out);
            out.flush();
            return;
        }

        String query = "SELECT id, subject, detail FROM Notification " +
                "WHERE " +
                "id NOT IN (SELECT notificationId FROM NotificationVisibility WHERE user = ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, currentUser);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Notification> notifications = new ArrayList<>();

                while (rs.next()) {
                    String notificationId = rs.getString("id");
                    String subject = rs.getString("subject");
                    String details = rs.getString("detail");

                    boolean containsTest = details.contains(userTest);
                    boolean containsCategory = details.contains(userCategory);

                    notifications.add(new Notification(notificationId, subject, details, containsTest, containsCategory));
                }
                notifications.sort(Comparator.comparing((Notification n) -> {
                    return n.containsCategory && n.containsTest;
                }).reversed());
                 notifications.sort(Comparator.comparing((Notification n) -> {
                        return n.containsCategory;
                    }).reversed());
                out.println("Notifications ==>");  
                for (Notification notification : notifications) {
                    out.println("=> " + notification.subject + " details: " + notification.details);
                    markAsSeenNotification(notification.id);
                }
                out.println("End of Response");
                out.flush();
            }
        } catch (SQLException e) {
            out.println("ERROR: Unable to fetch notifications.");
            e.printStackTrace(out);
            out.flush();
        }
    }

    private void markAsSeenNotification(String notificationId) {
        String insertQuery = "INSERT INTO NotificationVisibility (notificationId, user) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
            stmt.setString(1, notificationId);
            stmt.setString(2, currentUser);
            stmt.executeUpdate();
        } catch (SQLException e) {
            out.println("ERROR: Unable to mark notification as seen.");
            e.printStackTrace(out);
            out.flush();
        }
    }

    private static class Notification {
        String id;
        String subject;
        String details;
        boolean containsTest;
        boolean containsCategory;

        Notification(String id, String subject, String details, boolean containsTest, boolean containsCategory) {
            this.id = id;
            this.subject = subject;
            this.details = details;
            this.containsTest = containsTest;
            this.containsCategory = containsCategory;
        }
    }
}
