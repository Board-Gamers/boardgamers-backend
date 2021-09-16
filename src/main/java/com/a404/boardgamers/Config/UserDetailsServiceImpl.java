package com.a404.boardgamers.Config;

import com.a404.boardgamers.Exception.UserNotFoundException;
import com.a404.boardgamers.User.Domain.Entity.User;
import com.a404.boardgamers.User.Domain.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public MyUserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            if (!user.get().isWithdraw()) {
                return new MyUserDetails(user.get(), Collections.singleton(new SimpleGrantedAuthority(user.get().isAdmin() ? "ADMIN" : "USER")));
            }
            throw new UsernameNotFoundException(id);
        } else {
            throw new UserNotFoundException(id);
        }
    }
}
