package com.a404.boardgamers.GameQuestion.Domain.Repository;

import com.a404.boardgamers.GameQuestion.Domain.Entity.GameQuestion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameQuestionRepository extends JpaRepository<GameQuestion, Integer> {

    List<GameQuestion> findAllByGameId(int gameId, Pageable pageable);

    long countAllBy();

    long countByGameId(int gameId);

    Optional<GameQuestion> findById(int id);

    List<GameQuestion> findAllByOrderByAddDate(Pageable pageable);

    long countGameQuestionsByWriterId(String writerId);
}