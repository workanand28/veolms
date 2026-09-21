package com.veolms.auth;

import com.veolms.user.dto.RegisterRequest;
import com.veolms.user.dto.UserResponse;
import com.veolms.user.service.UserService;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Test
    void registerDelegatesToUserService() {
        AuthService authService = mock(AuthService.class);
        UserService userService = mock(UserService.class);
        AuthController controller = new AuthController(authService, userService);
        RegisterRequest request = new RegisterRequest(
                "Jane",
                "Doe",
                "jane@example.com",
                "correct horse battery staple"
        );
        UserResponse expected = new UserResponse(
                1L,
                "Jane",
                "Doe",
                "jane@example.com",
                Instant.now()
        );
        when(userService.register(request)).thenReturn(expected);

        assertSame(expected, controller.register(request));
        verify(userService).register(request);
    }
}