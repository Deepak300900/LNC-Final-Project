package recommendation.client;

import java.io.*;
import java.net.*;

import java.sql.SQLException;

public class Client {
    private static final String SERVER_HOSTNAME = "localhost"; 
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args){
        try (
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
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        }
    }
}
