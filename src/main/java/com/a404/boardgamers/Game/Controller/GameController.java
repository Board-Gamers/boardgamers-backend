package com.a404.boardgamers.Game.Controller;

import com.a404.boardgamers.Game.Domain.Entity.GameSpecs;
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
import java.util.HashMap;
import java.util.Map;

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


    //Map 이용한 검색 결과 얻기
    @ApiOperation(value = "필터 검색하기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nameKor", value = "검색할 게임 이름(한글"),
            @ApiImplicitParam(name = "name", value = "검색할 게임 이름(영어)"),
            @ApiImplicitParam(name = "category", value = "검색할 카테고리 이름"),
            @ApiImplicitParam(name = "order", value = "정렬 조건 이름(review, rank)"),
            @ApiImplicitParam(name = "minAge", value = "최소 연령"),
            @ApiImplicitParam(name = "minPlayers", value = "최소 플레이 인원 수"),
            @ApiImplicitParam(name = "maxPlayers", value = "최대 플레이 인원 수"),
            @ApiImplicitParam(name = "maxPlayTime", value = "최대 플레이 시간"),
            @ApiImplicitParam(name = "page", value = "조회할 페이지 번호", dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pagesize", value = "페이지당 보여주는 데이터 개수", dataType = "int", paramType = "query", defaultValue = "10"),
    })
    @GetMapping("/search")
    public ResponseEntity findGames(@RequestParam(required = false) Map<String, Object> searchRequest) {
        Map<GameSpecs.SearchKey, Object> searchKeys = new HashMap<>();
        int page = 1, pageSize = 10;
        String order = "rank";
        for (String key : searchRequest.keySet()) {
            if (key.equals("page")) {
                page = Integer.parseInt(searchRequest.get("page").toString());
            } else if (key.equals("pageSize")) {
                pageSize = Integer.parseInt(searchRequest.get("pageSize").toString());
            } else if (key.equals("order")) {
                order = searchRequest.get("order").toString().equals("review") ? "usersRated" : "rank";
            } else {
                try {
                    searchKeys.put(GameSpecs.SearchKey.valueOf(key.toUpperCase()), searchRequest.get(key));
                } catch (IllegalArgumentException e) {
                    System.out.println("유효하지 않은 key 값 > " + key);
                }
            }
        }
        return searchKeys.isEmpty()
                ? gameService.findAll(order, page, pageSize)
                : gameService.findGamesWithFilter(searchKeys, order, page, pageSize);
    }

    @ApiOperation(value = "추천 결과 반환")
    @GetMapping("/recommend")
    public ResponseEntity<Response> findRecommendation(HttpServletRequest httpServletRequest) {
        return gameService.findGameRecommendationsByUserId(httpServletRequest);
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