package com.a404.boardgamers.Board.Domain.Repository;

import com.a404.boardgamers.Board.Domain.Entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    Optional<Board> findBoardById(int id);

    @Query(value = "SELECT * FROM qna order by add_date desc", nativeQuery = true)
    List<Board> findAllQuestions(Pageable pageable);

    long countAllByTitleIsNotNull();

    long countBoardsByTitleContains(String keyword);

    List<Board> findBoardsByTitleContainsOrderByAddDate(String keyword, Pageable pageable);
}