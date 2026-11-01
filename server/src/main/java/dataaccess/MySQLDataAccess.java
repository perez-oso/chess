package dataaccess;

import com.google.gson.Gson;
import model.*;
import java.sql.*;



public class MySQLDataAccess implements DataAccess {
    public MySQLDataAccess() throws SQLException {
        configureDatabase();
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

    private void configureDatabase() {
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
