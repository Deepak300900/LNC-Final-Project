package recommendation.server;

import java.io.*;
import java.net.*;
import java.sql.*;

public class ClientHandler extends Thread {
    private final Socket clientSocket;
    private final Connection connection;
    private final ClientRequestHandler requestHandler;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        this.connection = DatabaseConnectionManager.getConnection();
        this.requestHandler = new ClientRequestHandler(connection);
    }

    @Override
    public void run() {
        try (
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            requestHandler.handleRequests(in, out);
        } catch (IOException e) {
            handleError("Error handling client: ", e);
        } finally {
            closeConnection();
        }
    }

    private void handleError(String message, Exception e) {
        System.err.println(message + e.getMessage());
    }

    private void closeConnection() {
        DatabaseConnectionManager.closeConnection(connection);
    }
}
