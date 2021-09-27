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
@Table(name = "qna_reply")
@ToString
@DynamicUpdate
@DynamicInsert
public class BoardReply {
    @Id
    private int id;
    private int qnaId;
    private String title;
    private String content;
    private Timestamp addDate;

    public void updateReply(String title, String content) {
        this.title = title;
        this.content = content;
    }
}