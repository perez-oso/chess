package server;

import com.google.gson.Gson;
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
            ctx.result();
            ctx.status(200);
        } catch (DataAccessException ex) {
            ctx.result(ex.toJson());
            ctx.status(500);
        }
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
        } catch (AlreadyTakenException ex) {
            ctx.result(ex.toJson());
            ctx.status(403);
        } catch (DataAccessException ex) {
            ctx.result(ex.toJson());
            ctx.status(500);
        }
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
            ctx.result(new Gson().toJson(loginResult));
            ctx.status(200);
        } catch (DataAccessException ex){
            ctx.result(ex.toJson());
            ctx.status(500);
        } catch (UserNotFoundException ex) {
            ctx.result(ex.toJson());
            ctx.status(400);
;        } catch (UnauthorizedException ex) {
            ctx.result(ex.toJson());
            ctx.status(401);
        }
    }
}