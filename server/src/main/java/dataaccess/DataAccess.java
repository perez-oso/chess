package dataaccess;
import dataaccess.DataAccessException;
import model.*;

import javax.xml.crypto.Data;

public interface DataAccess {
    UserData getUser(String username) throws DataAccessException;

    void createUser(UserData userData) throws DataAccessException;

    void createAuth(AuthData authData) throws DataAccessException;
}
