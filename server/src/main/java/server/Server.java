package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import service.*;

public class Server {

    private final Javalin javalin;
    private final UserService userService;

    public Server(UserService userService) {
        this.userService = userService;
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
        RegisterResult result = userService.registerUser(registerRequest);
        ctx.status(result.code());
//        ctx.result(result.toString());
        System.out.println(registerRequest.toString());
    }
}