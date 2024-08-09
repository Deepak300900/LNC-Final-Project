package recommendation.server.factory;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.sql.Connection;

import recommendation.server.handlers.AdminHandler;
import recommendation.server.handlers.ChefHandler;
import recommendation.server.handlers.EmployeeHandler;
import recommendation.server.handlers.RoleHandler;
import recommendation.server.models.UserRole;

public class RoleHandlerFactory {
    public static RoleHandler getHandler(UserRole role, Connection connection, BufferedReader in, PrintWriter out, String email) {
        switch (role) {
            case ADMIN:
                return new AdminHandler(connection, in, out);
            case CHEF:
                return new ChefHandler(connection, in, out);
            case EMPLOYEE:
                return new EmployeeHandler(connection, in, out, email);
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
}
