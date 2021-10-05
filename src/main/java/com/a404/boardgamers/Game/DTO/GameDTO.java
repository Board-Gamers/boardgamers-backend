package com.a404.boardgamers.Game.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class GameDTO {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GameListResponse {
        private int id;
        private String image;
        private String thumbnail;
        private String name;
        private String nameKor;
        private String category;
        private Double averageRate;
        private int minAge;
        private int maxPlayers;
        private int minPlayers;
        private int minPlayTime;
        private int maxPlayTime;
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
        private String thumbnail;
        private String image;
        private String description;
        private int yearPublished;
        private int minPlayers;
        private int maxPlayers;
        private int minPlayTime;
        private int minAge;
        private List<String> category;
        private List<String> playType;
        private List<String> series;
        private List<String> designer;
        private List<String> artist;
        private List<String> publisher;
        private int usersRated;
        private Double averageRate;
        private int rank;
    }

}