package com.a404.boardgamers.Review.Domain.Entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "review_data")
@ToString
@DynamicUpdate
@DynamicInsert
public class ReviewData {
    @Id
    private int id;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "user", nullable = false)
    private int userNickname;

    @Column(name = "rating", nullable = false)
    private Double rating;

    @Column(name = "comment", nullable = true)
    private String comment;

    @Column(name = "game_id", nullable = false)
    private int gameId;

    @Column(name = "game_name", nullable = false)
    private String gameName;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;


}
