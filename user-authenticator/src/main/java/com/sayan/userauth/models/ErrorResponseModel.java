package com.sayan.userauth.models;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ErrorResponseModel {
    private String timestamp;
    private Integer status;
    private String error;
    private String message;
}
