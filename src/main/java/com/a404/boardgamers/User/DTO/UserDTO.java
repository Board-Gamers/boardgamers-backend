package com.a404.boardgamers.User.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDTO {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class signUpDTO {
        String id;
        String nickname;
        String password;

        public void setPassword(String password) {
            this.password = password;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class userProfileDTO {
        String nickname;
        Integer age;
        Boolean gender;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class changePasswordDTO {
        String password;
        String newPassword;
    }

}