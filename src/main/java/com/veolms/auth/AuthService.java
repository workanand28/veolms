
package com.veolms.auth;

import com.veolms.auth.dto.LoginRequest;
import com.veolms.auth.dto.LoginResponse;
import com.veolms.security.JwtService;

import com.veolms.user.entity.User;
import com.veolms.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserRepository userRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public LoginResponse login(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.email(),
                                request.password()
                        )
                );

        String authenticatedEmail = authentication.getName();

        User user = userRepository.findByEmailIgnoreCase(authenticatedEmail)
                .orElseThrow(() ->
                        new IllegalStateException("Authenticated user was not found")
                );

        String accessToken =
                jwtService.generateToken(user.getId().toString());

        return new LoginResponse(
                accessToken,
                "Bearer",
                jwtService.getExpirationSeconds()
        );
    }

}