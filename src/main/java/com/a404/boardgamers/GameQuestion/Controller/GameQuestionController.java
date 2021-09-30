package com.a404.boardgamers.GameQuestion.Controller;

import com.a404.boardgamers.GameQuestion.DTO.GameQuestionDTO;
import com.a404.boardgamers.GameQuestion.Service.GameQuestionService;
import com.a404.boardgamers.Util.Response;
import com.a404.boardgamers.Util.TokenExtraction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
                                               @RequestParam(defaultValue = "10") int pageSize) {

        if (gameId != null && questionId == null) {
            return gameQuestionService.getGameQuestion(gameId, page, pageSize);
        } else if (gameId == null && questionId != null) {
            return gameQuestionService.getGameQuestionAnswer(questionId);
        }
        return Response.newResult(HttpStatus.BAD_REQUEST, "잘못된 접근입니다.", null);
    }

    @PostMapping("/qna")
    public ResponseEntity<Response> uploadQuestion(HttpServletRequest request, @RequestBody GameQuestionDTO.uploadGameQuestionDTO requestDTO) {
        String userId = TokenExtraction.getLoginId(request);
        if (userId == null) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요.", null);
        }
        return gameQuestionService.uploadGameQuestion(userId, requestDTO);
    }

    @DeleteMapping("/qna/{questionId}")
    public ResponseEntity<Response> deleteQuestion(HttpServletRequest request, @PathVariable Integer questionId) {
        if (questionId == null) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "questionId가 없습니다.", null);
        }
        String userId = TokenExtraction.getLoginId(request);
        if (userId == null) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요.",  null);
        }
       return  gameQuestionService.deleteQuestion(userId, questionId);
    }
}