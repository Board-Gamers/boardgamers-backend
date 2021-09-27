package com.a404.boardgamers.Review.Domain.Repository;

import com.a404.boardgamers.Review.Domain.Entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    long countReviewsByGameId(int gameId);

    List<Review> findReviewsByGameId(int gameId, Pageable pageable);

    Optional<Review> findReviewByGameIdAndUserId(int id, int userId);

    List<Review> findByUserNickname(String userId, Pageable pageable);
}