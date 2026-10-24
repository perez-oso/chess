package service;
import dataaccess.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChessServiceTests {
    @Test
    void RegisterOneUser() {
        ChessService chessService = new ChessService(new MemoryDataAccess());
        RegisterRequest testRegisterRequest = new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com");
        try {
            assertEquals("Alice", chessService.registerUser(testRegisterRequest).username());
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
    void Clear() {
        ChessService chessService = new ChessService(new MemoryDataAccess());

        try{
            chessService.registerUser(new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com"));
            chessService.registerUser(new RegisterRequest("Bob", "sdnicjwndij", "bob@mail.com"));
            chessService.clear();
        } catch(Exception ex) {return;}
        //add code to use other data and check if clear worked
    }

    @Test
    void loginRegisteredUser() {
        ChessService chessService = new ChessService((new MemoryDataAccess()));

        try {
            chessService.registerUser(new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com"));
            chessService.login(new LoginRequest("Alice", "pa55w0rd"));
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void loginUnregisteredUser() {
        ChessService chessService = new ChessService((new MemoryDataAccess()));

        try {
            chessService.registerUser(new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com"));
            assertThrows(UserNotFoundException.class, () -> {chessService.login(new LoginRequest("Bjork", "animalfries42069"));});
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void loginInvalidPassword() {
        ChessService chessService = new ChessService((new MemoryDataAccess()));

        try {
            chessService.registerUser(new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com"));
            assertThrows(UserNotFoundException.class, () -> {chessService.login(new LoginRequest("Alice", "saynopetobobhope"));});
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
}
