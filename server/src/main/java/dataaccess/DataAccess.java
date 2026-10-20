package dataaccess;
import dataaccess.DataAccessException;
import model.*;

import javax.xml.crypto.Data;

public interface DataAccess {
    UserData getUser(String username);

    void createUser(UserData userData);

    void createAuth(AuthData authData);

//    void loginUser(UserData userData);

    void clear();
}
