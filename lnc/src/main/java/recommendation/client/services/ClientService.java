package recommendation.client.services;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

import recommendation.client.factories.RoleServiceFactory;

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
        if (authenticateUser()) {
            handleServerResponse(in.readLine());
        } else {
            System.out.println("Authentication failed or invalid response from server.");
        }
    }

    private boolean authenticateUser() throws IOException {
        sendEmail();
        return promptPasswordIfRequested();
    }

    private void sendEmail() throws IOException {
        System.out.print("Enter email: ");
        String email = userInput.readLine();
        out.println("EMAIL:" + email);
    }

    private boolean promptPasswordIfRequested() throws IOException {
        String serverResponse = in.readLine();
        if ("PASSWORD:".equals(serverResponse)) {
            sendPassword();
            return true;
        } else {
            System.out.println("Incorrect Email");
            return false;
        }
    }

    private void sendPassword() throws IOException {
        System.out.print("Enter password: ");
        String password = userInput.readLine();
        out.println(password);
    }

    private void handleServerResponse(String serverResponse) throws IOException, SQLException {
        System.out.println("=> " + serverResponse);
        if (serverResponse.startsWith("ROLE:")) {
            handleRole(serverResponse.substring(5).toUpperCase());
        }
    }

    private void handleRole(String role) throws IOException, SQLException {
        RoleService roleService = RoleServiceFactory.getRoleService(role, userInput, in, out);
        if (roleService != null) {
            roleService.handleCommands();
        } else {
            System.out.println("Unknown role: " + role);
        }
    }
}
