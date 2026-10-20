package dataaccess;

import com.google.gson.Gson;

import java.util.Map;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }

    public String toJson() { return new Gson().toJson(Map.of("message", getMessage()));}
}
