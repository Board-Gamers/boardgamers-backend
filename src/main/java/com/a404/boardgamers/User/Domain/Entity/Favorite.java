package com.a404.boardgamers.User.Domain.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "favorite")
@ToString
@DynamicUpdate
@DynamicInsert
@IdClass(Favorite.class)
public class Favorite implements Serializable {
    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;
    @Id
    @Column(name = "game_id", nullable = false)
    private int gameId;
}