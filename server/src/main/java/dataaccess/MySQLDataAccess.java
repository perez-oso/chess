package dataaccess;

import com.google.gson.Gson;
import model.*;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;
import javax.xml.crypto.Data;
import java.sql.*;
import java.util.Collection;


public class MySQLDataAccess {//implements DataAccess {
    public MySQLDataAccess() {
        try {
            configureDatabase();
        } catch (DataAccessException ex) {
            System.out.println("unable to congifure database: " + ex.getMessage());
        }
}

//    public UserData getUser(String username) {
//      try (Connection conn = DatabaseManager.getConnection()) {
//            var statement = "SELECT userData FROM users WHERE username=?";
//            try (PreparedStatement ps = conn.prepareStatement(statement)) {
//                ps.setString(1, username);
//                try (ResultSet rs = ps.execut....... {
//                    if (rs.next()) {
//                        return readUser(rs);
//                    }
//                }
//            }
//        } catch (Exception ex) {
//            throw new DataAccessException("unable to read data: " + ex.getMessage());
//        }
//
//        return null;
//    }

//    public UserData readUser(ResultSet rs) {
//        var json = rs.getString("userData");
//        UserData userData = new Gson().fromJson(json, UserData.class);
//        return userData;
//    }

    public int createGame(String gameName) {
        return 0;
    }

    public void createUser(UserData userData) {}

    public void createAuth(AuthData authData) {}

    public boolean deleteAuth(String authToken) {
        return false;
    }

    public void clear() {}

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

    public UserData getUser(String username) {
        return null;
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
//
//    private int executeUpdate(String statement, Object... params) throws DataAccessException {
//        try (Connection conn = DatabaseManager.getConnection()) {
//            try (PreparedStatement ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
//                for (int i = 0; i < params.length; i++) {
//                    Object param = params[i];
//                    if (param instanceof String p) ps.setString(i + 1, p);
//                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
//                    else if (param == null) ps.setNull(i + 1, NULL);
//                }
//                ps.executeUpdate();
//
//                ResultSet rs = ps.getGeneratedKeys();
//                if (rs.next()) {
//                    return rs.getInt(1);
//                }
//
//                return 0;
//            }
//        } catch (SQLException ex) {
//            throw new DataAccessException("unable to update database: " + statement + " " + ex.getMessage());
//        }
//    }

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
