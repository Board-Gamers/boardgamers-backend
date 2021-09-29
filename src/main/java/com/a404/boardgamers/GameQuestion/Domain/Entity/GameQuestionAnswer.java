package com.a404.boardgamers.GameQuestion.Domain.Entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "game_question_answer")
@ToString
@DynamicUpdate
@DynamicInsert
public class GameQuestionAnswer {
    @Builder
    public GameQuestionAnswer(int questionId, String content, String writerId) {
        this.questionId = questionId;
        this.content = content;
        this.writerId = writerId;
    }

    @Id
    private int id;
    private int questionId;
    private String content;
    private Timestamp addDate;
    private String writerId;
}