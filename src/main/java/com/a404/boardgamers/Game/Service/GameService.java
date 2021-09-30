package com.a404.boardgamers.Game.Service;

import com.a404.boardgamers.Game.DTO.GameDTO;
import com.a404.boardgamers.Game.DTO.GameRecommendDTO;
import com.a404.boardgamers.Game.Domain.Entity.Game;
import com.a404.boardgamers.Game.Domain.Entity.GameRecommend;
import com.a404.boardgamers.Game.Domain.Repository.GameRecommendRepository;
import com.a404.boardgamers.Game.Domain.Repository.GameRepository;
import com.a404.boardgamers.Review.Domain.Repository.ReviewDataRepository;
import com.a404.boardgamers.User.Domain.Entity.Favorite;
import com.a404.boardgamers.User.Domain.Entity.User;
import com.a404.boardgamers.User.Domain.Repository.FavoriteRepository;
import com.a404.boardgamers.User.Domain.Repository.UserRepository;
import com.a404.boardgamers.Util.Response;
import com.a404.boardgamers.Util.TokenExtraction;
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
    private final ReviewDataRepository reviewDataRepository;
    private final FavoriteRepository favoriteRepository;

    public ResponseEntity<Response> getGameInformation(int id) {
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
                .thumbnail(item.get().getThumbnail())
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
                .designer(item.get().getDesigner())
                .artist(item.get().getArtist())
                .publisher(item.get().getPublisher())
                .playType(item.get().getPlayType())
                .series(item.get().getSeries())
                .build();

        return Response.newResult(HttpStatus.OK, "게임 정보를 불러왔습니다.", game);
    }

    public ResponseEntity<Response> findGamesByKeyword(String keyword, int page, int pageSize) {

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
                    .id(item.getId())
                    .thumbnail(item.getThumbnail())
                    .image(item.getImage())
                    .name(item.getName())
                    .nameKor(titleKor)
                    .category(item.getCategory())
                    .averageRate(item.getAverageRate())
                    .usersRated(item.getUsersRated())
                    .rank(item.getRank())
                    .build());
        }
        linkedHashMap.put("games", arr);

        return Response.newResult(HttpStatus.OK, keyword + "로 검색한 게임 정보입니다.", linkedHashMap);
    }

    public ResponseEntity<Response> findAllGames(int page, int pageSize) {
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
                    .image(item.getImage())
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

    public ResponseEntity<Response> findGamesByCategory(String category, int page, int pageSize) {
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
                    .image(item.getImage())
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

    public ResponseEntity<Response> findGameRecommendationsByUserId(HttpServletRequest httpServletRequest) {
        String userId = TokenExtraction.getLoginId(httpServletRequest);
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
                    .image(game.getImage())
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

    public ResponseEntity<Response> findGameRecommendationsByUserId(int userId) {
        List<GameRecommend> recommendList = gameRecommendRepository.findGameRecommendsByUserIdOrderByRank(userId);
        ArrayList<GameRecommendDTO.GameListResponse> arr = new ArrayList<>();
        for (GameRecommend item : recommendList) {
            Game game = gameRepository.findGameById(item.getGameId()).get();
            String titleKor = game.getNameKor() != null ? game.getNameKor() : "";
            double ratePredictCalc = (double) Math.round((item.getPredictedRate() * 10) / 10.0);
            if (ratePredictCalc < 3.0f) {
                continue;
            }
            arr.add(GameRecommendDTO.GameListResponse.builder()
                    .id(item.getGameId())
                    .thumbnail(game.getThumbnail())
                    .image(game.getImage())
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

    public ResponseEntity findGamesOrderByReview(int page, int pageSize) {
        long totalItemCount = gameRepository.countAll();
        HashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("totalPageItemCnt", totalItemCount);
        linkedHashMap.put("totalPage", ((totalItemCount - 1) / pageSize) + 1);
        linkedHashMap.put("nowPage", page);
        linkedHashMap.put("nowPageSize", pageSize);
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        List<Object[]> gamesOrderByReview = reviewDataRepository.findGamesOrderByReviewCnt(pageRequest);
        ArrayList<GameDTO.GameListResponse> arr = new ArrayList<>();
        for (Object[] objects : gamesOrderByReview) {
            Game item = gameRepository.getById((int) objects[0]);
            arr.add(
                    GameDTO.GameListResponse.builder()
                            .id(item.getId())
                            .name(item.getName())
                            .nameKor(item.getNameKor())
                            .thumbnail(item.getThumbnail())
                            .image(item.getImage())
                            .category(item.getImage())
                            .rank(item.getRank())
                            .usersRated(item.getUsersRated())
                            .averageRate(item.getAverageRate())
                            .build()
            );
        }
        /*List<Game> gameList = gameRepository.findAllGamesOrderByRank(pageRequest);

        for (Game item : gameList) {
            arr.add(
                    GameDTO.GameListResponse.builder()
                            .id(item.getId())
                            .name(item.getName())
                            .nameKor(item.getNameKor())
                            .thumbnail(item.getThumbnail())
                            .image(item.getImage())
                            .category(item.getImage())
                            .rank(item.getRank())
                            .usersRated(item.getUsersRated())
                            .averageRate(item.getAverageRate())
                            .build()
            );
        }*/
        linkedHashMap.put("games", arr);

        return Response.newResult(HttpStatus.OK, "", linkedHashMap);
    }

    public ResponseEntity<Response> addFavorite(String userId, int gameId) {
        Optional<User> optionalUser = userRepository.findUserByLoginId(userId);
        if (!optionalUser.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다.", null);
        }
        Optional<Game> optionalGame = gameRepository.findGameById(gameId);
        if (!optionalGame.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않는 게임입니다.", null);
        }
        Optional<Favorite> optionalFavorite = favoriteRepository.findAllByUserIdAndGameId(userId, gameId);
        if (!optionalFavorite.isPresent()) {
            Favorite favorite = new Favorite(userId, gameId);
            favoriteRepository.save(favorite);
            return Response.newResult(HttpStatus.OK, "즐겨찾기가 등록되었습니다.", null);
        }
        Favorite favorite = optionalFavorite.get();
        favoriteRepository.delete(favorite);
        return Response.newResult(HttpStatus.OK, "즐겨찾기가 해제되었습니다.", null);
    }
}