package com.a404.boardgamers.Board.Controller;

import com.a404.boardgamers.Board.Dto.BoardDTO;
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

    @ApiOperation(value = "문의글을 업로드한다.")
    @PostMapping("/upload")
    public ResponseEntity<Response> uploadQuestion(@RequestBody BoardDTO.BoardUploadRequest boardUploadRequest, HttpServletRequest httpServletRequest){
        System.out.println("컨트롤러 동작");
        return boardService.uploadQuestion(boardUploadRequest, httpServletRequest);
    }

    @ApiOperation(value = "게시판에 업로드된 문의글을 가져온다.")
    @GetMapping("/{id}")
    public ResponseEntity getQuestion(@PathVariable int id){
        return boardService.getQuestion(id);
    }

    @ApiOperation(value = "업로드한 문의글을 수정한다.")
    @PutMapping("/update")
    public ResponseEntity updateQuestion(@RequestBody BoardDTO.BoardUpdateRequest boardUpdateRequest, HttpServletRequest httpServletRequest) {
        return boardService.updateQuestion(boardUpdateRequest, httpServletRequest);
    }

    @ApiOperation(value = "게시판에 업로드한 글을 지운다.")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteQuestion(@PathVariable int id, HttpServletRequest httpServletRequest) {
        return boardService.deleteQuestion(id, httpServletRequest);
    }

    @ApiOperation(value = "문의글 목록을 불러옵니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "검색하고 싶은 문의글 키워드", dataType = "String", paramType = "query", defaultValue = ""),
            @ApiImplicitParam(name = "page", value = "조회할 페이지 번호", dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pagesize", value = "페이지당 보여주는 데이터 개수", dataType = "int", paramType = "query", defaultValue = "10"),
    })
    @GetMapping("/list")
    public ResponseEntity getQuestionList(@RequestParam(defaultValue = "") String keyword, @RequestParam(defaultValue = "1")int page, @RequestParam(defaultValue = "10")int pageSize) {
        if(!keyword.equals("")){
            return boardService.findQuestionsByKeyword(keyword, page, pageSize);
        }else {
            return boardService.getQuestionList(page, pageSize);
        }
    }
}
