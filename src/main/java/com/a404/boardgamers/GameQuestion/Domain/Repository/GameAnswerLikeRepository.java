package com.a404.boardgamers.GameQuestion.Domain.Repository;

import com.a404.boardgamers.GameQuestion.Domain.Entity.GameAnswerLike;
import com.a404.boardgamers.GameQuestion.Domain.Entity.GameAnswerLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameAnswerLikeRepository extends JpaRepository<GameAnswerLike, GameAnswerLikeId> {
    Optional<GameAnswerLike> findByUserIdAndAndAnswerId(String userId, int answerId);

    void deleteByUserIdAndAnswerId(String userId, int answerId);
}