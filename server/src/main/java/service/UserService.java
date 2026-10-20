package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.AlreadyTakenException;
import model.AuthData;
import model.UserData;

import javax.xml.crypto.Data;
import java.util.UUID;

public class UserService {
    DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public RegisterResult registerUser(RegisterRequest registerRequest) throws AlreadyTakenException, DataAccessException {
            UserData userData = dataAccess.getUser(registerRequest.username());

            if (userData != null) {
                throw new AlreadyTakenException("Error: username already taken.");
            }

            String authToken = generateToken();

            try {
                dataAccess.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
                dataAccess.createAuth(new AuthData(authToken, registerRequest.username()));
            } catch (Exception ex) {
                throw new DataAccessException("Error: " + ex.getMessage());
            }

            return new RegisterResult(registerRequest.username(), authToken);
    }

    public void clear() throws DataAccessException {
        try {
            dataAccess.clear();
        } catch (Exception ex) {
            throw new DataAccessException("Error: " + ex.getMessage());
        }
    }
}