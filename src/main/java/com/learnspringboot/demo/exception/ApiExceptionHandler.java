package com.learnspringboot.demo.exception;

import com.learnspringboot.demo.dto.MessageResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

import org.springframework.security.access.AccessDeniedException;
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

    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public MessageResponse handleAccessDeniedException(@NotNull Exception ex, WebRequest request){
        return new MessageResponse(ex.getMessage());
    }
}
