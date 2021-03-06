package com.a404.boardgamers.Review.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ReviewDTO {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReviewDetailResponse {
        private int id;
        private int userId;
        private String userNickname;
        private Double rating;
        private String comment;
        private String gameName;
        private String gameNameKor;
        private int gameId;
        private String createdAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReviewInsertRequest {
        private Double rating;
        private String comment;
        private int gameId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReviewUpdateRequest {
        private int id;
        private Double rating;
        private String comment;
    }
}