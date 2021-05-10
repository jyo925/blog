package com.cos.blog.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice //어디에서든 예외가 발생했을 때 이쪽으로 오게 하기
@RestController
public class GlobalExceptionHandler {

    //IllegalArgumentException이 발생하면 그 에러를 스프링이 이 함수로 전달
    @ExceptionHandler(value = IllegalArgumentException.class)
    public String handleArgumentException(IllegalArgumentException e) {
        return "<h1>" + e.getMessage() + "</h1>";
    }
    
}
