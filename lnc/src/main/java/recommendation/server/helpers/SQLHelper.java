package recommendation.server.helpers;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLHelper {

    public static void executeQuery(Connection connection, String sql, PreparedStatementSetter setter, ResultSetHandler handler, PrintWriter out) {
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            if (setter != null) {
                setter.setValues(pstmt);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                handler.handle(rs);
            }
        } catch (SQLException e) {
            out.println("Error executing query: " + e.getMessage());
            out.flush();
            System.err.println("Error executing query: " + e.getMessage());
        }
    }

    public static void executeUpdate(Connection connection, String sql, PreparedStatementSetter setter, UpdateHandler handler, PrintWriter out) {
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            if (setter != null) {
                setter.setValues(pstmt);
            }
            int rowsAffected = pstmt.executeUpdate();
            handler.handle(rowsAffected);
        } catch (SQLException e) {
            out.println("Error executing update: " + e.getMessage());
            out.flush();
            System.err.println("Error executing update: " + e.getMessage());
        }
    }

    @FunctionalInterface
    public interface PreparedStatementSetter {
        void setValues(PreparedStatement ps) throws SQLException;
    }

    @FunctionalInterface
    public interface ResultSetHandler {
        void handle(ResultSet rs) throws SQLException;
    }

    @FunctionalInterface
    public interface UpdateHandler {
        void handle(int rowsAffected) throws SQLException;
    }
}
