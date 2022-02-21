package com.sayan.webclient.controllers;

import com.sayan.webclient.services.CookieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

import static com.sayan.webclient.util.Constants.ACCESS_TOKEN;

@ControllerAdvice
public class WebControllerAdvice {

    @Autowired
    private CookieService cookieService;

    @ExceptionHandler(value = HttpClientErrorException.class)
    public String httpClientErrorException(HttpClientErrorException exception,
                                           HttpServletResponse response) {
        if (exception.getRawStatusCode() == 403 || exception.getRawStatusCode() == 401){
            cookieService.addCookie(response, ACCESS_TOKEN, null, "/");
            return "redirect:login";
        }
        return null;
    }
}
