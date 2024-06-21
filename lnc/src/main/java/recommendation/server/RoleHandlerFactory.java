package recommendation.server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.sql.Connection;

public class RoleHandlerFactory {
    public static RoleHandler getHandler(UserRole role, Connection connection, BufferedReader in, PrintWriter out) {
        switch (role) {
            case ADMIN:
                return new AdminHandler(connection, in, out);
            case CHEF:
                return new ChefHandler(connection, in, out);
            case EMPLOYEE:
                return new EmployeeHandler(connection, in, out);
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
}
