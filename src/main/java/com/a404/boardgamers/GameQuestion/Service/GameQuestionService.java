package com.a404.boardgamers.GameQuestion.Service;

import com.a404.boardgamers.Game.Domain.Entity.Game;
import com.a404.boardgamers.Game.Domain.Repository.GameRepository;
import com.a404.boardgamers.GameQuestion.DTO.GameQuestionDTO;
import com.a404.boardgamers.GameQuestion.Domain.Entity.GameQuestion;
import com.a404.boardgamers.GameQuestion.Domain.Entity.GameQuestionAnswer;
import com.a404.boardgamers.GameQuestion.Domain.Repository.GameQuestionAnswerRepository;
import com.a404.boardgamers.GameQuestion.Domain.Repository.GameQuestionRepository;
import com.a404.boardgamers.User.Domain.Entity.AchievementEnum;
import com.a404.boardgamers.User.Domain.Entity.User;
import com.a404.boardgamers.User.Domain.Repository.UserRepository;
import com.a404.boardgamers.User.Service.UserService;
import com.a404.boardgamers.Util.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GameQuestionService {
    private final GameQuestionRepository gameQuestionRepository;
    private final GameQuestionAnswerRepository gameQuestionAnswerRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final UserService userService;

    public ResponseEntity<Response> getAllGameQuestion(int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);

        List<GameQuestion> gameQuestionList = gameQuestionRepository.findAllByOrderByAddDate(pageRequest);
        if (gameQuestionList.size() == 0) {
            return Response.newResult(HttpStatus.OK, "등록된 글이 없습니다.", null);
        }
        return Response.newResult(HttpStatus.OK, "글을 불러왔습니다.", gameQuestionList);
    }

    public ResponseEntity<Response> getGameQuestion(int gameId, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);

        List<GameQuestion> gameQuestionList = gameQuestionRepository.findAllByGameId(gameId, pageRequest);
        if (gameQuestionList.size() == 0) {
            return Response.newResult(HttpStatus.OK, "등록된 QnA가 없습니다.", null);
        }
        return Response.newResult(HttpStatus.OK, gameId + "번 QnA를 불러왔습니다.", gameQuestionList);
    }

    public ResponseEntity<Response> getGameQuestionAnswer(int questionId) {
        List<GameQuestionAnswer> gameQuestionAnswerList = gameQuestionAnswerRepository.findByQuestionId(questionId);
        if (gameQuestionAnswerList.size() == 0) {
            return Response.newResult(HttpStatus.OK, "등록된 답변이 없습니다.", null);
        }
        return Response.newResult(HttpStatus.OK, questionId + "번 문의에 대한 답변입니다.", gameQuestionAnswerList);
    }

    @Transactional
    public ResponseEntity<Response> uploadGameQuestionAnswer(String userId, int questionId, GameQuestionDTO.uploadGameQuestionAnswerDTO requestDTO) {
        Optional<User> optionalUser = userRepository.findUserByLoginId(userId);
        if (!optionalUser.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다.", null);
        }
        User user = optionalUser.get();
        Optional<GameQuestion> optionalGameQuestion = gameQuestionRepository.findById(questionId);
        if (!optionalGameQuestion.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않는 글입니다.", null);
        }
        String content = requestDTO.getContent();
        if (content == null || content.equals("")) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "답변을 입력해주세요.", null);
        }
        GameQuestionAnswer gameQuestionAnswer = GameQuestionAnswer.builder().questionId(questionId).content(content).writerId(userId).build();
        gameQuestionAnswerRepository.save(gameQuestionAnswer);
        long cnt = gameQuestionAnswerRepository.countGameQuestionAnswersByWriterId(userId);
        userService.addAchievement(user.getId(), AchievementEnum.ANSWER.ordinal(), (int) cnt);
        return Response.newResult(HttpStatus.OK, questionId + "번 문의글에 답변을 등록했습니다.", null);
    }

    @Transactional
    public ResponseEntity<Response> uploadGameQuestion(String userId, GameQuestionDTO.uploadGameQuestionDTO requestDTO) {
        Optional<User> optionalUser = userRepository.findUserByLoginId(userId);
        if (!optionalUser.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다.", null);
        }
        User user = optionalUser.get();
        String nickname = user.getNickname();
        if (requestDTO.getTitle() == null || requestDTO.getContent() == null || requestDTO.getGameId() == null) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "파라미터를 모두 입력해주세요.", null);
        }
        Optional<Game> optionalGame = gameRepository.findGameById(requestDTO.getGameId());
        if (!optionalGame.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않는 게임입니다.", null);
        }
        GameQuestion gameQuestion = GameQuestion.builder()
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .gameId(requestDTO.getGameId())
                .writerId(nickname)
                .build();
        gameQuestionRepository.save(gameQuestion);
        long cnt = gameQuestionRepository.countGameQuestionsByWriterId(userId);
        userService.addAchievement(user.getId(), AchievementEnum.QUESTION.ordinal(), (int) cnt);
        return Response.newResult(HttpStatus.OK, "글을 작성했습니다.", null);
    }

    public ResponseEntity<Response> deleteQuestion(String userId, int questionId) {
        Optional<User> optionalUser = userRepository.findUserByLoginId(userId);
        if (!optionalUser.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다.", null);
        }
        User user = optionalUser.get();
        String nickname = user.getNickname();
        Optional<GameQuestion> optionalGameQuestion = gameQuestionRepository.findById(questionId);
        if (!optionalGameQuestion.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않는 글입니다.", null);
        }
        GameQuestion gameQuestion = optionalGameQuestion.get();
        if (!gameQuestion.getWriterId().equals(nickname)) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "자신의 글만 삭제할 수 있습니다.", null);
        }
        gameQuestionRepository.delete(gameQuestion);
        return Response.newResult(HttpStatus.OK, "글을 삭제하였습니다.", null);
    }

    public long countQuestionCount(String userId) {
        return gameQuestionRepository.countGameQuestionsByWriterId(userId);
    }
}