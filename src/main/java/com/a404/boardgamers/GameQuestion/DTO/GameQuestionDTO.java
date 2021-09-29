package com.a404.boardgamers.GameQuestion.DTO;

import com.a404.boardgamers.GameQuestion.Domain.Entity.GameQuestion;
import com.a404.boardgamers.GameQuestion.Domain.Entity.GameQuestionAnswer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class GameQuestionDTO {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class getGameQuestionDTO {
        GameQuestion gameQuestion;
        GameQuestionAnswer gameQuestionAnswer;
    }
}