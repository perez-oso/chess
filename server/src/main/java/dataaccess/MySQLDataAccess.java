package dataaccess;

import com.google.gson.Gson;
import kotlin.TuplesKt;
import model.*;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.xml.crypto.Data;
import java.sql.*;
import java.util.Collection;


public class MySQLDataAccess {//implements DataAccess {
    public MySQLDataAccess() {
        try {
            configureDatabase();
        } catch (DataAccessException ex) {
            System.out.println("unable to configure database: " + ex.getMessage());
        }
}

    public UserData getUser(String username) {
      try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT userData FROM users WHERE username=?";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                try (ResultSet rs = preparedStatement.executeQuery()){
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception ex) {
//          System.out.println("uh oh 4");
        }

        return null;
    }

    public UserData readUser(ResultSet rs) {
        UserData userData = null;

        try {
            var json = rs.getString("userData");
            userData = new Gson().fromJson(json, UserData.class);
        } catch (SQLException ex) {

        }

        return userData;
    }

    public int createGame(String gameName) {
        return 0;
    }

    public void createUser(UserData userData) {
        var statement = "INSERT INTO users (username, userData) VALUES(?, ?)";
        executeUpdate(statement, userData.username(), userData);
    }

    public void createAuth(AuthData authData) {
        var statement = "INSERT INTO auths (authToken, authData) VALUES(?, ?)";
        executeUpdate(statement, authData.authToken(), authData);
    }

    public void deleteAuth(String authToken) {
        var statement = "DELETE FROM auths WHERE authToken=?";
        executeUpdate(statement, authToken);
    }

    public void clear() {
        executeUpdate("TRUNCATE users");
        executeUpdate("TRUNCATE auths");
        executeUpdate("TRUNCATE games");
    }


    public Collection<GameData> getGamesList() {
        return java.util.List.of();
    }

    public UserData getUserByAuth(String authToken) {
        return null;
    }

    public GameData getGame(int gameID) {
        return null;
    }

    public void setBlackPlayer(int gameID, String username) {}

    public void setWhitePlayer(int gameID, String username) {}

    private int executeUpdate(String statement, Object... params) {
        try {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param instanceof UserData p) ps.setString(i + 1, new Gson().toJson(p));
                    else if (param instanceof AuthData p) ps.setString(i + 1, new Gson().toJson(p));
                    else if (param instanceof GameData p) ps.setString(i + 1, new Gson().toJson(p));
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
//            throw new ResponseException(ResponseException.Code.ServerError, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        } } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return 0;
    }

    private final String[] createStatements = {
        """
        CREATE TABLE IF NOT EXISTS users (
            username VARCHAR(255) NOT NULL,
            userData LONGTEXT NOT NULL
        );""",

        """
        CREATE TABLE IF NOT EXISTS auths (
            authToken VARCHAR(255) NOT NULL,
            authData LONGTEXT NOT NULL
        );""",

        """
        CREATE TABLE IF NOT EXISTS games (
            gameID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
            gameData LONGTEXT NOT NULL
        );"""
    };

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
