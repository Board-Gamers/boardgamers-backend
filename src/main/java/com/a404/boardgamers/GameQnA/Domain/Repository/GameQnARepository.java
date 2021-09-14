package com.a404.boardgamers.GameQnA.Domain.Repository;

import com.a404.boardgamers.GameQnA.Domain.Entity.GameQnA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameQnARepository extends JpaRepository<GameQnA, Integer> {
//    Optional<>
}