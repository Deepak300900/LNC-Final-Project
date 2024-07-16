package recommendation.server;

import java.io.IOException;
import java.sql.SQLException;

public interface ChefCommand {
    void execute() throws IOException, SQLException;
}
