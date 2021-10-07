package com.a404.boardgamers.GameQuestion.Domain.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Table(name = "game_question")
@DynamicUpdate
@DynamicInsert
public class GameQuestion {
    @Builder
    public GameQuestion(String title, int gameId, String writerId) {
        this.title = title;
        this.gameId = gameId;
        this.writerId = writerId;
    }

    @Id
    private int id;
    private String title;
    private Timestamp addDate;
    private int gameId;
    private String writerId;
    private Timestamp editDate;
}