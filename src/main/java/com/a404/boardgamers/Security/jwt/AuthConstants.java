package com.a404.boardgamers.Security.jwt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthConstants {
    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_TYPE = "Bearer";
}