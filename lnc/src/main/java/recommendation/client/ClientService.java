package recommendation.client;

import java.io.*;
import java.net.Socket;
//import java.sql.Connection;
import java.sql.SQLException;

public class ClientService {
    private final PrintWriter out;
    private final BufferedReader in;
    private final BufferedReader userInput;

    public ClientService(Socket socket, PrintWriter out, BufferedReader in, BufferedReader userInput) {
        this.out = out;
        this.in = in;
        this.userInput = userInput;
    }

    public void start() throws IOException, SQLException {
        authenticateUser();

        String serverResponse = in.readLine();
        System.out.println("=> " + serverResponse);

        if (serverResponse.startsWith("ROLE:")) {
            String role = serverResponse.substring(5).toUpperCase();
            handleRole(role);
        } else {
            System.out.println("Authentication failed or invalid response from server.");
        }
    }

    private void authenticateUser() throws IOException {
        System.out.print("Enter email: ");
        String email = userInput.readLine();
        out.println("EMAIL:" + email);

        String serverResponse = in.readLine();

        if ("PASSWORD:".equals(serverResponse)) {
            System.out.print("Enter password: ");
            String password = userInput.readLine();
            out.println(password);
        } else {
            System.out.println("Incorrect Email");
        }
    }

    private void handleRole(String role) throws IOException, SQLException {
        switch (role) {
            case "ADMIN":
                new AdminService(userInput, in, out).handleCommands();
                break;
            case "CHEF":
                new ChefService(userInput, in, out).handleCommands();
                break;
            case "EMPLOYEE":
                new EmployeeService(userInput, in, out).handleCommands();
                break;
            default:
                System.out.println("Unknown role: " + role);
        }
    }
}
