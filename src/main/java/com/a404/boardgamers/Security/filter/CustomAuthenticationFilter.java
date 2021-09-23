package com.a404.boardgamers.Security.filter;

import com.a404.boardgamers.User.Domain.Entity.User;
import com.a404.boardgamers.Exception.InputNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public CustomAuthenticationFilter(final AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        final UsernamePasswordAuthenticationToken authRequest;
        try {
            final User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            if (user.getLoginId() == null || user.getPassword() == null) {
                throw new InputNotFoundException();
            }
            authRequest = new UsernamePasswordAuthenticationToken(user.getLoginId(), user.getPassword());
        } catch (IOException e) {
            throw new InputNotFoundException();
        }

        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}