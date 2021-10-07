package com.a404.boardgamers.GameQuestion.Domain.Repository;

import com.a404.boardgamers.GameQuestion.Domain.Entity.GameQuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameQuestionAnswerRepository extends JpaRepository<GameQuestionAnswer, Integer> {
    Optional<GameQuestionAnswer> findById(int id);

    List<GameQuestionAnswer> findByQuestionId(int questionId);

    long countGameQuestionAnswersByWriterId(String userId);
}