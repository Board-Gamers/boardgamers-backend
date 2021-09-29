package com.a404.boardgamers.GameQuestion.Controller;

import com.a404.boardgamers.Exception.PageIndexLessThanZeroException;
import com.a404.boardgamers.GameQuestion.Service.GameQuestionService;
import com.a404.boardgamers.Util.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/game")
public class GameQuestionController {
    private final GameQuestionService gameQuestionService;

    @GetMapping("/qna")
    public ResponseEntity<Response> getGameQnA(@RequestParam Integer gameId,
                                               @RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int pageSize) throws PageIndexLessThanZeroException {
        if (gameId == null) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "게임 아이디를 입력해주세요.", null);
        }
        return gameQuestionService.getGameQuestion(gameId, page, pageSize);
    }
}