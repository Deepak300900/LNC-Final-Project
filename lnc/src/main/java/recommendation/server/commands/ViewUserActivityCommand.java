package recommendation.server.commands;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import recommendation.server.interfaces.AdminCommand;

public class ViewUserActivityCommand implements AdminCommand {
    private final Connection connection;
    private final PrintWriter out;

    public ViewUserActivityCommand(Connection connection, PrintWriter out) {
        this.connection = connection;
        this.out = out;
    }

    @Override
    public void execute() {
        String query = "SELECT email, logInTime, logOutTime FROM UserActivity";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            printUserActivityTable(rs);
        } catch (SQLException e) {
            handleAdminError("Failed to retrieve user activity: " + e.getMessage());
        }
    }

    private void printUserActivityTable(ResultSet rs) throws SQLException {
        out.println("+--------------------------------+---------------------+---------------------+");
        out.println("| Email                          | Log In Time         | Log Out Time        |");
        out.println("+--------------------------------+---------------------+---------------------+");

        while (rs.next()) {
            String email = rs.getString("email");
            String logInTime = rs.getString("logInTime");
            String logOutTime = rs.getString("logOutTime");
            out.printf("| %-30s | %-19s | %-19s |%n", email, logInTime, logOutTime);
        }

        out.println("+--------------------------------+---------------------+---------------------+");
        out.println("End of User Activity");
        out.flush();
    }

    private void handleAdminError(String errorMessage) {
        System.err.println(errorMessage);
        out.println(errorMessage);
        out.flush();
    }
}
