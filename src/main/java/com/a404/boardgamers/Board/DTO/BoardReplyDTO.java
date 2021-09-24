package com.a404.boardgamers.Board.DTO;

import lombok.*;

public class BoardReplyDTO {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BoardReplyRequest {
        private Integer qnaId;
        private String title;
        private String content;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BoardReplyUpdateRequest {
        private int id;
        private Integer qnaId;
        private String title;
        private String content;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BoardReplyResponse {
        private int qnaId;
        private String title;
        private String content;
        private String addDate;
    }
}