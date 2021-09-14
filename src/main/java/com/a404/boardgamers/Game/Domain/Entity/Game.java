package com.a404.boardgamers.Game.Domain.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "boardgame")
@ToString
@DynamicUpdate
@DynamicInsert
public class Game {
    @Id
    private int id;
    private String thumbnail;
    private String image;
    private String name;
    private String nameKor;
    private String description;
    private int yearPublished;
    private int minPlayers;
    private int maxPlayers;
    private int minPlayTime;
    private int minAge;
    private String category;
    private String playType;
    private String series;
    private String designer;
    private String artist;
    private String publisher;
    private int usersRated;
    private Double averageRate;
    private int rank;
}
