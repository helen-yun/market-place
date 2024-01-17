package com.pongift20.marketplace.backend.common.controller;

import com.pongift20.marketplace.backend.common.response.ErrorResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ErrorPageController implements ErrorController {
    @GetMapping("/error")
    public ResponseEntity<ErrorResponse> getError(HttpServletRequest request, HttpServletResponse response) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(System.currentTimeMillis())
                .domain(request.getServerName())
                .status(response.getStatus())
                .uid(request.getRemoteUser())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(response.getStatus()));
    }

    @PostMapping("/error")
    public ResponseEntity<ErrorResponse> postError(HttpServletRequest request, HttpServletResponse response) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(System.currentTimeMillis())
                .domain(request.getServerName())
                .status(response.getStatus())
                .uid(request.getRemoteUser())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(response.getStatus()));
    }
}
