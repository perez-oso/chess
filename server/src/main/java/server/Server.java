package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import service.*;

public class Server {

    private final Javalin javalin;
    private final ChessService chessService;

    public Server(ChessService chessService) {
        this.chessService = chessService;
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", this::registerUser)
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

    private void registerUser(Context ctx) {
        RegisterRequest registerRequest = new Gson().fromJson(ctx.body(), RegisterRequest.class);
        chessService.registerUser(registerRequest);
//        System.out.println(registerRequest.toString());

    }
}