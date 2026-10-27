package service;
import chess.ChessGame;
import dataaccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChessServiceTests {
    ChessService chessService;

    @BeforeEach
    void initialize() {
        chessService = new ChessService(new MemoryDataAccess());
    }

    @Test
    void RegisterOneUser() {
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
        RegisterRequest testRegisterRequest = new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com");
        try {
            chessService.registerUser(testRegisterRequest);
        } catch(Exception ex) {fail();}

        assertThrows(AlreadyTakenException.class, () -> {chessService.registerUser(testRegisterRequest);});
    }

    @Test
    void RegisterBadRequest() {
        RegisterRequest testRegisterRequest = new RegisterRequest("Alice", null, "alicedoe@gmail.com");
        try {
            assertThrows(BadRequestException.class, () -> {chessService.registerUser(testRegisterRequest);});;
        } catch(Exception ex) {fail();}
    }

    @Test
    void Clear() {
        ChessService chessService = new ChessService(new MemoryDataAccess());

        try{
            chessService.registerUser(new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com"));
            chessService.registerUser(new RegisterRequest("Bob", "sdnicjwndij", "bob@mail.com"));
            chessService.clear();
            assertThrows(BadRequestException.class, () -> {chessService.login(new LoginRequest("Bob", "sdnicjwndij"));});
        } catch(Exception ex) {return;}
    }

    @Test
    void loginRegisteredUser() {
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
       try {
            chessService.registerUser(new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com"));
            assertThrows(UnauthorizedException.class, () -> {chessService.logout(new LogoutRequest("loremipsum"));});
        } catch (Exception ex) {
           fail();
        }
    }

    @Test
    void createGame() {
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
        try {
            chessService.registerUser(new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com"));
            assertThrows(UnauthorizedException.class, () -> {CreateGameResult result = chessService.createGame("shibboleth", new CreateGameRequest("kasparov vs deep blue"));});

        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void listNoGames() {
        try {
            String authToken = chessService.registerUser(new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com")).authToken();
            assertEquals(0, chessService.listGames(new ListGamesRequest(authToken)).games().size());
        } catch (Exception ex) {
            fail();
        }
    }

    String authToken;
    CreateGameResult game;

    @BeforeEach
    void setupGame() {
        try {
            String authToken = chessService.registerUser(new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com")).authToken();
            CreateGameResult game = chessService.createGame(authToken, new CreateGameRequest("kasparov vs deep blue"));
        } catch (Exception ex) {
                fail();
            }
    }

    @Test
    void joinGame() {
        try {
            chessService.joinGame(authToken, new JoinGameRequest(ChessGame.TeamColor.BLACK, game.gameID()));
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void joinGameBadAuth() {
        try {
            assertThrows(UnauthorizedException.class, () -> {chessService.joinGame("nonsense", new JoinGameRequest(ChessGame.TeamColor.BLACK, game.gameID()));});
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void joinGameStealColor() {
        try {
            chessService.joinGame(authToken, new JoinGameRequest(ChessGame.TeamColor.BLACK, game.gameID()));
            assertThrows(AlreadyTakenException.class, () -> {chessService.joinGame(authToken, new JoinGameRequest(ChessGame.TeamColor.BLACK, game.gameID()));});
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void joinGameBadID() {
        try {
            assertThrows(UnauthorizedException.class, () -> {chessService.joinGame(authToken, new JoinGameRequest(ChessGame.TeamColor.BLACK, -3));});
        } catch (Exception ex) {
            fail();
        }
    }

    



}
