package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kotlin.TuplesKt;
import model.*;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

import javax.lang.model.type.PrimitiveType;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;


public class MySQLDataAccess {//implements DataAccess {
    public MySQLDataAccess() {
        try {
            configureDatabase();
        } catch (DataAccessException ex) {
            throw new RuntimeException("unable to configure database: " + ex.getMessage());
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
            throw new RuntimeException("unable to get user: " + ex.getMessage());
        }

        return null;
    }

    public UserData readUser(ResultSet rs) {
        UserData userData = null;

        try {
            var json = rs.getString("userData");
            userData = new Gson().fromJson(json, UserData.class);
        } catch (SQLException ex) {
            throw new RuntimeException("unable to read user: " + ex.getMessage());
        }

        return userData;
    }

    public AuthData getAuth(String authToken) {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authData FROM auths WHERE authToken=?";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                try (ResultSet rs = preparedStatement.executeQuery()){
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to get auth: " + ex.getMessage());
        }

        return null;
    }

    public AuthData readAuth(ResultSet rs) {
        AuthData authData = null;

        try {
            var json = rs.getString("authData");
            authData = new Gson().fromJson(json, AuthData.class);
        } catch (SQLException ex) {
            throw new RuntimeException("unable to read auth: " + ex.getMessage());
        }

        return authData;
    }

    public int createGame(String gameName) {
        var statement = "INSERT INTO games (gameData) VALUES(?)";
        GameData gameData = new GameData(0, null, null, gameName, null);
        int gameID = executeUpdate(statement, gameData);

        gameData = new GameData(gameID, null, null, gameName, new ChessGame());
        statement = "UPDATE games SET gameData = ? WHERE gameID = ?";
        executeUpdate(statement, gameData, gameID);

        return gameID;
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
        Collection<GameData> gamesList = new ArrayList<>(){};

        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameData FROM games";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                try (ResultSet rs = preparedStatement.executeQuery()){
                    while (rs.next()) {
                        gamesList.add(readGame(rs));
                    }
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(String.format("unable to get games list: %s", ex.getMessage()));
        }

        return gamesList;

    }

    public GameData getGame(int gameID) {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameData FROM games WHERE gameID=?";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setInt(1, gameID);
                try (ResultSet rs = preparedStatement.executeQuery()){
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(String.format("unable to get game: %s", ex.getMessage()));
        }

        return null;
    }

    public GameData readGame(ResultSet rs) {
        GameData gameData = null;

        try {
            var json = rs.getString("gameData");
            System.out.println("DEBUG: " + json);
            Gson gson = new GsonBuilder()
                    .enableComplexMapKeySerialization()
                    .create();
            gameData = gson.fromJson(json, GameData.class);
        } catch (SQLException ex) {
            throw new RuntimeException(String.format("unable to read game: %s", ex.getMessage()));
        }

        return gameData;
    }

    public void setBlackPlayer(int gameID, String username) {
        GameData oldGameData = getGame(gameID);
        GameData newGameData = new GameData(oldGameData.gameID(), oldGameData.whiteUsername(), username, oldGameData.gameName(), oldGameData.game());
        String statement = "UPDATE games SET gameData = ? WHERE gameID = ?";
        executeUpdate(statement, newGameData, gameID);
    }

    public void setWhitePlayer(int gameID, String username) {
        GameData oldGameData = getGame(gameID);
        GameData newGameData = new GameData(oldGameData.gameID(), username, oldGameData.blackUsername(), oldGameData.gameName(), oldGameData.game());
        String statement = "UPDATE games SET gameData = ? WHERE gameID = ?";
        executeUpdate(statement, newGameData, gameID);
    }

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
                        else if (param instanceof GameData p) {
                            Gson gson = new GsonBuilder()
                                    .enableComplexMapKeySerialization()
                                    .create();
                            ps.setString(i + 1, gson.toJson(p));
                        }
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
                throw new RuntimeException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
            }
        } catch (Exception ex) {
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
