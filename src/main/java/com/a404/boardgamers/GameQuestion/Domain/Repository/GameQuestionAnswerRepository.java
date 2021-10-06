package com.a404.boardgamers.GameQuestion.Domain.Repository;

import com.a404.boardgamers.GameQuestion.Domain.Entity.GameQuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameQuestionAnswerRepository extends JpaRepository<GameQuestionAnswer, Integer> {
    List<GameQuestionAnswer> findByQuestionId(int questionId);

    long countGameQuestionAnswersByWriterId(String userId);
}