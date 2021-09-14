package com.a404.boardworld.Game.Domain.Repository;

import com.a404.boardworld.Game.Domain.Entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Integer> {

}
