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

    public UserData getUser(String username) throws DataAccessException {
        if (users.containsKey(username)) {
            return users.get(username);
        }

        return null;
    }

    public AuthData getAuth(String authToken) throws DataAccessException  {
        if (activeUsers.containsKey(authToken)) {
            return activeUsers.get(authToken);
        }

        return null;
    }

    public void createUser(UserData userData) throws DataAccessException  {
        users.put(userData.username(), userData);
    }

    public void createAuth(AuthData authData) throws DataAccessException  {
        activeUsers.put(authData.authToken(), authData);
    }

    public void clear() throws DataAccessException {
        users.clear();
        activeUsers.clear();
        activeGames.clear();
        this.gameID = 0;
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        activeUsers.remove(authToken);
    }

    public Collection<GameData> getGamesList() throws DataAccessException  {
        return new ArrayList<>(activeGames.values());
    }

    public int createGame(String gameName) throws DataAccessException {
        int gameID = ++this.gameID;
        GameData gameData = new GameData(gameID, null, null, gameName, new ChessGame());
        this.activeGames.put(gameID, gameData);
        return gameID;
    }

    public GameData getGame(int gameID) throws DataAccessException  {
        return this.activeGames.get(gameID);
    }

    public void setBlackPlayer(int gameID, String blackUsername) throws DataAccessException {
        GameData oldGameData = this.activeGames.get(gameID);
        GameData newGameData =
                new GameData(oldGameData.gameID(), oldGameData.whiteUsername(), blackUsername, oldGameData.gameName(), oldGameData.game());
        this.activeGames.put(gameID, newGameData);
    }

    public void setWhitePlayer(int gameID, String whiteUsername) throws DataAccessException {
        GameData oldGameData = this.activeGames.get(gameID);
        GameData newGameData =
                new GameData(oldGameData.gameID(), whiteUsername, oldGameData.blackUsername(), oldGameData.gameName(), oldGameData.game());
        this.activeGames.put(gameID, newGameData);
    }
}
