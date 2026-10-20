package server;

import com.google.gson.Gson;
import dataaccess.AlreadyTakenException;
import dataaccess.DataAccessException;
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

            RegisterResult result = chessService.registerUser(registerRequest);
            ctx.result(new Gson().toJson(result));
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
}