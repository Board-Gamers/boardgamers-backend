package com.a404.boardgamers.Board.Domain.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "qna")
@ToString
@DynamicUpdate
@DynamicInsert
public class Board {
    @Builder
    public Board(String writerId, String writerNickname, String title, String content, int viewCnt) {
        this.writerId = writerId;
        this.writerNickname = writerNickname;
        this.title = title;
        this.content = content;
        this.viewCnt = viewCnt;
    }

    @Id
    private int id;
    private String writerId;
    private String writerNickname;
    private String title;
    private String content;
    private int viewCnt;
    private Timestamp addDate;
    private Timestamp editDate;

    public void updateBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
