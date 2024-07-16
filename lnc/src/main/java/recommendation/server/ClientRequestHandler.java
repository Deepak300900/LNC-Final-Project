package recommendation.server;

import java.io.*;
import java.sql.*;

public class ClientRequestHandler {
    private final Connection connection;

    public ClientRequestHandler(Connection connection) {
        this.connection = connection;
    }

    public void handleRequests(BufferedReader in, PrintWriter out) throws IOException {
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Received from client: " + inputLine);
            if (inputLine.startsWith("EMAIL:")) {
                handleEmailRequest(inputLine, in, out);
            }
            if (inputLine.equalsIgnoreCase("exit") || inputLine.equalsIgnoreCase("shutdown")) {
                Server.stop();
                break;
            }
        }
    }

    private void handleEmailRequest(String inputLine, BufferedReader in, PrintWriter out) throws IOException {
        String email = extractEmail(inputLine);
        try {
            if (emailExists(email)) {
                out.println("PASSWORD:");
                String password = in.readLine();
                validatePassword(email, password, out, in);
            } else {
                out.println("Email not found");
            }
        } catch (SQLException e) {
            handleError("Database error: ", e, out);
        }
    }

    private String extractEmail(String inputLine) {
        return inputLine.substring(6).trim();
    }

    private boolean emailExists(String email) throws SQLException {
        String sql = "SELECT password, username, userRole FROM userinfo WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        }
    }

    private void validatePassword(String email, String password, PrintWriter out, BufferedReader in) throws SQLException {
        String sql = "SELECT password, username, userRole FROM userinfo WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                String userRole = rs.getString("userRole");

                if (password.equals(storedPassword)) {
                    handleSuccessfulLogin(email, userRole, out, in);
                } else {
                    out.println("Incorrect password");
                }
            }
        }
    }

    private void handleSuccessfulLogin(String email, String userRole, PrintWriter out, BufferedReader in) throws SQLException {
        out.println("ROLE:" + userRole.toUpperCase());
        UserActivity userActivity = new UserActivity(email, connection);
        userActivity.addLogInInfo();
        UserRole role = UserRole.valueOf(userRole.toUpperCase());
        RoleHandler handler = RoleHandlerFactory.getHandler(role, connection, in, out, email);
        handler.process();
        userActivity.addLogOutInfo();
    }

    private void handleError(String message, SQLException e, PrintWriter out) {
        System.err.println(message + e.getMessage());
        out.println("Database error");
    }
}
