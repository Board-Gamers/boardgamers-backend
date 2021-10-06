package com.a404.boardgamers.User.Domain.Repository;

import com.a404.boardgamers.User.Domain.Entity.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, Integer> {
    List<UserAchievement> findUserAchievementsByUserId(int userId);

    Optional<UserAchievement> findUserAchievementByUserIdAndAchievementId(int userId, int achievementId);
}
