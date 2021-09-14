package com.a404.boardgamers.Game.Domain.Repository;

import com.a404.boardgamers.Game.Domain.Entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Integer> {

}