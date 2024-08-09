package recommendation.server.commands;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import recommendation.server.handlers.Communicationhandler;
import recommendation.server.interfaces.EmployeeCommand;

public class UpdateProfileCommand implements EmployeeCommand {
    private final Connection connection;
    private final PrintWriter out;
    private final Communicationhandler inputReader;
    private final String currentUser;

    public UpdateProfileCommand(Connection connection, PrintWriter out, Communicationhandler inputReader, String currentUser) {
        this.connection = connection;
        this.out = out;
        this.inputReader = inputReader;
        this.currentUser = currentUser;
        System.out.println("Update Profile Command Constructor");
    }

    @Override
    public void execute()  {
        try {
            String newName = inputReader.readString("New name:");
            String newPassword = inputReader.readString("New password:");
            String newCategory = inputReader.readString("New category:");
            String newTest = inputReader.readString("New test:");

            String query = "UPDATE UserInfo SET username = ?, password = ?, category = ?, test = ? WHERE email = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, newName);
                statement.setString(2, newPassword);
                statement.setString(3, newCategory);
                statement.setString(4, newTest);
                statement.setString(5, currentUser);

                int rowsUpdated = statement.executeUpdate();
                System.out.println("come here");
                if (rowsUpdated > 0) {
                    out.println("SUCCESS");
                } else {
                    out.println("FAILURE: No user found with the given email.");
                }
            }
        } catch (SQLException|IOException e) {
            out.println("FAILURE: " + e.getMessage());
        } finally {
            out.flush();
        }
    }
}
