package com.sayan.webclient.controllers;

import com.sayan.webclient.models.UserModel;
import com.sayan.webclient.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web")
public class WebController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String getUserDetails(@CookieValue(value = "token", defaultValue = "xyz") String token){
        UserModel userDetails = userService.getUserDetails(token);
        return userDetails.toString();
    }
}
