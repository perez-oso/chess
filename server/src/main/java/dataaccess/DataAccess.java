package dataaccess;
import model.*;

import java.util.Collection;

public interface DataAccess {
    UserData getUser(String username) throws DataAccessException ;

    void createUser(UserData userData) throws DataAccessException ;

    void createAuth(AuthData authData) throws DataAccessException ;

    void deleteAuth(String authToken) throws DataAccessException ;

    void clear() throws DataAccessException ;

    AuthData getAuth(String authToken) throws DataAccessException ;

    Collection<GameData> getGamesList() throws DataAccessException ;

    GameData getGame(int gameID) throws DataAccessException ;

    void setBlackPlayer(int gameID, String username) throws DataAccessException ;

    void setWhitePlayer(int gameID, String username) throws DataAccessException ;

    int createGame(String gameName) throws DataAccessException ;
}
