package service;
import dataaccess.AlreadyTakenException;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ChessServiceTests {
    @Test
    void RegisterOneUser() {
        ChessService chessService = new ChessService(new MemoryDataAccess());
        RegisterRequest testRegisterRequest = new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com");
        assertEquals("Alice", chessService.registerUser(testRegisterRequest).username());
    }

    @Test
    void RegisterDuplicateUser() {
        ChessService chessService = new ChessService(new MemoryDataAccess());
        RegisterRequest testRegisterRequest = new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com");
        chessService.registerUser(testRegisterRequest);
        assertThrows(AlreadyTakenException.class, chessService.registerUser(testRegisterRequest));
    }

    @Test
    void ClearTest() {
        ChessService chessService = new ChessService(new MemoryDataAccess());
        chessService.registerUser(new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com"));
        chessService.registerUser(new RegisterRequest("Bob", "sdnicjwndij", "bob@mail.com"));
        chessService.clear();
        //add code to use other data and check if clear worked
    }
}
