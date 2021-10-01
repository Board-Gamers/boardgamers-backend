package com.a404.boardgamers.Review.Domain.Repository;

import com.a404.boardgamers.Game.Domain.Entity.Game;
import com.a404.boardgamers.Review.Domain.Entity.ReviewData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewDataRepository extends JpaRepository<ReviewData, Integer> {
    @Query(value = "select game_id, count(*) as revCnt from review_data group by game_id order by revCnt desc", nativeQuery = true)
    List<Object[]> findGamesOrderByReviewCnt(Pageable pageable);
}
