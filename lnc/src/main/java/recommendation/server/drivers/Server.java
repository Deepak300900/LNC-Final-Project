package recommendation.server.drivers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import recommendation.server.handlers.ClientHandler;

public class Server {
    private static final int PORT = 1234;
    private static boolean running = true;
      
    public static void main(String[] args) {
        new Server().start();
    }

    public void start() {
        try (ServerSocket serverSocket = createServerSocket()) {
            System.out.println("Server is running on port " + PORT);
            while (isRunning()) {
                acceptClientConnection(serverSocket);
            }
        } catch (IOException e) {
            handleServerException(e);
        }
    }

    private ServerSocket createServerSocket() throws IOException {
        return new ServerSocket(PORT);
    }

    private boolean isRunning() {
        return running;
    }

    private void acceptClientConnection(ServerSocket serverSocket) throws IOException {
        Socket clientSocket = serverSocket.accept();
        handleClient(clientSocket);
    }

    private void handleClient(Socket clientSocket) {
        System.out.println("New client connected: " + clientSocket);
        new ClientHandler(clientSocket).start();
    }

    private void handleServerException(IOException e) {
        System.err.println("Server socket error: " + e.getMessage());
    }

    public static void stop() {
        running = false;
        System.out.println("Stopping server...");
    }
}
