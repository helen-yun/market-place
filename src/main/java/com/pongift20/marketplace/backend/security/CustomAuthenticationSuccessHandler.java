package com.pongift20.marketplace.backend.security;

import com.pongift20.marketplace.backend.user.model.dto.UserDto;
import com.pongift20.marketplace.backend.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        UserDto userDto = (UserDto) authentication.getDetails();
        PrintWriter writer = response.getWriter();
        writer.write(JsonUtils.toJson(userDto));
        writer.flush();
    }
}
