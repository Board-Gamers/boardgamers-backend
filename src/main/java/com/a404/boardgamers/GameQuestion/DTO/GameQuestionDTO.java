package com.a404.boardgamers.GameQuestion.DTO;

import com.a404.boardgamers.GameQuestion.Domain.Entity.GameQuestion;
import com.a404.boardgamers.GameQuestion.Domain.Entity.GameQuestionAnswer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

public class GameQuestionDTO {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class getGameQuestionDTO {
        GameQuestion gameQuestion;
        GameQuestionAnswer gameQuestionAnswer;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class GameAnswerDTO {
        int id;
        int questionId;
        String content;
        Timestamp addDate;
        String writerId;
        int likes;
        Boolean isLiked;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class uploadGameQuestionDTO {
        String title;
        Integer gameId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class uploadGameQuestionAnswerDTO {
        String content;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class likeOnAnswerDTO {
        Boolean isLiked;
    }
}