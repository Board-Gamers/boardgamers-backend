package com.a404.boardgamers.User.Domain.Repository;

import com.a404.boardgamers.User.Domain.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    int countAllByNickname(String nickname);

    Optional<User> findUserByLoginId(String id);

    Optional<User> findUserByNickname(String nickname);

}