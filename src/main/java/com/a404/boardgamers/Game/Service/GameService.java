package com.a404.boardgamers.Game.Service;

import com.a404.boardgamers.Game.Domain.Repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GameService {
    private final GameRepository gameRepository;
}