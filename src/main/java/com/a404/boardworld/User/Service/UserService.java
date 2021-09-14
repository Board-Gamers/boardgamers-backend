package com.a404.boardworld.User.Service;

import com.a404.boardworld.User.Domain.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public void findUserById(String id){
        userRepository.findUserById(id);
    }
}
