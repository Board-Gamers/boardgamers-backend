package com.a404.boardgamers.Board.Dto;

import lombok.Getter;
import lombok.Builder;

public class BoardDTO {
    @Getter
    @Builder
    public static class BoardUploadRequest {
        //        private String writerId;
//        private String writerNickname;
        private String title;
        private String content;

    }

    @Getter
    @Builder
    public static class BoardUpdateRequest {
        private int id;
        private String title;
        private String content;
    }

    @Getter
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