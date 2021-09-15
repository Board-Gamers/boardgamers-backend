package com.a404.boardgamers.Board.Controller;

import com.a404.boardgamers.Board.Dto.BoardDTO;
import com.a404.boardgamers.Board.Service.BoardService;
import com.a404.boardgamers.Util.Response;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    @ApiOperation(value = "문의글을 업로드한다.")
    @PostMapping("/upload")
    public ResponseEntity<Response> uploadQuestion(@RequestBody BoardDTO.BoardUploadRequest boardUploadRequest, HttpServletRequest httpServletRequest){
        return boardService.uploadQuestion(boardUploadRequest, httpServletRequest);
    }

    @ApiOperation(value = "게시판에 업로드된 문의글을 가져온다.")
    @GetMapping("/{id}")
    public ResponseEntity getQuestion(@RequestParam(value = "id") int id){
        return boardService.getQuestion(id);
    }

    @ApiOperation(value = "업로드한 문의글을 수정한다.")
    @PostMapping
    public ResponseEntity updateQuestion(@RequestBody BoardDTO.BoardUpdateRequest boardUpdateRequest, HttpServletRequest httpServletRequest) {
        return boardService.updateQuestion(boardUpdateRequest, httpServletRequest);
    }

    @ApiOperation(value = "게시판에 업로드한 글을 지운다.")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteQuestion(@RequestParam(value = "id") int id, HttpServletRequest httpServletRequest) {
        return boardService.deleteQuestion(id, httpServletRequest);
    }

    @ApiOperation(value = "")
    @GetMapping("/list")
    public ResponseEntity getQuestionList(int page, int pageSize) {
        return boardService.getQuestionList(page, pageSize);
    }
}
