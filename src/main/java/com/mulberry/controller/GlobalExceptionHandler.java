package com.mulberry.controller;

import com.mulberry.common.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public R<Void> handleException(Exception e) {
        // handler different exceptions...
        return R.error(e.getMessage());
    }
}
