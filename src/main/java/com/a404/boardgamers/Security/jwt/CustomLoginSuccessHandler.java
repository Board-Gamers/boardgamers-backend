package com.a404.boardgamers.Security.jwt;

import com.a404.boardgamers.User.Domain.Entity.User;
import com.a404.boardgamers.Security.service.UserDetailsImpl;
import com.a404.boardgamers.User.Service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Resource
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        final User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
        final String token = TokenUtils.generateJwtToken(user);
        response.addHeader(AuthConstants.AUTH_HEADER, AuthConstants.TOKEN_TYPE + " " + token);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        response.getWriter().printf("{\"nickname\":\"%s\",\"isAdmin\":%s}", user.getNickname(), user.isAdmin());
        userService.addAchievement(user.getId(), 11, 1);
    }
}