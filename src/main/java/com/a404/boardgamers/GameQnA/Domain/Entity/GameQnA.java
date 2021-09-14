package com.a404.boardgamers.GameQnA.Domain.Entity;

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
@Table(name = "game_qna")
@ToString
@DynamicUpdate
@DynamicInsert
public class GameQnA {
    @Builder
    public GameQnA(String title, String context, int gameId, String writerId) {
        this.title = title;
        this.context = context;
        this.gameId = gameId;
        this.writerId = writerId;
    }

    @Id
    private int id;
    private String title;
    private String context;
    private Timestamp addDate;
    private int gameId;
    private String writerId;
    private Timestamp editDate;
}