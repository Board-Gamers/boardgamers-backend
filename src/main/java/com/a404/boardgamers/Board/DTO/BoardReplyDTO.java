package com.a404.boardgamers.Board.Dto;

import lombok.Getter;
import lombok.Builder;

public class BoardReplyDTO {
    @Getter
    @Builder
    public static class BoardReplyResponse{
        private int qnaId;
        private String title;
        private String content;
        private String writerNickname;
        private String addDate;
    }
}
