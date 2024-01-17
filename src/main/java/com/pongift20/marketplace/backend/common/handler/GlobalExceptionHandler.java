package com.pongift20.marketplace.backend.common.handler;

import com.pongift20.marketplace.backend.common.response.CustomErrorResponse;
import com.pongift20.marketplace.backend.common.response.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 파라메터 유효성 검사 실패 시 핸들러 (@Valid)
     *
     * @param ex
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        return new ResponseEntity<>(errors, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
    * 커스텀 예외 처리 핸들러(ResponseCode 코드 정의)
    */
    @ExceptionHandler(value = { CustomException.class })
    public ResponseEntity<CustomErrorResponse> handleCustomException(CustomException e) {
        log.error("handleCustomException throw CustomException : {}", e.getErrorCode());
        return CustomErrorResponse.toResponse(e.getErrorCode());
    }
}
