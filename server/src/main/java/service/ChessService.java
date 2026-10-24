package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;

import java.util.Objects;
import java.util.UUID;

public class ChessService {
    DataAccess dataAccess;

    public ChessService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public RegisterResult registerUser(RegisterRequest registerRequest) throws AlreadyTakenException, DataAccessException {
            UserData userData;

            try {
                userData = dataAccess.getUser(registerRequest.username());
            } catch (Exception ex) {
                throw new DataAccessException("Error: " + ex.getMessage());
            }

            if (userData != null) {
                throw new AlreadyTakenException("Error: username already taken.");
            }

            AuthData authData;

            try {
                authData = new AuthData(generateToken(), registerRequest.username());
                dataAccess.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
                dataAccess.createAuth(authData);
            } catch (Exception ex) {
                throw new DataAccessException("Error: " + ex.getMessage());
            }

            return new RegisterResult(registerRequest.username(), authData.authToken());
    }

    public void clear() throws DataAccessException {
        try {
            dataAccess.clear();
        } catch (Exception ex) {
            throw new DataAccessException("Error: " + ex.getMessage());
        }
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException, UserNotFoundException {
        UserData userData;

        try {
            userData = dataAccess.getUser(loginRequest.username());
        } catch (Exception ex) {
            throw new DataAccessException("Error: " + ex.getMessage());
        }

        if (userData == null || !Objects.equals(userData.password(), loginRequest.password())) {
            throw new UserNotFoundException("Error: bad request");
        }

       AuthData authData;

        try {
            authData = new AuthData(generateToken(), userData.username());
            dataAccess.createAuth(authData);
        } catch (Exception ex) {
            throw new DataAccessException("Error: " + ex.getMessage());
        }

        return new LoginResult(userData.username(), authData.authToken());
    }

    public void logout(LogoutRequest logoutRequest) throws DataAccessException, UnauthorizedException {
        boolean success;

        try {
            success = this.dataAccess.deleteAuth(logoutRequest.authToken());
        } catch (Exception ex) {
            throw new DataAccessException("Error: " + ex.getMessage());
        }

        if (!success) {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }
}