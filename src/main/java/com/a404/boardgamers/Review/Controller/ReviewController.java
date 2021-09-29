package com.a404.boardgamers.Review.Controller;

import com.a404.boardgamers.Review.DTO.ReviewDTO;
import com.a404.boardgamers.Review.Service.ReviewService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    @ApiOperation(value = "특정 게임의 전체 리뷰를 불러온다.")
    @GetMapping
    public ResponseEntity getGameReviews(@RequestParam(defaultValue = "1") int gameId,
                                         @RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int pageSize) {
        return reviewService.findGameReviews(gameId, page, pageSize);
    }

    @ApiOperation(value = "평가 남기기")
    @PostMapping
    public ResponseEntity addGameReview(@RequestBody ReviewDTO.ReviewInsertRequest reviewRequest, HttpServletRequest httpServletRequest) {
        return reviewService.addGameReview(reviewRequest, httpServletRequest);
    }

    @ApiOperation(value = "평가 수정하기. rating이 0 이면 리뷰 삭제.")
    @PutMapping
    public ResponseEntity updateGameReview(@RequestBody ReviewDTO.ReviewUpdateRequest reviewRequest, HttpServletRequest httpServletRequest) {
        if (reviewRequest.getRating() > 0.0) {
            return reviewService.updateGameReview(reviewRequest, httpServletRequest);
        } else {
            return reviewService.deleteGameReview(reviewRequest.getId(), httpServletRequest);
        }
    }

    @ApiOperation(value = "평가 삭제하기")
    @DeleteMapping
    public ResponseEntity deleteGameReview(@RequestParam int id, HttpServletRequest httpServletRequest) {
        return reviewService.deleteGameReview(id, httpServletRequest);
    }
}