package com.a404.boardgamers.GameQnA.Service;

import com.a404.boardgamers.GameQnA.Domain.Repository.GameQnARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GameQnAService {
    private final GameQnARepository gameQnARepository;
}