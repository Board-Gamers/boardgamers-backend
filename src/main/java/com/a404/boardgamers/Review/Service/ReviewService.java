package com.a404.boardgamers.Review.Service;

import com.a404.boardgamers.Game.Domain.Entity.Category;
import com.a404.boardgamers.Game.Domain.Entity.Game;
import com.a404.boardgamers.Game.Domain.Repository.CategoryRepository;
import com.a404.boardgamers.Game.Domain.Repository.GameRepository;
import com.a404.boardgamers.Review.DTO.ReviewDTO;
import com.a404.boardgamers.Review.Domain.Entity.Review;
import com.a404.boardgamers.Review.Domain.Repository.ReviewRepository;
import com.a404.boardgamers.User.Domain.Entity.AchievementEnum;
import com.a404.boardgamers.User.Domain.Entity.User;
import com.a404.boardgamers.User.Domain.Repository.UserAchievementRepository;
import com.a404.boardgamers.User.Domain.Repository.UserRepository;
import com.a404.boardgamers.User.Service.UserService;
import com.a404.boardgamers.Util.Response;
import com.a404.boardgamers.Util.StringToList;
import com.a404.boardgamers.Util.TimestampToDateString;
import com.a404.boardgamers.Util.TokenExtraction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {
    private final UserService userService;
    private final ReviewRepository reviewRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final CategoryRepository categoryRepository;

    public ResponseEntity<Response> findGameReviews(int gameId, int page, int pageSize) {
        Optional<Game> game = gameRepository.findGameById(gameId);
        if (!game.isPresent()) {
            return Response.newResult(HttpStatus.NO_CONTENT, "???????????? ????????? ???????????? ????????????.", null);
        }
        long totalItemCount = reviewRepository.countReviewsByGameId(gameId);

        HashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("totalPageItemCnt", totalItemCount);
        linkedHashMap.put("totalPage", ((totalItemCount - 1) / pageSize) + 1);
        linkedHashMap.put("nowPage", page);
        linkedHashMap.put("nowPageSize", pageSize);

        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        List<Review> reviewList = reviewRepository.findReviewsByGameId(gameId, pageRequest);

        ArrayList<ReviewDTO.ReviewDetailResponse> arr = new ArrayList<>();

        for (Review item : reviewList) {
            String nameKor = game.get().getNameKor() == null ? "" : game.get().getNameKor();
            arr.add(ReviewDTO.ReviewDetailResponse.builder()
                    .id(item.getId())
                    .userId(item.getUserId())
                    .gameId(gameId)
                    .gameName(game.get().getName())
                    .gameNameKor(nameKor)
                    .userNickname(item.getUserNickname())
                    .comment(item.getComment())
                    .rating(item.getRating())
                    .createdAt(TimestampToDateString.convertDate(item.getCreatedAt()))
                    .build());
        }
        linkedHashMap.put("reviews", arr);

        return Response.newResult(HttpStatus.OK, gameId + "??? ", linkedHashMap);
    }

    @Transactional
    public ResponseEntity<Response> addGameReview(@RequestBody ReviewDTO.ReviewInsertRequest reviewInsertRequest, HttpServletRequest httpServletRequest) {
        String userId = TokenExtraction.getLoginId(httpServletRequest);
        if (userId == null) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "????????? ??? ??????????????????.", null);
        }
        Optional<User> user = userRepository.findUserByLoginId(userId);
        if (!user.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "???????????? ?????? ???????????????.", null);
        }
        // ?????? ????????? ?????? ?????? ?????? ????????? ????????????
        Optional<Review> optionalReview = reviewRepository.findReviewByGameIdAndUserId(reviewInsertRequest.getGameId(), user.get().getId());
        if (optionalReview.isPresent()) {
            return updateGameReview(ReviewDTO.ReviewUpdateRequest.builder()
                    .id(optionalReview.get().getId())
                    .comment(reviewInsertRequest.getComment())
                    .rating(reviewInsertRequest.getRating())
                    .build(), httpServletRequest);
        }
        Optional<Game> optionalGame = gameRepository.findGameById(reviewInsertRequest.getGameId());
        if (!optionalGame.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "???????????? ?????? ????????? ?????? ????????? ????????? ??? ????????????.", null);
        }
        Review review = Review.builder()
                .userId(user.get().getId())
                .userNickname(user.get().getNickname())
                .gameName(optionalGame.get().getName())
                .rating(reviewInsertRequest.getRating())
                .gameId(reviewInsertRequest.getGameId())
                .comment(reviewInsertRequest.getComment())
                .build();
        reviewRepository.save(review);
        reviewRepository.findAll();
        // ?????? ?????? cnt
        long cnt = reviewRepository.countReviewsByUserId(user.get().getId());
        // ?????? ?????? ??????????????? ??????.
        if (cnt == 1 || cnt == 5 || cnt == 10 || cnt == 20 || cnt == 50 || cnt == 100) {
            log.error(AchievementEnum.REVIEW.ordinal() + "?????? ?????? " + cnt + "??? ?????? ?????????.");
            userService.addAchievement(user.get().getId(), AchievementEnum.REVIEW.ordinal(), (int) cnt);
        }

        // ???????????? ???????????? ?????? ?????????.
        List<String> categories = StringToList.parsing(optionalGame.get().getCategory());
        for (String item : categories) {
            log.error(">> ???????????? " + item + " ????????????~~~");
            Optional<Category> category = categoryRepository.findCategoryByName(item);
            if (!category.isPresent()) {
                continue;
            }
            int categoryIdx = category.get().getId();
            userService.addAchievement(user.get().getId(), 11 + categoryIdx, 1);
        }

        return Response.newResult(HttpStatus.OK, "????????? ???????????????.", review);
    }

    @Transactional
    public ResponseEntity<Response> updateGameReview(@RequestBody ReviewDTO.ReviewUpdateRequest updateRequest, HttpServletRequest httpServletRequest) {
        String userId = TokenExtraction.getLoginId(httpServletRequest);
        if (userId == null) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "????????? ??? ??????????????????.", null);
        }
        Optional<User> optUser = userRepository.findUserByLoginId(userId);
        if (!optUser.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "???????????? ?????? ???????????????.", null);
        }
        Optional<Review> optReview = reviewRepository.findById(updateRequest.getId());
        if (!optReview.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "???????????? ????????? ????????????.", null);
        }
        if (optUser.get().getId() != optReview.get().getUserId()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "?????? ????????? ????????? ????????? ??? ????????????.", null);
        }
        optReview.get().updateReview(updateRequest.getRating(), updateRequest.getComment());
        return Response.newResult(HttpStatus.OK, "????????? ?????????????????????.", optReview.get());
    }

    @Transactional
    public ResponseEntity<Response> deleteGameReview(int id, HttpServletRequest httpServletRequest) {
        String userId = TokenExtraction.getLoginId(httpServletRequest);
        if (userId == null) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "????????? ??? ??????????????????.", null);
        }
        Optional<User> optUser = userRepository.findUserByLoginId(userId);
        if (!optUser.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "???????????? ?????? ???????????????.", null);
        }
        Optional<Review> optReview = reviewRepository.findById(id);
        if (!optReview.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "???????????? ?????? ???????????????.", null);
        }
        if (optUser.get().getId() != optReview.get().getUserId()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "?????? ????????? ????????? ????????? ??? ????????????.", null);
        }
        reviewRepository.delete(optReview.get());
        return Response.newResult(HttpStatus.OK, "????????? ?????????????????????.", null);
    }

    public long countReviewsByUserId(int id) {
        return reviewRepository.countReviewsByUserId(id);
    }
}