package com.a404.boardgamers.Game.Domain.Repository;

import com.a404.boardgamers.Game.Domain.Entity.Game;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Integer> {
    Optional<Game> findGameById(int id);

    long countGamesByNameContainsOrNameKorContains(String keyword1, String keyword2);

    List<Game> findGamesByNameContainsOrNameKorContainsOrDescriptionContainsOrderByRank(String keyword1, String keyword2, String keyword3, Pageable pageable);

    List<Game> findGamesByNameContainsOrNameKorContainsOrDescriptionContainsOrderByUsersRated(String keyword1, String keyword2, String keyword3, Pageable pageable);

    @Query(value = "select count(*) from boardgame", nativeQuery = true)
    long countAll();

    @Query(value = "select * from boardgame order by boardgame.rank", nativeQuery = true)
    List<Game> findAllGamesOrderByRank(Pageable pageable);

    @Query(value = "select * from boardgame order by users_rated", nativeQuery = true)
    List<Game> findAllGamesOrderByReview(Pageable pageable);

    long countGamesByCategoryContains(String category);

    List<Game> findGamesByCategoryContainsOrderByRank(String category, Pageable pageable);

    List<Game> findGamesByCategoryContainsOrderByUsersRatedDesc(String category, Pageable pageable);

    List<Game> findGamesByMinAgeGreaterThanEqualAndMaxPlayersLessThanEqualAndMinPlayersGreaterThanEqualAndMaxPlayTimeLessThanEqual(int minAge, int maxPlayers, int minPlayers, int maxPlayTime);
}