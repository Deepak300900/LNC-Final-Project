package recommendation.client.drivers;

import java.io.*;
import java.net.*;
import java.sql.SQLException;

import recommendation.client.services.ClientService;

public class Client {
    private static final String SERVER_HOSTNAME = "localhost";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        new Client().start();
    }

    public void start() {
        try (Socket socket = createSocket();
             PrintWriter out = createPrintWriter(socket);
             BufferedReader in = createBufferedReader(socket);
             BufferedReader userInput = createUserInputReader()) {

            System.out.println("Connected to server on " + SERVER_HOSTNAME + ":" + SERVER_PORT);
            ClientService clientService = new ClientService(socket, out, in, userInput);
            clientService.start();
        } catch (UnknownHostException e) {
            handleUnknownHostException(e);
        } catch (IOException e) {
            handleIOException(e);
        } catch (SQLException e) {
            handleSQLException(e);
        } catch(Exception e){
                System.out.println("Server connection is not Succfull");
        }

    }
    
    private Socket createSocket() throws IOException {
        return new Socket(SERVER_HOSTNAME, SERVER_PORT);
    }

    private PrintWriter createPrintWriter(Socket socket) throws IOException {
        return new PrintWriter(socket.getOutputStream(), true);
    }

    private BufferedReader createBufferedReader(Socket socket) throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private BufferedReader createUserInputReader() throws IOException {
        return new BufferedReader(new InputStreamReader(System.in));
    }

    private void handleUnknownHostException(UnknownHostException e) {
        System.err.println("Unknown host: " + SERVER_HOSTNAME);
    }

    private void handleIOException(IOException e) {
        System.err.println("Error connecting to server: " + e.getMessage());
    }

    private void handleSQLException(SQLException e) {
        System.err.println("Database connection error: " + e.getMessage());
    }
}
