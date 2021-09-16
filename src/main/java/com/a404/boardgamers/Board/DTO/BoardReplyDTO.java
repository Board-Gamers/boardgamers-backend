package com.a404.boardgamers.Board.Dto;

import lombok.*;

public class BoardReplyDTO {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BoardReplyResponse{
        private int qnaId;
        private String title;
        private String content;
        private String addDate;
    }
}
