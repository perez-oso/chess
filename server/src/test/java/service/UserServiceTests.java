package service;
import dataaccess.AlreadyTakenException;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceTests {
    @Test
    void RegisterResultTest() {
        UserService userService = new UserService(new MemoryDataAccess());
        RegisterRequest testRegisterRequest = new RegisterRequest("Alice", "pa55w0rd", "alicedoe@gmail.com");
        try {
            assertEquals("Alice", userService.registerUser(testRegisterRequest).username());
            System.out.println("Registered user.");
            userService.registerUser(testRegisterRequest).username();
        } catch (AlreadyTakenException ex) {
            System.out.println("Could not register duplicate user.");
        }



    }
}
