package dataaccess;
import model.*;

import java.util.Collection;

public interface DataAccess {
    UserData getUser(String username);

    void createUser(UserData userData);

    void createAuth(AuthData authData);

    boolean deleteAuth(String authToken);

    void clear();

    Collection<GameData> getGamesList();

    UserData getUserByAuth(String authToken);

    GameData getGame(int gameID);

    void setBlackPlayer(int gameID, String username);

    void setWhitePlayer(int gameID, String username);

    int createGame(String gameName);
}
