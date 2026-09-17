package com.veolms.user.dto;


public record CreateUserRequest(
        String firstName,
        String lastName,
        String email
) {
}