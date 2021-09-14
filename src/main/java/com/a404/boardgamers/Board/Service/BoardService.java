package com.a404.boardgamers.Board2.Service;

import com.a404.boardgamers.Board2.Domain.Repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
}