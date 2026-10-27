package service;
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
            chessService.registerUser(new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com");
            loginResult = chessService.login(new LoginRequest("Alice", "pa55w0rd"));
            assertEquals("Alice", loginResult.username());
            assertNotEquals(null, loginResult.authToken());
        } catch(Exception ex) {fail();}
    }

    @Test
    void loginBadRequest() {
      try {
            chessService.registerUser(new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com"));
            assertThrows(BadRequestException.class, () -> {chessService.login(new LoginRequest(null, "animalfries42069"));});
            assertThrows(BadRequestException.class, () -> {chessService.login(new LoginRequest("Bjork", null));});
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void loginInvalidPassword() {
        ChessService chessService = new ChessService((new MemoryDataAccess()));

        try {
            chessService.registerUser(new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com"));
            assertThrows(UnauthorizedException.class, () -> {chessService.login(new LoginRequest("Alice", "saynopetobobhope"));});
            assertThrows(BadRequestException.class, () -> {chessService.login(new LoginRequest("Ringo", "saynopetobobhope"));});
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void logoutValidAuth() {
        ChessService chessService = new ChessService((new MemoryDataAccess()));
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
        ChessService chessService = new ChessService((new MemoryDataAccess()));
        String authToken;

        try {
            chessService.registerUser(new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com"));
            assertThrows(UnauthorizedException.class, () -> {chessService.logout(new LogoutRequest("loremipsum"));});
        } catch (Exception ex) {
           fail();
        }
    }

    @Test
    void listGamesValidAuth() {

    }
}
