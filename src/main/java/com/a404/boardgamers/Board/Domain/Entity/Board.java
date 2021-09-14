package com.a404.boardgamers.Board.Domain.Entity;

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
    @Id
    private int id;
    private String writerId;
    private String writerNickname;
    private String title;
    private String content;
    private Timestamp addDate;
    private Timestamp editDate;
}
