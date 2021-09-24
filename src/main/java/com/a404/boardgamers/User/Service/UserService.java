package com.a404.boardgamers.User.Service;

import com.a404.boardgamers.Review.Domain.Entity.Review;
import com.a404.boardgamers.Review.Domain.Repository.ReviewRepository;
import com.a404.boardgamers.User.DTO.UserDTO;
import com.a404.boardgamers.User.Domain.Entity.User;
import com.a404.boardgamers.User.Domain.Repository.UserRepository;
import com.a404.boardgamers.Util.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final BCryptPasswordEncoder passwordEncoder;

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
    public ResponseEntity<Response> updateInfo(String userId, UserDTO.updateInfoDTO requestDTO) {
        Optional<User> optionalUser = userRepository.findUserByLoginId(userId);
        if (!optionalUser.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않는 아이디입니다.", null);
        }
        User user = optionalUser.get();
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
        UserDTO.userProfile profile = new UserDTO.userProfile(user.getNickname());
        return Response.newResult(HttpStatus.OK, nickname + "유저의 정보를 출력합니다.", profile);
    }

    public ResponseEntity<Response> getReviewByNickname(String nickname) {
        Optional<User> optionalUser = userRepository.findUserByNickname(nickname);
        if (!optionalUser.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다.", null);
        }
        User user = optionalUser.get();
        if (user.isWithdraw()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "탈퇴한 유저입니다.", null);
        }
        List<Review> reviewList = reviewRepository.findByUserNickname(nickname);
        if (reviewList.size() == 0) {
            return Response.newResult(HttpStatus.OK, "작성한 리뷰가 없습니다.", null);
        }
        return Response.newResult(HttpStatus.OK, nickname + "유저가 작성한 리뷰를 출력합니다.", reviewList);
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

}