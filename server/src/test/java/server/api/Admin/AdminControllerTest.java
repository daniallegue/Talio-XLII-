package server.api.Admin;

import commons.Result;
import commons.utils.HardcodedIDGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.request.async.DeferredResult;
import server.api.Admin.AdminController;
import server.api.Admin.AdminService;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {


    @Mock
    AdminService adminService;

    @InjectMocks
    AdminController adminController;

    @Test
    void getPasswordTest() {
        doReturn("password").when(adminService).retrievePassword();
        Result<String> result = adminController.getPassword();
        assertEquals(Result.SUCCESS.of("password"), result);
    }

    @Test
    void resetPasswordTest() {
        Consumer<String> consumerMock = mock(Consumer.class);
        Map<Object, Consumer<String>> listenersMock = new HashMap<>();
        listenersMock.put("listener1", consumerMock);

        adminController.setListeners(listenersMock);
        when(adminService.resetPassword()).thenReturn("newPassword");

        Result<Object> result = adminController.resetPassword();

        assertEquals(Result.SUCCESS, result);
    }

    @Test
    void testCheckPasswordChange() {
        Consumer<String> consumerMock = mock(Consumer.class);
        Map<Object, Consumer<String>> listenersMock = new HashMap<>();
        listenersMock.put("listener1", consumerMock);


        assertEquals(0,adminController.getListeners().size());
        DeferredResult<Result<String>> result = adminController.checkPasswordChange();
        assertEquals(1,adminController.getListeners().size());
        adminController.getListeners().forEach((k,v)->{
            v.accept("listener1");
        });

        assertEquals(Result.SUCCESS.of("listener1"), result.getResult());

    }

}