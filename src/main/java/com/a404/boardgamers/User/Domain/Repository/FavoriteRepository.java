package com.a404.boardgamers.User.Domain.Repository;

import com.a404.boardgamers.User.Domain.Entity.Favorite;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface FavoriteRepository extends JpaRepository<Favorite, Favorite> {
    Optional<Favorite> findAllByUserIdAndGameId(String userId, int gameId);

    List<Favorite> findByUserId(String userId, Pageable pageable);

    int countByUserId(String userId);
}