package com.game.controller;

import com.game.exceptions.ExceptionsBAD_REQUEST;
import com.game.exceptions.ExceptionsNOT_FOUND;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PlayerControllerAdvice {

    @ResponseBody
    @ExceptionHandler(ExceptionsBAD_REQUEST.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String badRequest(ExceptionsBAD_REQUEST exceptions) {
        return exceptions.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(ExceptionsNOT_FOUND.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFound(ExceptionsNOT_FOUND exceptions) {
        return exceptions.getMessage();
    }
}