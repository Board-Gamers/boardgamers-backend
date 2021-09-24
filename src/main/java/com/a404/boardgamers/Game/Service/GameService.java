package com.a404.boardgamers.Game.Service;

import com.a404.boardgamers.Game.Domain.Entity.Game;
import com.a404.boardgamers.Game.Domain.Repository.GameRepository;
import com.a404.boardgamers.Game.DTO.GameDTO;
import com.a404.boardgamers.Util.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class GameService {
    private final GameRepository gameRepository;

    public ResponseEntity<Response> getGameInformation(int id) {
        Optional<Game> item = gameRepository.findGameById(id);
        if (!item.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "불가능한 접근입니다.", null);
        }
        String titleKor = item.get().getNameKor() == null ? "" : item.get().getNameKor();
        GameDTO.GameDetailResponse game = GameDTO.GameDetailResponse.builder()
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
}