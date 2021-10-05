package com.a404.boardgamers.Board.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class BoardDTO {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BoardUploadRequest {
        private String title;
        private String content;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BoardUpdateRequest {
        private int id;
        private String title;
        private String content;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BoardListResponse {
        private int id;
        private String writerNickname;
        private String title;
        private String addDate;
        // 조회수
        private int viewCnt;
        private boolean isAnswered;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BoardResponse {
        private int id;
        private String writerNickname;
        private String title;
        private String content;
        private String addDate;
        // 조회수
        private int viewCnt;
    }

}