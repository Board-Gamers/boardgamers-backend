package com.a404.boardgamers.User.Controller;

import com.a404.boardgamers.User.DTO.UserDTO;
import com.a404.boardgamers.User.Service.UserService;
import com.a404.boardgamers.Util.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/signup")
    public ResponseEntity<Response> signUp(@RequestBody UserDTO.signUp userSaveRequest) {
        return userService.signUp(userSaveRequest);
    }
}