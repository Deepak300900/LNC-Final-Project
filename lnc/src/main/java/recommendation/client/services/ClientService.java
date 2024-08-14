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
        boolean isAuthenticated = false;
        while (true) {
            int option = displayOptionsAndGetSelection();
            if (option == 1) {
                isAuthenticated = authenticateUser();
                if (!isAuthenticated) {
                    System.out.println("Authentication failed. Please try again.");
                }
            } else if (option == 2) {
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        
            if (isAuthenticated) {
                handleServerResponse(in.readLine());
            }
            
        }
    }

    private int displayOptionsAndGetSelection() throws IOException {
        MenuDisplayService.showLoginMenu();
        try {
            String choice = userInput.readLine();
            return Integer.parseInt(choice);
        } catch (NumberFormatException e) {
            return -1;
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
        System.out.println("-------------------------------------------------------------\n");
        String response = in.readLine();
        if("Incorrect password".equals(response)){
                System.out.println("Wrong Password. Please try again.. ");
        }else{
        System.out.println("Hello " + response + "! You're successfully logged in. Let's get started!");
        }
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