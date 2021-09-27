package com.a404.boardgamers.Game.Domain.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "recommend_result")
@ToString
@DynamicUpdate
@DynamicInsert
public class GameRecommend {
    @Id
    private int id;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "game_id", nullable = false)
    private int gameId;

    @Column(name = "rank", nullable = false)
    private int rank;

    @Column(name = "predicted_rating", nullable = false)
    private Double predictedRate;
}
