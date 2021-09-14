package com.a404.boardworld.User.Domain.Repository;

import com.a404.boardworld.User.Domain.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>{
    Optional<User> findUserById(String id);

}
