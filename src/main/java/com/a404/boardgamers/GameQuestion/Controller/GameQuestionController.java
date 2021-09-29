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
    public ResponseEntity<Response> getGameQnA(@RequestParam(required = false) Integer gameId,
                                               @RequestParam(required = false) Integer questionId,
                                               @RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int pageSize) throws PageIndexLessThanZeroException {

        if (gameId != null && questionId == null) {
            return gameQuestionService.getGameQuestion(gameId, page, pageSize);
        } else if (gameId == null && questionId != null) {
            return gameQuestionService.getGameQuestionAnswer(questionId);
        }
        return Response.newResult(HttpStatus.BAD_REQUEST, "잘못된 접근입니다.", null);
    }
}