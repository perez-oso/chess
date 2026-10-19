package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;

public class MemoryDataAccess implements DataAccess {
    final private HashMap<String, UserData> users = new HashMap<>();
    final private HashMap<String, String> userAuthTokens = new HashMap<>();

    public UserData getUser(String username) throws DataAccessException {
        if (users.containsKey(username)) {
            return users.get(username);
        }

        return null;
    }

    public void createUser(UserData userData) throws DataAccessException {
        users.put(userData.username(), userData);
    }

    public void createAuth(AuthData authData) throws DataAccessException {
        userAuthTokens.put(authData.authToken(), authData.username());
    }
}
