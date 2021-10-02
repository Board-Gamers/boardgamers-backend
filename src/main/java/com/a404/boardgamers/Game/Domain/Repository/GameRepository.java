package com.a404.boardgamers.Game.Domain.Repository;

import com.a404.boardgamers.Game.Domain.Entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Integer>, JpaSpecificationExecutor<Game> {
    Optional<Game> findGameById(int id);

    long countGamesByNameContainsOrNameKorContains(String keyword1, String keyword2);

    Page<Game> findAll(Specification<Game> spec, Pageable pageable);

    List<Game> findGamesByNameContainsOrNameKorContainsOrDescriptionContainsOrderByRank(String keyword1, String keyword2, String keyword3, Pageable pageable);

    List<Game> findGamesByNameContainsOrNameKorContainsOrDescriptionContainsOrderByUsersRatedDesc(String keyword1, String keyword2, String keyword3, Pageable pageable);

    List<Game> findGamesByNameContainsOrNameKorContainsOrDescriptionContainsAndMinAgeGreaterThanEqualAndMaxPlayersLessThanEqualAndMinPlayersGreaterThanEqualAndMaxPlayTimeLessThanEqualOrderByRank(String keyword1, String keyword2, String keyword3, int minAge, int maxPlayers, int minPlayers, int maxPlayTime, Pageable pageable);

    List<Game> findGamesByNameContainsOrNameKorContainsOrDescriptionContainsAndMinAgeGreaterThanEqualAndMaxPlayersLessThanEqualAndMinPlayersGreaterThanEqualAndMaxPlayTimeLessThanEqualOrderByUsersRatedDesc(String keyword1, String keyword2, String keyword3, int minAge, int maxPlayers, int minPlayers, int maxPlayTime, Pageable pageable);

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

    // 카테고리 검색 + 최소 연령 + 최대 인원 + 최소 인원 + 최대 시간 + 리뷰 순
    List<Game> findGamesByCategoryContainsAndMinAgeGreaterThanEqualAndMaxPlayersLessThanEqualAndMinPlayersGreaterThanEqualAndMaxPlayTimeLessThanEqualOrderByUsersRatedDesc(String category, int minAge, int maxPlayers, int minPlayers, int maxPlayTime, Pageable pageable);

    // 카테고리 검색 + 최소 연령 + 최대 인원 + 최소 인원 + 최대 시간 + 랭킹 순
    List<Game> findGamesByCategoryContainsAndMinAgeGreaterThanEqualAndMaxPlayersLessThanEqualAndMinPlayersGreaterThanEqualAndMaxPlayTimeLessThanEqualOrderByRank(String category, int minAge, int maxPlayers, int minPlayers, int maxPlayTime, Pageable pageable);


}