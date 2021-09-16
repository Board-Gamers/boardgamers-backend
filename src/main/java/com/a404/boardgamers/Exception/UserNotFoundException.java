package com.a404.boardgamers.Exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String id) {
        super(id + "NotFoundException");
    }
}