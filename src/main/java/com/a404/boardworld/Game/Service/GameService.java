package com.a404.boardworld.Game.Service;

import com.a404.boardworld.Game.Domain.Repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GameService {
    private final GameRepository gameRepository;
}
