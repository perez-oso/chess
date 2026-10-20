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
        try {
            assertEquals("Alice", chessService.registerUser(testRegisterRequest).username());
        } catch(Exception ex) {return;}
    }

    @Test
    void RegisterDuplicateUser() {
        ChessService chessService = new ChessService(new MemoryDataAccess());
        RegisterRequest testRegisterRequest = new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com");
        try {
            chessService.registerUser(testRegisterRequest);
        } catch(Exception ex) {return;}

        assertThrows(AlreadyTakenException.class, () -> {chessService.registerUser(testRegisterRequest);});
    }

    @Test
    void ClearTest() {
        ChessService chessService = new ChessService(new MemoryDataAccess());

        try{
            chessService.registerUser(new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com"));
            chessService.registerUser(new RegisterRequest("Bob", "sdnicjwndij", "bob@mail.com"));
            chessService.clear();
        } catch(Exception ex) {return;}
        //add code to use other data and check if clear worked
    }
}
