package com.sayan.webclient.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserModel {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
