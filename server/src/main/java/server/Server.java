package server;

import chess.ChessGame;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.*;
import io.javalin.*;
import io.javalin.http.Context;
import service.*;

import java.util.Map;

public class Server {

    private final Javalin javalin;
    private final ChessService chessService;

    public Server(ChessService chessService) {
        this.chessService = chessService;
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", this::registerUser)
                .post("/session", this::login)
                .exception(Exception.class, this::exceptionHandler)
                .delete("/db", this::clear)
                .delete("/session", this::logout)
                .get("/game", this::listGames)
                .post("/game", this::createGame)
        ;

        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void exceptionHandler(Exception ex, Context ctx) {

    }

    private void clear(Context ctx) {
        try {
            chessService.clear();
            ctx.result("").status(200);
        } catch (DataAccessException ex) {ctx.result(ex.toJson()).status(500);}
    }

    private void registerUser(Context ctx) {
        try {
            RegisterRequest registerRequest;

            try {
                registerRequest = new Gson().fromJson(ctx.body(), RegisterRequest.class);
            } catch (Exception ex) {
                ctx.result(new Gson().toJson(Map.of("message:", "Error: bad request")));
                ctx.status(400);
                return;
            }

            RegisterResult registerResult = chessService.registerUser(registerRequest);
            ctx.result(new Gson().toJson(registerResult));
            ctx.status(200);
        } catch (BadRequestException ex) {ctx.result(ex.toJson()).status(400);
        } catch (AlreadyTakenException ex) {ctx.result(ex.toJson()).status(403);
        } catch (DataAccessException ex) {ctx.result(ex.toJson()).status(500);}
//        System.out.println(registerRequest.toString());
    }

    private void login(Context ctx) {
        try {
            LoginRequest loginRequest;

            try {
                loginRequest = new Gson().fromJson(ctx.body(), LoginRequest.class);
            } catch (Exception ex) {
                ctx.result(new Gson().toJson(Map.of("message:", "Error: bad request")));
                ctx.status(400);
                return;
            }

            LoginResult loginResult = chessService.login(loginRequest);
            ctx.result(new Gson().toJson(loginResult)).status(200);
        } catch (BadRequestException ex) {ctx.result(ex.toJson()).status(400);
        } catch (DataAccessException ex){ctx.result(ex.toJson()).status(500);
        } catch (UnauthorizedException ex) {ctx.result(ex.toJson()).status(401);}
    }

    private void logout(Context ctx) {
        try {
            LogoutRequest logoutRequest;
//            System.out.println("DEBUG: " + ctx.header("authorization"));

            try {
                logoutRequest = new LogoutRequest(ctx.header("authorization"));
            } catch (Exception ex) {
                ctx.result(new Gson().toJson(Map.of("message:", "Error: bad request")));
                ctx.status(400);
                return;
            }

            chessService.logout(logoutRequest);
            ctx.result("").status(200);
        } catch (DataAccessException ex) {ctx.result(ex.toJson()).status(500);
        } catch (UnauthorizedException ex) {ctx.result(ex.toJson()).status(401);}
    }

    private void listGames(Context ctx) {
        ExclusionStrategy strategy = new ExclusionStrategy() {
            @Override
            public boolean shouldSkipClass(Class<?> klass) {
                return false;
            }

            @Override
            public boolean shouldSkipField(FieldAttributes field) {
                return field.getName().equals("game");
            }
        };

        try {
            ListGamesRequest listGamesRequest;

            try {
                listGamesRequest = new ListGamesRequest(ctx.header("authorization"));
            } catch (Exception ex) {
                ctx.result(new Gson().toJson(Map.of("message:", "Error: bad request")));
                ctx.status(400);
                return;
            }

            ListGamesResult listGamesResult = chessService.listGames(listGamesRequest);
            Gson gson = new GsonBuilder()
                .addSerializationExclusionStrategy(strategy)
                .create();
            ctx.result(gson.toJson(listGamesResult)).status(200);
        } catch (DataAccessException ex) {ctx.result(ex.toJson()).status(500);
        } catch (UnauthorizedException ex) {ctx.result(ex.toJson()).status(401);}
    }

    private void createGame(Context ctx) {
        try {
            CreateGameRequest createGameRequest;
            String authToken;

            try {
                authToken = ctx.header("authorization");
                createGameRequest = new Gson().fromJson(ctx.body(), CreateGameRequest.class);
            } catch (Exception ex) {
                ctx.result(new Gson().toJson(Map.of("message:", "Error: bad request")));
                ctx.status(400);
                return;
            }

            ctx.result(new Gson().toJson(chessService.createGame(authToken, createGameRequest)));
            ctx.status(200);
        } catch (DataAccessException ex) {ctx.result(ex.toJson()).status(500);
        } catch (UnauthorizedException ex) {ctx.result(ex.toJson()).status(401);
        } catch (BadRequestException ex) {ctx.result(ex.toJson()).status(400);}
    }
}