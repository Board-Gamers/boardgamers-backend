package com.a404.boardgamers.Game.Domain.Repository;

import com.a404.boardgamers.Game.Domain.Entity.GameRecommend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRecommendRepository extends JpaRepository<GameRecommend, Integer> {
    List<GameRecommend> findGameRecommendsByUserIdOrderByRank(int userId);

}
