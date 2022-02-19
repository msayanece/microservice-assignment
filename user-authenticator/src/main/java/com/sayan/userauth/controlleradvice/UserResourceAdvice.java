package com.sayan.userauth.controlleradvice;

import com.sayan.userauth.exceptions.UserNotFoundException;
import com.sayan.userauth.models.ErrorResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@ControllerAdvice
public class UserResourceAdvice {
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<Object> exception(UserNotFoundException exception) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        ErrorResponseModel response = ErrorResponseModel.builder()
                .error("User not found")
                .timestamp(sdf2.format(timestamp))
                .status(HttpStatus.NOT_FOUND.value())
                .message("User is null.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Object> exception(IllegalArgumentException exception) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        ErrorResponseModel response = ErrorResponseModel.builder()
                .error("Illegal data")
                .timestamp(sdf2.format(timestamp))
                .status(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
