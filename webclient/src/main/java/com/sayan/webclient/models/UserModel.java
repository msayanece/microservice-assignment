package com.sayan.webclient.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private String username;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
