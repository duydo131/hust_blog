package com.learnspringboot.demo.exception;

import com.learnspringboot.demo.dto.MessageResponse;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public MessageResponse handleAllException(Exception ex, WebRequest request) {
        return new MessageResponse(ex.getMessage());
    }
}
