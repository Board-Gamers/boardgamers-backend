package com.a404.boardgamers.GameQuestion.Domain.Entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "game_answer_like")
@IdClass(GameAnswerLikeId.class)
@DynamicUpdate
@DynamicInsert
@Builder
public class GameAnswerLike {

    @Id
    private String userId;

    @Id
    private int answerId;

    private Boolean isLiked;

    public void updateIsLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }
}