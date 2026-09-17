package com.veolms.user.dto;


import java.time.Instant;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        Instant createdAt
) {
}