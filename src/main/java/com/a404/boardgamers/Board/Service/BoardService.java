package com.a404.boardgamers.Board.Service;

import com.a404.boardgamers.Board.DTO.BoardDTO;
import com.a404.boardgamers.Board.DTO.BoardReplyDTO;
import com.a404.boardgamers.Board.Domain.Entity.Board;
import com.a404.boardgamers.Board.Domain.Entity.BoardReply;
import com.a404.boardgamers.Board.Domain.Repository.BoardReplyRepository;
import com.a404.boardgamers.Board.Domain.Repository.BoardRepository;
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
import java.util.*;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardReplyRepository boardReplyRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<Response> uploadQuestion(BoardDTO.BoardUploadRequest uploadRequest, HttpServletRequest httpServletRequest) {
        String userId = TokenExtraction.getLoginId(httpServletRequest);
        Optional<User> user = userRepository.findUserByLoginId(userId);
        if (!user.isPresent()) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "유효하지 않은 접근입니다.", null);
        }
        Board question = Board.builder()
                .title(uploadRequest.getTitle())
                .content(uploadRequest.getContent())
                .writerId(user.get().getLoginId())
                .writerNickname(user.get().getNickname())
                .build();
        Board questionRequest = boardRepository.save(question);
        return Response.newResult(HttpStatus.OK, "문의사항을 등록하였습니다.", questionRequest);
    }

    public ResponseEntity<Response> getQuestion(int id) {
        Optional<Board> question = boardRepository.findBoardById(id);
        if (!question.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "해당 게시글이 없습니다.", null);
        }
        String questionUploadedDate = TimestampToDateString.convertDate(question.get().getAddDate());
        BoardDTO.BoardResponse questionResponse = BoardDTO.BoardResponse.builder()
                .writerNickname(question.get().getWriterNickname())
                .id(question.get().getId())
                .title(question.get().getTitle())
                .content(question.get().getContent())
                .addDate(questionUploadedDate)
                .viewCnt(question.get().getViewCnt())
                .build();
        // 조회수 반영 로직 고민해보기.

        // 답변 추가하기.
        List<BoardReply> answers = boardReplyRepository.findBoardRepliesByQnaId(id);
        ArrayList<BoardReplyDTO.BoardReplyResponse> arr = new ArrayList<>();

        for (BoardReply item : answers) {
            String itemDate = TimestampToDateString.convertDate(item.getAddDate());
            arr.add(BoardReplyDTO.BoardReplyResponse.builder()
                    .qnaId(item.getQnaId())
                    .title(item.getTitle())
                    .content(item.getContent())
                    .addDate(itemDate)
                    .build());
        }
        HashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("question", questionResponse);
        linkedHashMap.put("reply", arr);

        return Response.newResult(HttpStatus.OK, "게시글을 불러왔습니다.", linkedHashMap);
    }

    @Transactional
    public ResponseEntity<Response> updateQuestion(BoardDTO.BoardUpdateRequest boardUpdateRequest, HttpServletRequest httpServletRequest) {
        String userId = TokenExtraction.getLoginId(httpServletRequest);
        if (userId == null) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요.", null);
        }
        Optional<User> user = userRepository.findUserByLoginId(userId);
        if (!user.isPresent()) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "유효하지 않은 접근입니다.", null);
        }
        Optional<Board> question = boardRepository.findBoardById(boardUpdateRequest.getId());

        if (!question.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않는 게시글입니다.", null);
        }

        if (!userId.equals(question.get().getWriterId())) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "자신이 작성한 게시글만 수정할 수 있습니다.", null);
        }
        question.get().updateBoard(boardUpdateRequest.getTitle(), boardUpdateRequest.getContent());
        return Response.newResult(HttpStatus.OK, "게시글이 수정되었습니다.", question.get());
    }

    public ResponseEntity<Response> deleteQuestion(int id, HttpServletRequest httpServletRequest) {
        String userId = TokenExtraction.getLoginId(httpServletRequest);
        if (userId == null) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요", null);
        }
        Optional<Board> question = boardRepository.findBoardById(id);
        if (!question.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않는 게시글이므로 해당 요청을 수행할 수 없습니다.", null);
        }
        if (!userId.equals(question.get().getWriterId())) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "게시글 삭제 권한이 없습니다.", null);
        }
        boardRepository.delete(question.get());
        // 달린 답글도 삭제?
        List<BoardReply> replies = boardReplyRepository.findBoardRepliesByQnaId(question.get().getId());
        for (BoardReply item : replies) {
            boardReplyRepository.delete(item);
        }
        return Response.newResult(HttpStatus.OK, "게시글이 삭제되었습니다.", null);
    }

    public ResponseEntity<Response> getQuestionList(int page, int pageSize) {
        long totalItemCount = boardRepository.countAllByTitleIsNotNull();
        HashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("totalPageItemCnt", totalItemCount);
        linkedHashMap.put("totalPage", ((totalItemCount - 1) / pageSize) + 1);
        linkedHashMap.put("nowPage", page);
        linkedHashMap.put("nowPageSize", pageSize);
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        List<Board> questionList = boardRepository.findAllQuestions(pageRequest);
        List<BoardDTO.BoardListResponse> arr = new ArrayList<>();
        for (Board item : questionList) {
            String itemDate = TimestampToDateString.convertDate(item.getAddDate());
            long replyCnt = boardReplyRepository.countBoardRepliesByQnaId(item.getId());
            boolean replied = replyCnt > 0;
            arr.add(BoardDTO.BoardListResponse.builder()
                    .id(item.getId())
                    .title(item.getTitle())
                    .writerNickname(item.getWriterNickname())
                    .addDate(itemDate)
                    .viewCnt(item.getViewCnt())
                    .isAnswered(replied)
                    .build());
        }

        linkedHashMap.put("questions", arr);
        return Response.newResult(HttpStatus.OK, "문의글을 불러왔습니다.", linkedHashMap);
    }

    public ResponseEntity<Response> findQuestionsByKeyword(String keyword, int page, int pageSize) {
        long totalItemCount = boardRepository.countBoardsByTitleContains(keyword);
        HashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("totalPageItemCnt", totalItemCount);
        linkedHashMap.put("totalPage", ((totalItemCount - 1) / pageSize) + 1);
        linkedHashMap.put("nowPage", page);
        linkedHashMap.put("nowPageSize", pageSize);
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        List<Board> questionList = boardRepository.findBoardsByTitleContainsOrderByAddDate(keyword, pageRequest);

        List<BoardDTO.BoardListResponse> arr = new ArrayList<>();
        for (Board item : questionList) {
            String itemDate = TimestampToDateString.convertDate(item.getAddDate());
            arr.add(BoardDTO.BoardListResponse.builder()
                    .id(item.getId())
                    .title(item.getTitle())
                    .writerNickname(item.getWriterNickname())
                    .addDate(itemDate)
                    .viewCnt(item.getViewCnt())
                    .build());
        }
        linkedHashMap.put("questions", arr);
        return Response.newResult(HttpStatus.OK, keyword + "로 문의글을 검색하였습니다.", linkedHashMap);
    }

    @Transactional
    public void viewCountUp(int id) {
        Optional<Board> optBoard = boardRepository.findById(id);
        if (optBoard.isPresent()) {
            optBoard.get().viewCountUp();
        }
    }
}