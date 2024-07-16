package recommendation.client;

import java.io.IOException;
import java.sql.SQLException;

public interface RoleService {
    void handleCommands() throws IOException, SQLException;
}
