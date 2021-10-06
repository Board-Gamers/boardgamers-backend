package com.a404.boardgamers.Game.Service;

import com.a404.boardgamers.Game.DTO.GameDTO;
import com.a404.boardgamers.Game.DTO.GameRecommendDTO;
import com.a404.boardgamers.Game.Domain.Entity.Game;
import com.a404.boardgamers.Game.Domain.Entity.GameRecommend;
import com.a404.boardgamers.Game.Domain.Entity.GameSpecs;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        Game game = item.get();
        String titleKor = item.get().getNameKor() == null ? "" : item.get().getNameKor();
        List<String> categories = parsing(game.getCategory());
        List<String> playType = parsing(game.getPlayType());
        List<String> series = parsing(game.getSeries());
        List<String> designer = parsing(game.getSeries());
        List<String> artist = parsing(game.getArtist());
        List<String> publisher = parsing(game.getPublisher());

        GameDTO.GameDetailResponse gameDetail = GameDTO.GameDetailResponse.builder()
                .id(item.get().getId())
                .name(item.get().getName())
                .nameKor(titleKor)
                .thumbnail(item.get().getThumbnail())
                .image(item.get().getImage())
                .description(item.get().getDescription())
                .yearPublished(item.get().getYearPublished())
                .minPlayers(item.get().getMinPlayers())
                .maxPlayers(item.get().getMaxPlayers())
                .minPlayTime(item.get().getMinPlayTime())
                .minAge(item.get().getMinAge())
                .category(categories)
                .playType(playType)
                .series(series)
                .designer(designer)
                .artist(artist)
                .publisher(publisher)
                .usersRated(item.get().getUsersRated())
                .averageRate(item.get().getAverageRate())
                .rank(item.get().getRank())
                .build();

        return Response.newResult(HttpStatus.OK, "게임 정보를 불러왔습니다.", gameDetail);
    }

    public ResponseEntity<Response> findAll(String order, int page, int pageSize) {
        HashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        PageRequest pageRequest = order.equals("usersRated") ? PageRequest.of(page - 1, pageSize, Sort.by(order).descending()) : PageRequest.of(page - 1, pageSize, Sort.by(order));
        Page<Game> pageList = gameRepository.findAll(pageRequest);
        linkedHashMap.put("totalPageItemCnt", pageList.getTotalElements());
        linkedHashMap.put("totalPage", pageList.getTotalPages());
        linkedHashMap.put("nowPage", pageList.getNumber());
        linkedHashMap.put("nowPageSize", pageList.getNumberOfElements());
        List<Game> gameList = pageList.getContent();
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
                    .minAge(item.getMinAge())
                    .minPlayers(item.getMinPlayers())
                    .maxPlayers(item.getMaxPlayers())
                    .minPlayTime(item.getMinPlayTime())
                    .maxPlayTime(item.getMaxPlayTime())
                    .build());
        }
        linkedHashMap.put("games", arr);
        return Response.newResult(HttpStatus.OK, "전체 게임을 검색합니다.", linkedHashMap);
    }

    public ResponseEntity<Response> findGamesWithFilter(Map<GameSpecs.SearchKey, Object> search, String order, int page, int pageSize) {
        Map<GameSpecs.SearchKey, Object> searchKeys = new HashMap<>();
        for (GameSpecs.SearchKey key : search.keySet()) {
            searchKeys.put(key, search.get(key));
        }

        HashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        PageRequest pageRequest = order.equals("usersRated") ? PageRequest.of(page - 1, pageSize, Sort.by(order).descending()) : PageRequest.of(page - 1, pageSize, Sort.by(order));
        Page<Game> pageList = gameRepository.findAll(GameSpecs.searchWith(searchKeys), pageRequest);

        linkedHashMap.put("totalPageItemCnt", pageList.getTotalElements());
        linkedHashMap.put("totalPage", pageList.getTotalPages());
        linkedHashMap.put("nowPage", pageList.getNumber());
        linkedHashMap.put("nowPageSize", pageList.getNumberOfElements());
        List<Game> gameList = pageList.getContent();
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
                    .minAge(item.getMinAge())
                    .minPlayers(item.getMinPlayers())
                    .maxPlayers(item.getMaxPlayers())
                    .minPlayTime(item.getMinPlayTime())
                    .maxPlayTime(item.getMaxPlayTime())
                    .build());
        }
        linkedHashMap.put("games", arr);

        return Response.newResult(HttpStatus.OK, "검색 결과를 가져옵니다.", linkedHashMap);
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
                    .minAge(game.getMinAge())
                    .minPlayers(game.getMinPlayers())
                    .maxPlayers(game.getMaxPlayers())
                    .minPlayTime(game.getMinPlayTime())
                    .maxPlayTime(game.getMaxPlayTime())
                    .predictedRank(item.getRank())
                    .build());
        }
        return Response.newResult(HttpStatus.OK, "추천 결과를 불러옵니다.", arr);
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

    public List<String> parsing(String string) {
        List<String> result = new ArrayList<>();
        int from = string.indexOf("'", 0);
        int to = string.indexOf("'", from + 1);
        while (from != -1 && to != -1) {
            String piece = string.substring(from + 1, to);
            System.out.println(from + " " + to + ": " + piece);
            result.add(piece);
            from = string.indexOf("'", to + 1);
            to = string.indexOf("'", from + 1);
        }
        return result;
    }
}