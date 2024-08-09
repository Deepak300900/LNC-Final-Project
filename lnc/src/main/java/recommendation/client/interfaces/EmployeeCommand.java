package recommendation.client.interfaces;

import java.io.IOException;

import recommendation.client.exceptions.InvalidInputException;

public interface EmployeeCommand {
    void execute() throws IOException, InvalidInputException;
}
