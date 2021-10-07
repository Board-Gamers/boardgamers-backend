package com.a404.boardgamers.GameQuestion.Domain.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameAnswerLikeId implements Serializable {
    private String userId;
    private int answerId;
}