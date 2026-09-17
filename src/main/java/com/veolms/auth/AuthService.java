
package com.veolms.auth;

import com.veolms.auth.dto.LoginRequest;
import com.veolms.auth.dto.LoginResponse;
import com.veolms.security.JwtService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.email(),
                                request.password()
                        )
                );

        String userId = authentication.getName();

        String accessToken = jwtService.generateToken(userId);

        return new LoginResponse(
                accessToken,
                "Bearer",
                jwtService.getExpirationSeconds()
        );
    }
}