package service;
import chess.ChessGame;
import dataaccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChessServiceTests {


    @Test
    void RegisterOneUser() {
        ChessService chessService = new ChessService(new MemoryDataAccess());
        RegisterRequest testRegisterRequest = new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com");
        RegisterResult registerResult;

        try {
            registerResult = chessService.registerUser(testRegisterRequest);
            assertEquals("Alice", registerResult.username());
            assertNotEquals(null, registerResult.authToken());
        } catch(Exception ex) {fail();}
    }

    @Test
    void RegisterDuplicateUser() {
        ChessService chessService = new ChessService(new MemoryDataAccess());
        RegisterRequest testRegisterRequest = new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com");
        try {
            chessService.registerUser(testRegisterRequest);
        } catch(Exception ex) {fail();}

        assertThrows(AlreadyTakenException.class, () -> {chessService.registerUser(testRegisterRequest);});
    }

    @Test
    void RegisterBadRequest() {
        ChessService chessService = new ChessService(new MemoryDataAccess());
        RegisterRequest testRegisterRequest = new RegisterRequest("Alice", null, "alicedoe@gmail.com");
        try {
            assertThrows(BadRequestException.class, () -> {chessService.registerUser(testRegisterRequest);});;
        } catch(Exception ex) {fail();}
    }

    @Test
    void loginRegisteredUser() {
        ChessService chessService = new ChessService(new MemoryDataAccess());
        LoginResult loginResult;

        try {
            chessService.registerUser(new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com"));
            loginResult = chessService.login(new LoginRequest("Alice", "pa55w0rd"));
            assertEquals("Alice", loginResult.username());
            assertNotEquals(null, loginResult.authToken());
        } catch(Exception ex) {fail();}
    }

    @Test
    void loginBadRequest() {
        ChessService chessService = new ChessService(new MemoryDataAccess());
      try {
            chessService.registerUser(new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com"));
            assertThrows(BadRequestException.class, () -> {chessService.login(new LoginRequest(null, "pa55w0rd"));});
            assertThrows(BadRequestException.class, () -> {chessService.login(new LoginRequest("Alice", null));});
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void loginUnauthorized() {
        ChessService chessService = new ChessService(new MemoryDataAccess());
        try {
            chessService.registerUser(new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com"));
            assertThrows(UnauthorizedException.class, () -> {chessService.login(new LoginRequest("Alice", "animalfries42069"));});
            assertThrows(UnauthorizedException.class, () -> {chessService.login(new LoginRequest("Bjork", "pa55w0rd"));});
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void logoutValidAuth() {
        ChessService chessService = new ChessService(new MemoryDataAccess());
        String authToken;

        try {
            authToken = chessService.registerUser(new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com")).authToken();
            chessService.logout(new LogoutRequest(authToken));
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void logoutInvalidAuth() {
        ChessService chessService = new ChessService(new MemoryDataAccess());
       try {
            chessService.registerUser(new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com"));
            assertThrows(UnauthorizedException.class, () -> {chessService.logout(new LogoutRequest("loremipsum"));});
        } catch (Exception ex) {
           fail();
        }
    }

    @Test
    void createGame() {
        ChessService chessService = new ChessService(new MemoryDataAccess());
        try {
            String authToken = chessService.registerUser(new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com")).authToken();
            CreateGameResult result = chessService.createGame(authToken, new CreateGameRequest("kasparov vs deep blue"));
            assertNotEquals(null, result);
            assertNotEquals(0, chessService.listGames(new ListGamesRequest(authToken)).games().size());
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void createGameInvalidAuth() {
        ChessService chessService = new ChessService(new MemoryDataAccess());
        try {
            chessService.registerUser(new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com"));
            assertThrows(UnauthorizedException.class, () -> {CreateGameResult result = chessService.createGame("shibboleth", new CreateGameRequest("kasparov vs deep blue"));});

        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void listNoGames() {
        ChessService chessService = new ChessService(new MemoryDataAccess());
        try {
            String authToken = chessService.registerUser(new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com")).authToken();
            assertEquals(0, chessService.listGames(new ListGamesRequest(authToken)).games().size());
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void joinGame() {
        ChessService chessService = new ChessService(new MemoryDataAccess());

        try {
            String authToken = chessService.registerUser(new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com")).authToken();
            CreateGameResult game = chessService.createGame(authToken, new CreateGameRequest("kasparov vs deep blue"));
            chessService.joinGame(authToken, new JoinGameRequest(ChessGame.TeamColor.BLACK, game.gameID()));
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void joinGameBadAuth() {
        ChessService chessService = new ChessService(new MemoryDataAccess());
        try {
            String authToken = chessService.registerUser(new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com")).authToken();
            CreateGameResult game = chessService.createGame(authToken, new CreateGameRequest("kasparov vs deep blue"));
            int gameID = chessService.createGame(authToken, new CreateGameRequest("kasparov vs deep blue")).gameID();

            assertThrows(UnauthorizedException.class, () -> {chessService.joinGame("nonsense", new JoinGameRequest(ChessGame.TeamColor.BLACK, game.gameID()));});
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void joinGameStealColor() {
        ChessService chessService = new ChessService(new MemoryDataAccess());
        try {
            String authToken = chessService.registerUser(new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com")).authToken();
            CreateGameResult game = chessService.createGame(authToken, new CreateGameRequest("kasparov vs deep blue"));
            chessService.joinGame(authToken, new JoinGameRequest(ChessGame.TeamColor.BLACK, game.gameID()));
            assertThrows(AlreadyTakenException.class, () -> {chessService.joinGame(authToken, new JoinGameRequest(ChessGame.TeamColor.BLACK, game.gameID()));});
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void joinGameBadID() {
        ChessService chessService = new ChessService(new MemoryDataAccess());
        try {
            String authToken = chessService.registerUser(new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com")).authToken();
            CreateGameResult game = chessService.createGame(authToken, new CreateGameRequest("kasparov vs deep blue"));
            assertThrows(BadRequestException.class, () -> {chessService.joinGame(authToken, new JoinGameRequest(ChessGame.TeamColor.BLACK, 1000));});
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void listMultipleGames() {
        ChessService chessService = new ChessService(new MemoryDataAccess());
        try {
            String authToken = chessService.registerUser(new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com")).authToken();
            CreateGameResult game = chessService.createGame(authToken, new CreateGameRequest("kasparov vs deep blue"));
            chessService.createGame(authToken, new CreateGameRequest("brown v board"));
            chessService.createGame(authToken, new CreateGameRequest("roe v wade"));
            assertEquals(3, chessService.listGames(new ListGamesRequest(authToken)).games().size());
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void clear() {
        ChessService chessService = new ChessService(new MemoryDataAccess());
        try {
            String authToken = chessService.registerUser(new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com")).authToken();
            CreateGameResult game = chessService.createGame(authToken, new CreateGameRequest("kasparov vs deep blue"));
            chessService.clear();
            assertThrows(UnauthorizedException.class, () -> {
                chessService.login(new LoginRequest("Alice", "pa55w0rd"));
            });
            authToken = chessService.registerUser(new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com")).authToken();
            assertEquals(0, chessService.listGames(new ListGamesRequest(authToken)).games().size());
        } catch (Exception ex) {
            fail();
        }
    }
}
