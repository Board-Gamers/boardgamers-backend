package com.a404.boardgamers.Game.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class GameDTO {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GameListResponse {
        private int id;
        private String thumbnail;
        private String name;
        private String nameKor;
        private String category;
        private Double averageRate;
        private int usersRated;
        private int rank;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GameDetailResponse {
        private int id;
        private String name;
        private String nameKor;
        private String image;
        private String description;
        private int yearPublished;
        private int minPlayers;
        private int maxPlayers;
        private int minPlayTime;
        private int minAge;
        private String category;
        private String playType;
        private String series;
//        private String designer;
//        private String artist;
//        private String publisher;
        private int usersRated;
        private Double averageRate;
        private int rank;
    }

}