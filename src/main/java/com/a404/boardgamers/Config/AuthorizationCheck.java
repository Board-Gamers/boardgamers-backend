package com.a404.boardgamers.Config;

import java.util.ArrayList;
import java.util.List;

public class AuthorizationCheck {
    public static AuthorizationCheck object;
    private List<String> tokenRequiredPathList;

    private AuthorizationCheck() {
        tokenRequiredPathList = new ArrayList<>();
        tokenRequiredPathList.add("/user/withdraw");
        tokenRequiredPathList.add("/board/upload");
    }

    public static AuthorizationCheck getObject() {
        if (object == null) {
            object = new AuthorizationCheck();
        }
        return object;
    }

    public List<String> getPathList() {
        return tokenRequiredPathList;
    }
}