package com.sayan.userauth.controlleradvice;

import com.sayan.userauth.exceptions.UserNotFoundException;
import com.sayan.userauth.models.ErrorResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@ControllerAdvice
public class GlobalControllerAdvice {
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<Object> exception(HttpMessageNotReadableException exception) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        ErrorResponseModel response = ErrorResponseModel.builder()
                .error("Bad request")
                .timestamp(sdf2.format(timestamp))
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Kindly check request body.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
