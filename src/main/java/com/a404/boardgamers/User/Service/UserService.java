package com.a404.boardgamers.User.Service;

import com.a404.boardgamers.Game.Domain.Entity.Game;
import com.a404.boardgamers.Game.Domain.Repository.GameRepository;
import com.a404.boardgamers.Review.DTO.ReviewDTO;
import com.a404.boardgamers.Review.Domain.Entity.Review;
import com.a404.boardgamers.Review.Domain.Repository.ReviewRepository;
import com.a404.boardgamers.User.DTO.UserAcheivementDTO;
import com.a404.boardgamers.User.DTO.UserDTO;
import com.a404.boardgamers.User.Domain.Entity.Achievement;
import com.a404.boardgamers.User.Domain.Entity.Favorite;
import com.a404.boardgamers.User.Domain.Entity.User;
import com.a404.boardgamers.User.Domain.Entity.UserAchievement;
import com.a404.boardgamers.User.Domain.Repository.AchievementRepository;
import com.a404.boardgamers.User.Domain.Repository.FavoriteRepository;
import com.a404.boardgamers.User.Domain.Repository.UserAchievementRepository;
import com.a404.boardgamers.User.Domain.Repository.UserRepository;
import com.a404.boardgamers.Util.Response;
import com.a404.boardgamers.Util.TimestampToDateString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final FavoriteRepository favoriteRepository;
    private final GameRepository gameRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;

    private Map<Integer, Integer> categoryMap = new HashMap<Integer, Integer>() {
        {
            put(1, 1);
            put(5, 2);
            put(10, 3);
            put(20, 4);
            put(50, 7);
            put(100, 8);
        }
    };

    @Transactional
    public ResponseEntity<Response> signUp(UserDTO.signUpDTO requestDTO) {
        String id = requestDTO.getId();
        String nickname = requestDTO.getNickname();
        if (userRepository.findUserByLoginId(id).isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다.", null);
        }
        if (userRepository.findUserByNickname(nickname).isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다.", null);
        }
        requestDTO.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        User user = User.builder()
                .loginId(id)
                .nickname(nickname)
                .password(requestDTO.getPassword())
                .build();
        userRepository.save(user);
        return Response.newResult(HttpStatus.OK, "회원가입이 완료되었습니다.", null);
    }

    @Transactional
    public ResponseEntity<Response> updateInfo(String userId, UserDTO.userProfileDTO requestDTO) {
        Optional<User> optionalUser = userRepository.findUserByLoginId(userId);
        if (!optionalUser.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않는 아이디입니다.", null);
        }
        User user = optionalUser.get();
        Optional<User> checkNickUser = userRepository.findUserByNickname(requestDTO.getNickname());
        if (checkNickUser.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다.", null);
        }
        user.updateInfo(requestDTO.getNickname(), requestDTO.getAge(), requestDTO.getGender());
        return Response.newResult(HttpStatus.OK, "회원정보가 수정되었습니다.", null);
    }

    @Transactional
    public ResponseEntity<Response> changePassword(String userId, UserDTO.changePasswordDTO requestDTO) {
        Optional<User> optionalUser = userRepository.findUserByLoginId(userId);
        if (!optionalUser.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않는 아이디입니다.", null);
        }
        User user = optionalUser.get();
        if (!passwordEncoder.matches(requestDTO.getPassword(), user.getPassword())) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.", null);
        }
        user.changePassword(passwordEncoder.encode(requestDTO.getNewPassword()));
        return Response.newResult(HttpStatus.OK, "비밀번호가 변경되었습니다.", null);
    }

    public ResponseEntity<Response> getProfile(String nickname) {
        Optional<User> optionalUser = userRepository.findUserByNickname(nickname);
        if (!optionalUser.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다.", null);
        }
        User user = optionalUser.get();
        if (user.isWithdraw()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "탈퇴한 유저입니다.", null);
        }
        UserDTO.userProfileDTO profile = new UserDTO.userProfileDTO(user.getNickname(), user.getAge(), user.getGender());
        return Response.newResult(HttpStatus.OK, nickname + " 유저의 정보를 출력합니다.", profile);
    }

    public ResponseEntity<Response> getReviewByNickname(String nickname, int page, int pageSize) {
        Optional<User> optionalUser = userRepository.findUserByNickname(nickname);
        if (!optionalUser.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다.", null);
        }
        User user = optionalUser.get();
        if (user.isWithdraw()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "탈퇴한 유저입니다.", null);
        }

        int totalItemCount = userRepository.countAllByNickname(nickname);
        if (totalItemCount == 0) {
            return Response.newResult(HttpStatus.OK, "작성한 리뷰가 없습니다.", null);
        }

        HashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("totalPage", ((totalItemCount - 1) / pageSize) + 1);
        linkedHashMap.put("nowPage", page);
        linkedHashMap.put("nowPageSize", pageSize);
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);

        List<Review> reviewList = reviewRepository.findByUserNickname(nickname, pageRequest);
        ArrayList<ReviewDTO.ReviewDetailResponse> list = new ArrayList<>();

        for (Review item : reviewList) {
            Game game = gameRepository.findGameById(item.getGameId()).get();
            list.add(ReviewDTO.ReviewDetailResponse.builder()
                    .id(item.getId())
                    .userId(item.getUserId())
                    .gameId(item.getGameId())
                    .gameName(item.getGameName())
                    .gameNameKor(game.getNameKor())
                    .userNickname(item.getUserNickname())
                    .comment(item.getComment())
                    .rating(item.getRating())
                    .createdAt(TimestampToDateString.convertDate(item.getCreatedAt()))
                    .build());
        }
        linkedHashMap.put("reviews", list);
        return Response.newResult(HttpStatus.OK, nickname + "유저가 작성한 리뷰를 출력합니다.", linkedHashMap);
    }

    @Transactional
    public ResponseEntity<Response> deleteUser(String userId) {
        Optional<User> optionalUser = userRepository.findUserByLoginId(userId);
        if (!optionalUser.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다.", null);
        }
        userRepository.delete(optionalUser.get());
        return Response.newResult(HttpStatus.OK, "회원탈퇴가 완료되었습니다.", null);
    }

    public ResponseEntity<Response> getAchievement(String nickname) {
        User user = userRepository.findUserByNickname(nickname).get();

        List<UserAchievement> userAchievements = userAchievementRepository.findUserAchievementsByUserId(user.getId());
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        ArrayList<UserAcheivementDTO.AchievementRecordResponse> record = new ArrayList<>();
        ArrayList<UserAcheivementDTO.AchievementResponse> arr = new ArrayList<>();
        ArrayList<UserAcheivementDTO.AchievementResponse> categories = new ArrayList<>();
        for (UserAchievement item : userAchievements) {
            Achievement achievement = achievementRepository.findAchievementById(item.getAchievementId()).get();
            if (achievement.getId() >= 12) {
                categories.add(UserAcheivementDTO.AchievementResponse.builder()
                        .id(item.getId())
                        .title(achievement.getDetail())
                        .detail(item.getDetail() + "")
                        .date(TimestampToDateString.convertDate(item.getAchievedAt()))
                        .build()
                );
            } else if (achievement.getId() >= 9) {
                record.add(UserAcheivementDTO.AchievementRecordResponse.builder()
                        .id(item.getAchievementId())
                        .title(achievement.getTitle())
                        .detail(achievement.getDetail())
                        .date(TimestampToDateString.convertDate(item.getAchievedAt()))
                        .count(item.getDetail())
                        .build()
                );
            } else {
                arr.add(UserAcheivementDTO.AchievementResponse.builder()
                        .id(item.getAchievementId())
                        .title(achievement.getTitle())
                        .detail(achievement.getDetail())
                        .date(TimestampToDateString.convertDate(item.getAchievedAt()))
                        .build()
                );
            }
        }
        linkedHashMap.put("award", arr);
        linkedHashMap.put("badges", record);
        linkedHashMap.put("conquered", categories);
        linkedHashMap.put("percent", (double) (categories.size() / 84.0) * 100.0);
        return Response.newResult(HttpStatus.OK, "유저의 달성 목록을 불러옵니다.", linkedHashMap);
    }

    @Transactional
    public void addAchievement(int userId, int type, int count) {
        Optional<UserAchievement> optUserAchievement = userAchievementRepository.findUserAchievementByUserIdAndAchievementId(userId, getAchievementId(type));
        log.error("사용자 ID : " + userId + " 받은 : " + type + ", 변환한 타입 " + getAchievementId(type) + " cnt " + count);
        if (type == 1) {
            // 리뷰 개수 관련 - 사용자가 남긴 리뷰 갯수가 achievement에 들어있는 것과 일치하는 경우
            // 1, 5, 10, 20, 50, 100
            userAchievementRepository.save(UserAchievement.builder()
                    .userId(userId)
                    .achievementId(categoryMap.get(count))
                    .build());
        } else if (!optUserAchievement.isPresent()) {
            // 없으면 save
            userAchievementRepository.save(UserAchievement.builder()
                    .userId(userId)
                    .achievementId(getAchievementId(type))
                    .detail(count)
                    .build());
        } else {
            // 있으면 내용 갱신.
            switch (type) {
                case 0:
                    // 로그인 관련 처리 ( 연속 로그인 처리를 어떻게 하지?) detail 에 뭘 넣어야 할지 고민해보기.
                    break;
                case 2:
                    // 질문 개수 얘는 매번 갱신하기.
                    break;
                case 3:
                    // 질문에 답변 단 갯수 얘도 매번 갱신하기.
                    optUserAchievement.get().update(count + 1);
                    break;
                case 4:
                    // 리뷰 남길 때마다 몇 개의 카테고리를 해봤는지 남기기.
                    break;
                default:
                    // 카테고리 갱신
                    optUserAchievement.get().update(optUserAchievement.get().getDetail() + 1);
                    break;
            }
        }
    }

    public int getAchievementId(int type) {
        int answer = 0;
        switch (type) {
            case 0:
                // 로그인 한 경우
                answer = 0;
                break;
            case 1:
                // 리뷰 단 경우 <- 리뷰 갯수 따라서 달라짐.
                answer = 1;
                break;
            case 2:
                // 질문한 경우
                answer = 9;
                break;
            case 3:
                // 답변 한 경우
                answer = 10;
                break;
            default:
                answer = type;
                break;
        }
        return answer;
    }

    public ResponseEntity<Response> getFavorites(String nickname, int page, int pageSize) {
        Optional<User> optionalUser = userRepository.findUserByNickname(nickname);
        if (!optionalUser.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않은 유저입니다.", null);
        }
        User user = optionalUser.get();

        HashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        int totalPageItemCnt = favoriteRepository.countByUserId(user.getLoginId());
        linkedHashMap.put("totalPage", ((totalPageItemCnt - 1) / pageSize) + 1);
        linkedHashMap.put("nowPage", page);
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        List<Favorite> favoriteList = favoriteRepository.findByUserId(user.getLoginId(), pageRequest);
        if (favoriteList.size() == 0) {
            return Response.newResult(HttpStatus.OK, "즐겨찾기한 게임이 없습니다.", null);
        }
        List<UserDTO.userFavoriteDTO> favoriteDTOList = new ArrayList<>();
        for (int i = 0; i < favoriteList.size(); i++) {
            Favorite favorite = favoriteList.get(i);
            Game game = gameRepository.findGameById(favorite.getGameId()).get();
            UserDTO.userFavoriteDTO userFavoriteDTO = UserDTO.userFavoriteDTO.builder()
                    .gameId(game.getId())
                    .thumbnail(game.getThumbnail())
                    .gameName(game.getName())
                    .gameNameKor(game.getNameKor())
                    .build();
            favoriteDTOList.add(userFavoriteDTO);
        }

        linkedHashMap.put("list", favoriteDTOList);
        return Response.newResult(HttpStatus.OK, nickname + "유저의 즐겨찾기 목록입니다.", linkedHashMap);
    }
}