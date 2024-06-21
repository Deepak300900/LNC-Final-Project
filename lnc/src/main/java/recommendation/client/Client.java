package recommendation.client;

import java.io.*;
import java.net.*;
//import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.SQLException;

public class Client {
    private static final String SERVER_HOSTNAME = "172.16.0.130"; // Update to server's IP address
    private static final int SERVER_PORT = 1234;
   // private static final String DB_URL = "jdbc:mysql://localhost:3306/DeepakDatabase";
   // private static final String DB_USERNAME = "root";
   // private static final String DB_PASSWORD = "Deepak@300900";

    public static void main(String[] args) {
        try (
            //Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            Socket socket = new Socket(SERVER_HOSTNAME, SERVER_PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Connected to server on " + SERVER_HOSTNAME + ":" + SERVER_PORT);
            ClientService clientService = new ClientService(socket, out, in, userInput);
            clientService.start();
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + SERVER_HOSTNAME);
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        }
    }
}
