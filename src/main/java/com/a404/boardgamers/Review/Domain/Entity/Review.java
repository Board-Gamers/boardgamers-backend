package com.a404.boardgamers.Review.Domain.Entity;

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
@Table(name = "review")
@ToString
@DynamicUpdate
@DynamicInsert
public class Review {
    @Id
    private int id;

    private String user;

    private Double rating;

    private String comment;

    private String gameName;

    private int gameId;

    private Timestamp createdAt;

    private Timestamp updatedAt;

}
