package dataaccess;

import com.google.gson.Gson;
import model.*;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;
import javax.xml.crypto.Data;
import java.sql.*;



public class MySQLDataAccess implements DataAccess {
    public MySQLDataAccess() {
        try {
            configureDatabase();
        } catch (DataAccessException ex) {
            System.out.println("unable to congifure database: " + ex.getMessage());
        }
    }

    private final String[] createStatements = {
        """
        CREATE TABLE IF NOT EXISTS users (
            username VARCHAR(255) NOT NULL,
            userData LONGTEXT NOT NULL,
          )""",

        """
        CREATE TABLE IF NOT EXISTS activeUsers (
            authToken VARCHAR(255) NOT NULL,
            authData LONGTEXT NOT NULL,
        )""",

        """
        CREATE TABLE IF NOT EXISTS activeGames (
            gameID INT NOT NULL AUTO_INCREMENT,
            userData LONGTEXT NOT NULL,
        )"""


    };

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException ex) {
            throw new DataAccessException("unable to update database: " + statement + " " + ex.getMessage());
        }
    }

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();

        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("unable to configure database: %s", ex.getMessage()));
        }
    }
}
