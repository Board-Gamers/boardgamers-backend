package com.a404.boardgamers.User.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserAcheivementDTO {
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class AchievementResponse {
        int id;
        String title;
        String detail;
        String date;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class AchievementRecordResponse {
        int id;
        String title;
        String detail;
        int count;
        String date;
    }
}
