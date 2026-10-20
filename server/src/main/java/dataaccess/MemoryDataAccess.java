package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.UserData;

import java.util.HashMap;
import chess.ChessGame;

public class MemoryDataAccess implements DataAccess {
    final private HashMap<String, UserData> users = new HashMap<>();
    final private HashMap<String, String> userAuthTokens = new HashMap<>();
    final private HashMap<String, ChessGame> activeGames = new HashMap<>();

    public UserData getUser(String username) {
        if (users.containsKey(username)) {
            return users.get(username);
        }

        return null;
    }

    public void createUser(UserData userData) {
        users.put(userData.username(), userData);
    }

    public void createAuth(AuthData authData) {
        userAuthTokens.put(authData.authToken(), authData.username());
    }

    public void clearDatabase() {
        users.clear();
        userAuthTokens.clear();
        activeGames.clear();
    }
}
