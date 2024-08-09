package recommendation.client.factories;

import java.io.BufferedReader;
import java.io.PrintWriter;

import recommendation.client.services.AdminService;
import recommendation.client.services.ChefService;
import recommendation.client.services.EmployeeService;
import recommendation.client.services.RoleService;

public class RoleServiceFactory {
    public static RoleService getRoleService(String role, BufferedReader userInput, BufferedReader in, PrintWriter out) {
        switch (role) {
            case "ADMIN":
                return new AdminService(userInput, in, out);
            case "CHEF":
                return new ChefService(userInput, in, out);
            case "EMPLOYEE":
                return new EmployeeService(userInput, in, out);
            default:
                return null;
        }
    }
}
