package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.AlreadyTakenException;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {
    DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public RegisterResult registerUser(RegisterRequest registerRequest) {
        try {
            UserData userData = dataAccess.getUser(registerRequest.username());

            if (userData != null) {
                return new RegisterResult(null, null, 403);
            } else {
                String authToken = generateToken();
                dataAccess.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
                dataAccess.createAuth(new AuthData(authToken, registerRequest.username()));
                return new RegisterResult(registerRequest.username(), authToken, 200);
            }
        } catch (DataAccessException e) {
            return new RegisterResult(null, null, 500);
        }
    }
}