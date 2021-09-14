package com.a404.boardgamers.User.Service;

import com.a404.boardgamers.User.Domain.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public void findUserById(String id){
        userRepository.findUserById(id);
    }
}
