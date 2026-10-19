package server;

import chess.ChessGame;
import io.javalin.*;
import io.javalin.http.Context;
import com.google.gson.*;

public class Server {
    private final Javalin javalin;
    private final ChessService service;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
            .delete("/db", this::clear)
        ;

        // Register your endpoints and exception handlers here.
        void clear {

        }

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

//    private void exceptionHandler(Exception ex, Context ctx) {
//
//    }
    private void clear(Context ctx) throws Exception {
        service.deleteGame();
        ctx.status(204);
    }
}

