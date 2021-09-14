package com.a404.boardgamers.Board.Domain.Repository;

import com.a404.boardgamers.Board.Domain.Entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Integer> {
}
