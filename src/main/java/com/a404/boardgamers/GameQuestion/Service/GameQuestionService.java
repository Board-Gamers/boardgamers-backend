package com.a404.boardgamers.GameQuestion.Service;

import com.a404.boardgamers.GameQuestion.Domain.Entity.GameQuestion;
import com.a404.boardgamers.GameQuestion.Domain.Entity.GameQuestionAnswer;
import com.a404.boardgamers.GameQuestion.Domain.Repository.GameQuestionAnswerRepository;
import com.a404.boardgamers.GameQuestion.Domain.Repository.GameQuestionRepository;
import com.a404.boardgamers.Util.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GameQuestionService {
    private final GameQuestionRepository gameQuestionRepository;
    private final GameQuestionAnswerRepository gameQuestionAnswerRepository;

    public ResponseEntity<Response> getGameQuestion(int gameId, int page, int pageSize) {
        long totalItemCount = gameQuestionRepository.countByGameId(gameId);
        HashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("totalPageItemCnt", totalItemCount);
        linkedHashMap.put("totalPage", ((totalItemCount - 1) / pageSize) + 1);
        linkedHashMap.put("nowPage", page);
        linkedHashMap.put("nowPageSize", pageSize);
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);

        List<GameQuestion> gameQuestionList = gameQuestionRepository.findAllByGameId(gameId, pageRequest);
        if (gameQuestionList.size() == 0) {
            return Response.newResult(HttpStatus.OK, "등록된 QnA가 없습니다.", null);
        }
        return Response.newResult(HttpStatus.OK, gameId + "번 QnA를 불러왔습니다.", gameQuestionList);
    }

    public ResponseEntity<Response> getGameQuestionAnswer(int questionId) {
        Optional<GameQuestionAnswer> optionalGameQuestionAnswer = gameQuestionAnswerRepository.findByQuestionId(questionId);
        if (!optionalGameQuestionAnswer.isPresent()) {
            return Response.newResult(HttpStatus.OK, "등록된 답변이 없습니다.", null);
        }
        GameQuestionAnswer gameQuestionAnswer = optionalGameQuestionAnswer.get();
        return Response.newResult(HttpStatus.OK, questionId + "번 문의에 대한 답변입니다.", gameQuestionAnswer);
    }
}