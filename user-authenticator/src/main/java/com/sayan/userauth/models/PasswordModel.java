package com.sayan.userauth.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PasswordModel {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
