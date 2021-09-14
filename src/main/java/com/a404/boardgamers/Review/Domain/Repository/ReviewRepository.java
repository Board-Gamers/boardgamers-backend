package com.a404.boardgamers.Review.Domain.Repository;

import com.a404.boardgamers.Review.Domain.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Integer> {
}
