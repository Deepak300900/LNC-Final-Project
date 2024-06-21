package recommendation.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//import java.sql.Date;

public class UserActivity {
        private static final String INSERT_SQL = "INSERT INTO UserActivity (email, loginTime) VALUES (?, NOW())";
        private static final String UPDATE_SQL = "UPDATE UserActivity SET logOutTime = NOW() WHERE email = ? AND logOutTime IS NULL;";
        private String email;
        private Connection connection;

        UserActivity(String email, Connection connection) {
                this.email = email;
                this.connection = connection;
        }

        public boolean addLogInInfo() {
                try (PreparedStatement pstmt = connection.prepareStatement(INSERT_SQL)) {
                        pstmt.setString(1, this.email);
                        //pstmt.setString(2, "description");
                        int rowsAffected = pstmt.executeUpdate();
                        return rowsAffected > 0;
                } catch (SQLException e) {
                        System.err.println("Error adding menu item: " + e.getMessage());
                        return false;
                }
        }

        public boolean addLogOutInfo() {

                try (PreparedStatement pstmt = connection.prepareStatement(UPDATE_SQL)) {
                        pstmt.setString(1, this.email);
                        int rowsAffected = pstmt.executeUpdate();
                        return rowsAffected > 0;
                } catch (SQLException e) {
                        System.err.println("Error updating menu item: " + e.getMessage());
                        return false;
                }
        }
}
