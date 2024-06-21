package recommendation.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 1234;
    private static boolean running = true;

    public static final String DB_URL = "jdbc:mysql://localhost:3306/deepakdatabase";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "Deepak@300900";

    public static void main(String[] args) {
        start();
    }

    private static void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);
            while (running) {
                handleClient(serverSocket.accept());
            }
        } catch (IOException e) {
            System.err.println("Server socket error: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) {
        System.out.println("New client connected: " + clientSocket);
        new ClientHandler(clientSocket).start();
    }

    public static void stop() {
        running = false;
        System.out.println("Stopping server...");
    }
}
