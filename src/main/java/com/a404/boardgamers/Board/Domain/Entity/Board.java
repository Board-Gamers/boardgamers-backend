package com.a404.boardgamers.Board.Domain.Entity;

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
@Builder
@Entity
@Table(name = "qna")
@ToString
@DynamicUpdate
@DynamicInsert
public class Board {

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

    public void viewCountUp() {
        this.viewCnt++;
    }
}
