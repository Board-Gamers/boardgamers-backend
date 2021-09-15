package com.a404.boardgamers.Board.Domain.Repository;

import com.a404.boardgamers.Board.Domain.Entity.BoardReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardReplyRepository extends JpaRepository<BoardReply, Integer> {
    List<BoardReply> findBoardRepliesByQnaId(@Param(value = "qna_id") int id);

    long countBoardRepliesByQnaId(@Param(value = "qna_id") int id);
}
