package com.a404.boardgamers.User.Controller;

import com.a404.boardgamers.User.DTO.UserDTO;
import com.a404.boardgamers.User.Service.UserService;
import com.a404.boardgamers.Util.Response;
import com.a404.boardgamers.Util.TokenToId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/signup")
    public ResponseEntity<Response> signUp(@RequestBody UserDTO.signUpDTO requestDTO) {
        return userService.signUp(requestDTO);
    }

    @PutMapping
    public ResponseEntity<Response> updateInfo(HttpServletRequest request, @RequestBody UserDTO.updateInfoDTO requestDTO) {
        String userId = TokenToId.getId(request);
        if (userId == null) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요.", null);
        }
        return userService.updateInfo(userId, requestDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Response> changePassword(HttpServletRequest request, @PathVariable String id, @RequestBody UserDTO.changePasswordDTO requestDTO) {
        String userId = TokenToId.getId(request);
        if (userId == null) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요.", null);
        }
        if (!userId.equals(id)) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "본인의 비밀번호만 수정할 수 있습니다.", null);
        }
        return userService.changePassword(userId, requestDTO);
    }
}