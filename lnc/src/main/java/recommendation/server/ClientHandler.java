package recommendation.server;

import java.io.*;
import java.net.*;
import java.sql.*;

class ClientHandler extends Thread {
    private final Socket clientSocket;
    private final Connection connection;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        this.connection = initializeDatabaseConnection();
    }

    private Connection initializeDatabaseConnection() {
        try {
            return DriverManager.getConnection(Server.DB_URL, Server.DB_USER, Server.DB_PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void run() {
        try (
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            handleClientRequests(in, out);
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    private void handleClientRequests(BufferedReader in, PrintWriter out) throws IOException {
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
        System.out.println("Client disconnected: " + clientSocket);
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
            System.err.println("Database error: " + e.getMessage());
            out.println("Database error");
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
        RoleHandler handler = RoleHandlerFactory.getHandler(role, connection, in, out);
        handler.process();
        userActivity.addLogOutInfo();
    }

    private void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}
