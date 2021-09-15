package com.a404.boardgamers.Board.Service;

import com.a404.boardgamers.Board.Domain.Entity.Board;
import com.a404.boardgamers.Board.Domain.Entity.BoardReply;
import com.a404.boardgamers.Board.Domain.Repository.BoardReplyRepository;
import com.a404.boardgamers.Board.Domain.Repository.BoardRepository;
import com.a404.boardgamers.Board.Dto.BoardDTO;
import com.a404.boardgamers.Board.Dto.BoardReplyDTO;
import com.a404.boardgamers.User.Domain.Entity.User;
import com.a404.boardgamers.User.Domain.Repository.UserRepository;
import com.a404.boardgamers.Util.Response;
import com.a404.boardgamers.Util.TimestampToDateString;
import com.a404.boardgamers.Util.TokenExtraction;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.awt.print.Pageable;
import java.util.*;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardReplyRepository boardReplyRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity uploadQuestion(BoardDTO.BoardUploadRequest uploadRequest, HttpServletRequest httpServletRequest) {
        String userId = TokenExtraction.check(httpServletRequest);
        Optional<User> user = userRepository.findUserById(userId);
        if (!user.isPresent()) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "유효하지 않은 접근입니다.", null);
        }
        Board question = Board.builder()
                .title(uploadRequest.getTitle())
                .content(uploadRequest.getContent())
                .writerId(user.get().getId())
                .writerNickname(user.get().getNickname())
                .build();
        boardRepository.save(question);
        return Response.newResult(HttpStatus.OK, "문의사항을 등록하였습니다.", question);
    }

    public ResponseEntity getQuestion(int id) {
        Optional<Board> getQuestion = boardRepository.findBoardById(id);
        String questionUploadedDate = TimestampToDateString.convertDate(getQuestion.get().getAddDate());
        BoardDTO.BoardResponse questionResponse = BoardDTO.BoardResponse.builder()
                .id(getQuestion.get().getId())
                .title(getQuestion.get().getTitle())
                .content(getQuestion.get().getContent())
                .addDate(questionUploadedDate)
                .build();

        // 답변도 같이 줄 것
//        long totalPageItemCnt = boardReplyRepository.countBoardRepliesByQnaId(id);
        List<BoardReply> answers = boardReplyRepository.findBoardRepliesByQnaId(id);
        ArrayList<BoardReplyDTO.BoardReplyResponse> arr = new ArrayList<>();

        // 관리자 답변 밖에 없으니 writer 별도 표기 하지 말까? 아니면 관리자라고 표기할까.
        for (BoardReply item : answers) {
            String itemDate = TimestampToDateString.convertDate(item.getAddDate());
            arr.add(BoardReplyDTO.BoardReplyResponse.builder()
                    .title(item.getTitle())
                    .content(item.getContent())
                    .writerNickname(item.getWriterNickname())
                    .addDate(itemDate)
                    .build());
        }
        HashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("question", questionResponse);
        linkedHashMap.put("reply", arr);

        return Response.newResult(HttpStatus.OK, "게시글을 불러왔습니다.", linkedHashMap);
    }

    @Transactional
    public ResponseEntity updateQuestion(BoardDTO.BoardUpdateRequest boardUpdateRequest, HttpServletRequest httpServletRequest) {
        String userId = TokenExtraction.check(httpServletRequest);
        if (userId == null) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요.", null);
        }
        Optional<User> user = userRepository.findUserById(userId);
        if (!user.isPresent()) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "유효하지 않은 접근입니다.", null);
        }
        Optional<Board> question = boardRepository.findBoardById(boardUpdateRequest.getId());
//        Board question = boardRepository.findBoardById(boardUpdateRequest.getId()).orElseGet(Board::new);
        if (!question.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않는 게시글입니다.", null);
        }

        if (!userId.equals(question.get().getWriterId())) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "자신이 작성한 게시글만 수정할 수 있습니다.", null);
        }
        question.get().updateBoard(boardUpdateRequest.getTitle(), boardUpdateRequest.getContent());
        return Response.newResult(HttpStatus.OK, "게시글이 수정되었습니다.", question.get() );
    }

    public ResponseEntity deleteQuestion(int id, HttpServletRequest httpServletRequest) {
        Optional<Board> question = boardRepository.findBoardById(id);
        if (!question.isPresent()) {

        }
        boardRepository.delete(question.get());
        return Response.newResult(HttpStatus.OK, "게시글이 삭제되었습니다.", null);
    }

    public ResponseEntity getQuestionList(int page, int pageSize) {
        long totalItemCount = boardRepository.countAllByTitleIsNotNull();
        HashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("totalPageItemCnt", totalItemCount);
        linkedHashMap.put("totalPage", ((totalItemCount - 1) / pageSize) + 1);
        linkedHashMap.put("nowPage", page);
        linkedHashMap.put("nowPageSize", pageSize);
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        List<Board> questionList = boardRepository.findAllQuestions((Pageable) pageRequest);
        List<BoardDTO.BoardResponse> arr = new ArrayList<>();
        for (Board item : questionList) {
            String itemDate = TimestampToDateString.convertDate(item.getAddDate());
            arr.add(BoardDTO.BoardResponse.builder()
                    .id(item.getId())
                    .title(item.getTitle())
                    .writerNickname(item.getWriterNickname())
                    .addDate(itemDate)
                    .viewCnt(item.getViewCnt())
                    .build());
        }

        linkedHashMap.put("questions", arr);
        return Response.newResult(HttpStatus.OK, "문의글을 불러왔습니다.", arr);
    }

}
