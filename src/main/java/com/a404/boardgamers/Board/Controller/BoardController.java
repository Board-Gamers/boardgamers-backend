package com.a404.boardgamers.Board.Controller;

import com.a404.boardgamers.Board.DTO.BoardDTO;
import com.a404.boardgamers.Board.DTO.BoardReplyDTO;
import com.a404.boardgamers.Board.Service.BoardReplyService;
import com.a404.boardgamers.Board.Service.BoardService;
import com.a404.boardgamers.Util.Response;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
    private final BoardReplyService boardReplyService;

    @ApiOperation(value = "문의글을 업로드한다.")
    @PostMapping("/upload")
    public ResponseEntity<Response> uploadQuestion(@RequestBody BoardDTO.BoardUploadRequest boardUploadRequest, HttpServletRequest httpServletRequest) {
        System.out.println("컨트롤러 동작");
        return boardService.uploadQuestion(boardUploadRequest, httpServletRequest);
    }

    @ApiOperation(value = "게시판에 업로드된 문의글을 가져온다.")
    @GetMapping("/{id}")
    public ResponseEntity<Response> getQuestion(@PathVariable int id) {
        return boardService.getQuestion(id);
    }

    @ApiOperation(value = "업로드한 문의글을 수정한다.")
    @PutMapping("/update")
    public ResponseEntity<Response> updateQuestion(@RequestBody BoardDTO.BoardUpdateRequest boardUpdateRequest, HttpServletRequest httpServletRequest) {
        return boardService.updateQuestion(boardUpdateRequest, httpServletRequest);
    }

    @ApiOperation(value = "게시판에 업로드한 글을 지운다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteQuestion(@PathVariable int id, HttpServletRequest httpServletRequest) {
        return boardService.deleteQuestion(id, httpServletRequest);
    }

    @ApiOperation(value = "문의글 목록을 불러옵니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "검색하고 싶은 문의글 키워드", dataType = "String", paramType = "query", defaultValue = ""),
            @ApiImplicitParam(name = "page", value = "조회할 페이지 번호", dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pagesize", value = "페이지당 보여주는 데이터 개수", dataType = "int", paramType = "query", defaultValue = "10"),
    })
    @GetMapping("/list")
    public ResponseEntity<Response> getQuestionList(@RequestParam(defaultValue = "") String keyword, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        if (!keyword.equals("")) {
            return boardService.findQuestionsByKeyword(keyword, page, pageSize);
        } else {
            return boardService.getQuestionList(page, pageSize);
        }
    }

    @ApiOperation(value = "답글을 작성합니다.")
    @PostMapping("/reply")
    public ResponseEntity<Response> addAnswer(@RequestBody BoardReplyDTO.BoardReplyRequest boardReplyRequest, HttpServletRequest httpServletRequest) {
        return boardReplyService.addAnswer(boardReplyRequest, httpServletRequest);
    }

    @ApiOperation(value = "답글을 수정합니다.")
    @PutMapping("/reply")
    public ResponseEntity<Response> updateAnswer(@RequestBody BoardReplyDTO.BoardReplyUpdateRequest boardReplyUpdateRequest, HttpServletRequest httpServletRequest) {
        return boardReplyService.updateAnswer(boardReplyUpdateRequest, httpServletRequest);
    }

    @ApiOperation(value = "답글을 삭제합니다.")
    @DeleteMapping("/reply")
    public ResponseEntity<Response> deleteAnswer(@RequestParam int id, HttpServletRequest httpServletRequest) {
        return boardReplyService.deleteAnswer(id, httpServletRequest);
    }
}