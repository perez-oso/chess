package service;

import model.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import java.util.UUID;

public class ChessService {
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public String registerUser(RegisterRequest registerRequest) {
        return "ass";
    }
}
