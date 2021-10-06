package com.a404.boardgamers.Review.Domain.Entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "review")
@ToString
@DynamicUpdate
@DynamicInsert
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "user_nickname", nullable = false)
    private String userNickname;

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

    public void updateReview(Double rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }
}
