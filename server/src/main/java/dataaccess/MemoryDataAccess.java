package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;

public class MemoryDataAccess implements DataAccess {
    final private HashMap<String, UserData> users = new HashMap<>();
    final private HashMap<String, AuthData> activeUsers = new HashMap<>();
    final private HashMap<String, GameData> activeGames = new HashMap<>();

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
    }

    public boolean deleteAuth(String authToken) {
        if (activeUsers.containsKey(authToken)) {
            activeUsers.remove(authToken);
            return true;
        }

        return false;
    }
}
