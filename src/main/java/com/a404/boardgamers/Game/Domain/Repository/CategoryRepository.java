package com.a404.boardgamers.Game.Domain.Repository;

import com.a404.boardgamers.Game.Domain.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findCategoryByName(String name);
}
