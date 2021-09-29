package com.a404.boardgamers.Game.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class GameRecommendDTO {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GameListResponse {
        private int id;
        private String thumbnail;
        private String image;
        private String name;
        private String nameKor;
        private String category;
        private Double averageRate;
        private Double predictedRate;
        private int usersRated;
        //        private int rank;
        private int predictedRank;
    }
}
