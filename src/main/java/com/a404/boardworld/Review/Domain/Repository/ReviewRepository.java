package com.a404.boardworld.Review.Domain.Repository;

import com.a404.boardworld.Review.Domain.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Integer> {
}
