package recommendation.client;

import java.io.IOException;

public interface EmployeeCommand {
    void execute() throws IOException, InvalidInputException;
}
