package com.a404.boardgamers.Board.Service;

import com.a404.boardgamers.Board.Domain.Entity.BoardReply;
import com.a404.boardgamers.Board.Domain.Repository.BoardReplyRepository;
import com.a404.boardgamers.Board.DTO.BoardReplyDTO;
import com.a404.boardgamers.User.Domain.Entity.User;
import com.a404.boardgamers.User.Domain.Repository.UserRepository;
import com.a404.boardgamers.Util.Response;
import com.a404.boardgamers.Util.TokenExtraction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardReplyService {
    private final BoardReplyRepository boardReplyRepository;
    private final UserRepository userRepository;

    public ResponseEntity<Response> addAnswer(BoardReplyDTO.BoardReplyRequest replyRequest, HttpServletRequest httpServletRequest) {
        String userId = TokenExtraction.getLoginId(httpServletRequest);
        if (userId == null) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 다시 시도해주십시오.", null);
        }
        Optional<User> userOptional = userRepository.findUserByLoginId(userId);
        if (!userOptional.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "잘못된 접근입니다.", null);
        }
        if (!userOptional.get().isAdmin()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "관리자만 답을 달 수 있습니다.", null);
        }
        if (replyRequest.getQnaId() == null) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "답글을 달 질문 번호를 지정해야 합니다.", null);
        }
        if (replyRequest.getTitle().length() == 0 || replyRequest.getContent().length() == 0) {
            return Response.newResult(HttpStatus.NO_CONTENT, "제목과 내용을 모두 입력해야 합니다.", null);
        }
        BoardReply reply = BoardReply.builder()
                .qnaId(replyRequest.getQnaId())
                .title(replyRequest.getTitle())
                .content(replyRequest.getContent())
                .build();

        return Response.newResult(HttpStatus.OK, "문의글에 답을 달았습니다.", reply);
    }

    public ResponseEntity<Response> updateAnswer(BoardReplyDTO.BoardReplyUpdateRequest replyUpdateRequest, HttpServletRequest httpServletRequest) {
        String userId = TokenExtraction.getLoginId(httpServletRequest);
        if (userId == null) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 다시 시도해주십시오.", null);
        }
        Optional<User> userOptional = userRepository.findUserByLoginId(userId);
        if (!userOptional.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "잘못된 접근입니다.", null);
        }
        if (!userOptional.get().isAdmin()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "관리자만 답을 달 수 있습니다.", null);
        }
        if (replyUpdateRequest.getQnaId() == null) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "답글을 달 질문 번호를 지정해야 합니다.", null);
        }
        if (replyUpdateRequest.getTitle().length() == 0 || replyUpdateRequest.getContent().length() == 0) {
            return Response.newResult(HttpStatus.NO_CONTENT, "제목과 내용을 모두 입력해야 합니다.", null);
        }
        Optional<BoardReply> boardReply = boardReplyRepository.findBoardReplyById(replyUpdateRequest.getId());
        if (!boardReply.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "해당 요청을 수행할 수 없습니다.(원인 : 존재하지 않는 댓글)", null);
        }
        boardReply.get().updateReply(replyUpdateRequest.getTitle(), replyUpdateRequest.getContent());
        return Response.newResult(HttpStatus.OK, "답글을 수정했습니다.", boardReply.get());
    }

    public ResponseEntity<Response> deleteAnswer(int id, HttpServletRequest httpServletRequest) {
        String userId = TokenExtraction.getLoginId(httpServletRequest);
        if (userId == null) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 다시 시도해주십시오.", null);
        }
        Optional<User> userOptional = userRepository.findUserByLoginId(userId);
        if (!userOptional.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "잘못된 접근입니다.", null);
        }
        if (!userOptional.get().isAdmin()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "관리자에게만 삭제 권한이 있습니다.", null);
        }
        Optional<BoardReply> boardReply = boardReplyRepository.findBoardReplyById(id);
        if (!boardReply.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않는 답글 입니다.", null);
        }
        boardReplyRepository.delete(boardReply.get());
        return Response.newResult(HttpStatus.OK, "답글이 삭제 되었습니다.", id);
    }
}