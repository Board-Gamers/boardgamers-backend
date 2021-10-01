package com.a404.boardgamers.Game.Controller;

import com.a404.boardgamers.Exception.PageIndexLessThanZeroException;
import com.a404.boardgamers.Game.Service.GameService;
import com.a404.boardgamers.Util.Response;
import com.a404.boardgamers.Util.TokenExtraction;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;

    @ApiOperation(value = "게임 상세 정보를 가져온다.")
    @GetMapping("/{id}")
    public ResponseEntity<Response> getGameInformation(@PathVariable int id) {
        return gameService.getGameInformation(id);
    }

    @ApiOperation(value = "게임 검색(keyword, category 안 주면 전체 검색)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "검색할 게임 이름"),
            @ApiImplicitParam(name = "category", value = "검색할 카테고리 이름"),
            @ApiImplicitParam(name = "page", value = "조회할 페이지 번호", dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pagesize", value = "페이지당 보여주는 데이터 개수", dataType = "int", paramType = "query", defaultValue = "10"),
    })
    @GetMapping("/search")
    public ResponseEntity<Response> findGames(@RequestParam(defaultValue = "") String keyword,
                                              @RequestParam(defaultValue = "") String category,
                                              @RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "10") int pageSize) throws PageIndexLessThanZeroException {

        if (keyword.equals("") && category.equals("")) {
            try {
                return gameService.findAllGames(page, pageSize);
            } catch (ArithmeticException | IllegalArgumentException e) {
                throw new PageIndexLessThanZeroException();
            }
        } else if (keyword.equals("")) {
            return gameService.findGamesByCategory(category, page, pageSize);
        } else {
            return gameService.findGamesByKeyword(keyword, page, pageSize);
        }
    }

    @ApiOperation(value = "게임 전체 리스트 검색")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "조회할 페이지 번호", dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pagesize", value = "페이지당 보여주는 데이터 개수", dataType = "int", paramType = "query", defaultValue = "10"),
    })
    public ResponseEntity<Response> getGameList(@RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "10") int pageSize) {
        return gameService.findAllGames(page, pageSize);
    }

    @ApiOperation(value = "추천 결과 반환")
    @GetMapping("/recommend")
    public ResponseEntity<Response> findRecommendation(HttpServletRequest httpServletRequest) {
        return gameService.findGameRecommendationsByUserId(httpServletRequest);
    }

    @ApiOperation(value = "게임 리뷰 많은 순으로 검색하기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order", value = "검색할 순서", dataType = "String", defaultValue = "review"),
            @ApiImplicitParam(name = "page", value = "조회할 페이지 번호", dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pagesize", value = "페이지당 보여주는 데이터 개수", dataType = "int", paramType = "query", defaultValue = "10"),
    })
    @GetMapping
    public ResponseEntity findGamesOrderByReview(@RequestParam(defaultValue = "review") String order,
                                                 @RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "20") int pageSize) {
        if (order.equals("review")) {
            return gameService.findGamesOrderByReview(page, pageSize);
        }
        return gameService.findGamesOrderByReview(page, pageSize);
    }

    @ApiOperation(value = "게임 즐겨찾기. 같은 요청을 보내면 즐겨찾기가 해제됩니다.")
    @PostMapping("/favorite")
    public ResponseEntity<Response> addFavorite(HttpServletRequest httpServletRequest, @RequestParam Integer gameId) {
        if (gameId == null) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "gameId가 없습니다", null);
        }
        String userId = TokenExtraction.getLoginId(httpServletRequest);
        if (userId == null) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요.", null);
        }
        return gameService.addFavorite(userId, gameId);

    }
}