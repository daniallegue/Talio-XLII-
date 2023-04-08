package server.api.Admin;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import commons.utils.HardcodedIDGenerator;
import commons.utils.IDGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import server.api.Board.BoardService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AdminServiceTest {


    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGeneratePassword() {
        HardcodedIDGenerator idGenerator1 = new HardcodedIDGenerator();
        idGenerator1.setHardcodedID("password");
        adminService = new AdminService(idGenerator1);
        adminService.generatePassword();
        String password1 = idGenerator1.generateID().toString();
        String password2 = adminService.getPassword();
        assertEquals(password1,password2);
    }

    @Test
    public void testResetPassword() {
        HardcodedIDGenerator idGenerator1 = new HardcodedIDGenerator();
        HardcodedIDGenerator idGenerator2 = new HardcodedIDGenerator();
        idGenerator1.setHardcodedID("password");
        idGenerator2.setHardcodedID("newPassword");

        adminService = new AdminService(idGenerator1);
        adminService.generatePassword();

        String password1 = idGenerator1.generateID().toString();
        String password2 = adminService.getPassword();
        assertEquals(password1,password2);

        adminService.setidGenerator(idGenerator2);
        String password3 = adminService.resetPassword();
        String password4 = adminService.getPassword();
        assertEquals(password3,password4);
    }
    @Test
    public void testRetrievePassword() {
        HardcodedIDGenerator idGenerator1 = new HardcodedIDGenerator();
        idGenerator1.setHardcodedID("password");
        adminService = new AdminService(idGenerator1);
        assertNull(adminService.getPassword());

        String password1 = idGenerator1.generateID().toString();
        String password2 = adminService.retrievePassword();
        assertEquals(password1,password2);
    }
}
