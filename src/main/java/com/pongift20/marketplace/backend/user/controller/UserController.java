package com.pongift20.marketplace.backend.user.controller;

import com.pongift20.marketplace.backend.user.model.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/info")
    public ResponseEntity<UserDto> info(Authentication authentication) {
        UserDto userDto = (UserDto) authentication.getDetails();
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
