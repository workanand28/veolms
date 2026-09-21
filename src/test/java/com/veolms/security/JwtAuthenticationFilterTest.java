package com.veolms.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

class JwtAuthenticationFilterTest {

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void missingUserForValidTokenIsIgnoredAsUnauthenticated()
            throws ServletException, IOException {
        JwtService jwtService = mock(JwtService.class);
        CustomUserDetailsService userDetailsService =
                mock(CustomUserDetailsService.class);
        Claims claims = mock(Claims.class);
        when(jwtService.validateAndExtractClaims("token")).thenReturn(claims);
        when(claims.getSubject()).thenReturn("99");
        doThrow(new UsernameNotFoundException("Invalid credentials"))
                .when(userDetailsService).loadEmailByUserId("99");

        JwtAuthenticationFilter filter =
                new JwtAuthenticationFilter(jwtService, userDetailsService);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(userDetailsService).loadEmailByUserId("99");
    }
}