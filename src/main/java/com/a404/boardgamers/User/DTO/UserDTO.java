package com.a404.boardgamers.User.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDTO {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class signUp {
        String id;
        String nickname;
        String password;
        boolean gender;
        int age;

        public void setPassword(String password) {
            this.password = password;
        }
    }
}