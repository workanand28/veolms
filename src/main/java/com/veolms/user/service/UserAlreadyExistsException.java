package com.veolms.user.service;

public class UserAlreadyExistsException extends RuntimeException{

    public UserAlreadyExistsException() {
        super("An account with this email already exists");
    }
}
