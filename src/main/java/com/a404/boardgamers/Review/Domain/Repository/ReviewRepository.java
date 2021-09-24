package com.a404.boardgamers.Review.Domain.Repository;

import com.a404.boardgamers.Review.Domain.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByUserNickname(String userId);
}