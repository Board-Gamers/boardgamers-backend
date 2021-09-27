package com.a404.boardgamers.Game.Service;

import com.a404.boardgamers.Game.DTO.GameDTO;
import com.a404.boardgamers.Game.DTO.GameRecommendDTO;
import com.a404.boardgamers.Game.Domain.Entity.Game;
import com.a404.boardgamers.Game.Domain.Entity.GameRecommend;
import com.a404.boardgamers.Game.Domain.Repository.GameRecommendRepository;
import com.a404.boardgamers.Game.Domain.Repository.GameRepository;
import com.a404.boardgamers.User.Domain.Entity.User;
import com.a404.boardgamers.User.Domain.Repository.UserRepository;
import com.a404.boardgamers.Util.Response;
import com.a404.boardgamers.Util.TokenToId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RequiredArgsConstructor
@Service
public class GameService {
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final GameRecommendRepository gameRecommendRepository;

    public ResponseEntity getGameInformation(int id) {
        Optional<Game> item = gameRepository.findGameById(id);
        if (!item.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "불가능한 접근입니다.", null);
        }
        String titleKor = item.get().getNameKor() == null ? "" : item.get().getNameKor();
        GameDTO.GameDetailResponse game = GameDTO.GameDetailResponse.builder()
                .id(item.get().getId())
                .name(item.get().getName())
                .nameKor(titleKor)
                .description(item.get().getDescription())
                .image(item.get().getImage())
                .averageRate(item.get().getAverageRate())
                .rank(item.get().getRank())
                .category(item.get().getCategory())
                .minAge(item.get().getMinAge())
                .minPlayers(item.get().getMinPlayers())
                .maxPlayers(item.get().getMaxPlayers())
                .minPlayTime(item.get().getMinPlayTime())
                .usersRated(item.get().getUsersRated())
                .yearPublished(item.get().getYearPublished())
                .playType(item.get().getPlayType())
                .series(item.get().getSeries())
                .build();

        return Response.newResult(HttpStatus.OK, "게임 정보를 불러왔습니다.", game);
    }

    public ResponseEntity findGamesByKeyword(String keyword, int page, int pageSize) {

        long totalItemCount = gameRepository.countGamesByNameContainsOrNameKorContains(keyword, keyword);
        HashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("totalPageItemCnt", totalItemCount);
        linkedHashMap.put("totalPage", ((totalItemCount - 1) / pageSize) + 1);
        linkedHashMap.put("nowPage", page);
        linkedHashMap.put("nowPageSize", pageSize);
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);

        List<Game> gameList = gameRepository.findGamesByNameContainsOrNameKorContainsOrderByRank(keyword, keyword, pageRequest);
        ArrayList<GameDTO.GameListResponse> arr = new ArrayList<>();
        for (Game item : gameList) {
            String titleKor = item.getNameKor() != null ? item.getNameKor() : "";
            arr.add(GameDTO.GameListResponse.builder()
                    .thumbnail(item.getThumbnail())
                    .name(item.getName())
                    .nameKor(titleKor)
                    .category(item.getCategory())
                    .averageRate(item.getAverageRate())
                    .usersRated(item.getUsersRated())
                    .rank(item.getRank())
                    .build());
        }
        linkedHashMap.put("list", arr);

        return Response.newResult(HttpStatus.OK, keyword + "로 검색한 게임 정보입니다.", linkedHashMap);
    }

    public ResponseEntity findAllGames(int page, int pageSize){
        long totalItemCount = gameRepository.countAll();
        HashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("totalPageItemCnt", totalItemCount);
        linkedHashMap.put("totalPage", ((totalItemCount - 1) / pageSize) + 1);
        linkedHashMap.put("nowPage", page);
        linkedHashMap.put("nowPageSize", pageSize);
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);

        List<Game> gameList = gameRepository.findAllGamesOrderByRank(pageRequest);
        if (gameList.size() == 0) {
            return Response.newResult(HttpStatus.NO_CONTENT, "데이터가 없습니다.", null);
        }
        ArrayList<GameDTO.GameListResponse> arr = new ArrayList<>();
        for (Game item : gameList) {
            String titleKor = item.getNameKor() != null ? item.getNameKor() : "";
            arr.add(GameDTO.GameListResponse.builder()
                    .id(item.getId())
                    .thumbnail(item.getThumbnail())
                    .name(item.getName())
                    .nameKor(titleKor)
                    .category(item.getCategory())
                    .averageRate(item.getAverageRate())
                    .usersRated(item.getUsersRated())
                    .rank(item.getRank())
                    .build());
        }
        linkedHashMap.put("games", arr);

        return Response.newResult(HttpStatus.OK, "전체 게임을 불러옵니다.", linkedHashMap);
    }

    public ResponseEntity findGamesByCategory(String category, int page, int pageSize) {
        long totalItemCount = gameRepository.countGamesByCategoryContains(category);
        HashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("totalPageItemCnt", totalItemCount);
        linkedHashMap.put("totalPage", ((totalItemCount - 1) / pageSize) + 1);
        linkedHashMap.put("nowPage", page);
        linkedHashMap.put("nowPageSize", pageSize);
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);

        List<Game> gameList = gameRepository.findGamesByCategoryContainsOrderByRank(category, pageRequest);
        ArrayList<GameDTO.GameListResponse> arr = new ArrayList<>();
        for (Game item : gameList) {
            String titleKor = item.getNameKor() != null ? item.getNameKor() : "";
            arr.add(GameDTO.GameListResponse.builder()
                    .thumbnail(item.getThumbnail())
                    .name(item.getName())
                    .nameKor(titleKor)
                    .category(item.getCategory())
                    .averageRate(item.getAverageRate())
                    .usersRated(item.getUsersRated())
                    .rank(item.getRank())
                    .build());
        }
        linkedHashMap.put("games", arr);
        return Response.newResult(HttpStatus.OK, category + "의 게임 목록을 불러옵니다.", arr);
    }

    public ResponseEntity findGameRecommendationsByUserId(HttpServletRequest httpServletRequest) {
        String userId = TokenToId.getId(httpServletRequest);
        Optional<User> optionalUser = userRepository.findUserByLoginId(userId);
        if (!optionalUser.isPresent()) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요.", null);
        }

        List<GameRecommend> recommendList = gameRecommendRepository.findGameRecommendsByUserIdOrderByRank(optionalUser.get().getId() + 1000000);
        // 우리 DB의 유저 id 1번 == 추천 결과 user_id 1000001번
        ArrayList<GameRecommendDTO.GameListResponse> arr = new ArrayList<>();
        for (GameRecommend item : recommendList) {
            Game game = gameRepository.findGameById(item.getGameId()).get();
            String titleKor = game.getNameKor() != null ? game.getNameKor() : "";
            double ratePredictCalc = (double) Math.round((item.getPredictedRate() * 100) / 100);
            if (ratePredictCalc < 3.0f) {
                continue;
            }
            arr.add(GameRecommendDTO.GameListResponse.builder()
                    .id(item.getGameId())
                    .thumbnail(game.getThumbnail())
                    .name(game.getName()) // 영어 이름
                    .nameKor(titleKor) // 한글 이름 있다면
                    .category(game.getCategory())
                    .averageRate(game.getAverageRate())
                    .predictedRate(ratePredictCalc)
                    .usersRated(game.getUsersRated())
//                    .rank(item.getRank())
                    .predictedRank(item.getRank())
                    .build());
        }
        return Response.newResult(HttpStatus.OK, "추천 결과를 불러옵니다.", arr);
    }

    public ResponseEntity findGameRecommendationsByUserId(int userId) {
        List<GameRecommend> recommendList = gameRecommendRepository.findGameRecommendsByUserIdOrderByRank(userId);
        ArrayList<GameRecommendDTO.GameListResponse> arr = new ArrayList<>();
        for (GameRecommend item : recommendList) {
            System.out.println(item.getGameId());
            Game game = gameRepository.findGameById(item.getGameId()).get();
            String titleKor = game.getNameKor() != null ? game.getNameKor() : "";
            double ratePredictCalc = (double) Math.round((item.getPredictedRate() * 10) / 10.0);
            if (ratePredictCalc < 3.0f) {
                continue;
            }
            arr.add(GameRecommendDTO.GameListResponse.builder()
                    .id(item.getGameId())
                    .thumbnail(game.getThumbnail())
                    .name(game.getName()) // 영어 이름
                    .nameKor(titleKor) // 한글 이름 있다면
                    .category(game.getCategory())
                    .averageRate(game.getAverageRate())
                    .predictedRate(ratePredictCalc)
                    .usersRated(game.getUsersRated())
//                    .rank(item.getRank())
                    .predictedRank(item.getRank())
                    .build());
        }
        return Response.newResult(HttpStatus.OK, "추천 결과를 불러옵니다.", arr);
    }
}