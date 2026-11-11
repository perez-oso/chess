package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class MemoryDataAccess implements DataAccess {
    private int gameID = 0;
    final private HashMap<String, UserData> users = new HashMap<>();
    final private HashMap<String, AuthData> activeUsers = new HashMap<>();
    final private HashMap<Integer, GameData> activeGames = new HashMap<>();

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
        activeUsers.put(authData.authToken(), authData);
    }

    public void clear() {
        users.clear();
        activeUsers.clear();
        activeGames.clear();
        this.gameID = 0;
    }

    public void deleteAuth(String authToken) {
        activeUsers.remove(authToken);
    }

    public Collection<GameData> getGamesList() {
        return new ArrayList<>(activeGames.values());
    }

    public int createGame(String gameName) {
        int gameID = ++this.gameID;
        GameData gameData = new GameData(gameID, null, null, gameName, new ChessGame());
        this.activeGames.put(gameID, gameData);
        return gameID;
    }

    public GameData getGame(int gameID) {
        return this.activeGames.get(gameID);
    }

    public void setBlackPlayer(int gameID, String blackUsername) {
        GameData oldGameData = this.activeGames.get(gameID);
        GameData newGameData =
                new GameData(oldGameData.gameID(), oldGameData.whiteUsername(), blackUsername, oldGameData.gameName(), oldGameData.game());
        this.activeGames.put(gameID, newGameData);
    }

    public void setWhitePlayer(int gameID, String whiteUsername) {
        GameData oldGameData = this.activeGames.get(gameID);
        GameData newGameData =
                new GameData(oldGameData.gameID(), whiteUsername, oldGameData.blackUsername(), oldGameData.gameName(), oldGameData.game());
        this.activeGames.put(gameID, newGameData);
    }
}
