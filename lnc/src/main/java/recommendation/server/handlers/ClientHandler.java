package recommendation.server.handlers;

import java.io.*;
import java.net.*;

public class ClientHandler extends Thread {
    private final Socket clientSocket;
    private final ClientRequestHandler requestHandler;
    private final DatabaseConnectionHandler dbHandler;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        this.dbHandler = DatabaseConnectionHandler.getInstance();
        this.requestHandler = new ClientRequestHandler(dbHandler.getConnection());
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
        } 
    }

    private void handleError(String message, Exception e) {
        System.err.println(message + e.getMessage());
    }
}
