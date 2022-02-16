package com.sayan.webclient.controllers;

import com.sayan.webclient.models.LoginModel;
import com.sayan.webclient.models.UserModel;
import com.sayan.webclient.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage(@CookieValue(value = "token", defaultValue = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXlhbiIsImV4cCI6MTY0NDk0NzMwNywiaWF0IjoxNjQ0OTQ2MTA3fQ.Qzp-3jAGF0QHj_i_kmRCGg0eobcRa3HfA-JugRrNhkELsQVfwXcexaGNVwf28Khx59Ket66jjCMPETO5693tJg") String token){
        String s = "";
        return "login";
    }
    @GetMapping("/dashboard")
    public String dashboardPage(@CookieValue(value = "token", defaultValue = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXlhbiIsImV4cCI6MTY0NDk0NzMwNywiaWF0IjoxNjQ0OTQ2MTA3fQ.Qzp-3jAGF0QHj_i_kmRCGg0eobcRa3HfA-JugRrNhkELsQVfwXcexaGNVwf28Khx59Ket66jjCMPETO5693tJg") String token){
        String s = "";
        return "dashboard";
    }
    @GetMapping("/register")
    public String register(){
        String s = "";
        return "registration";
    }
    @GetMapping("/resetPassword")
    public String resetPassword(){
        String s = "";
        return "reset-password";
    }

    @PostMapping("/doLogin")
    public String doLogin(
            LoginModel loginModel,
            @CookieValue(value = "token", defaultValue = "test") String token){
        String s = "";
        System.out.println(loginModel);
        return "redirect:dashboard";
    }

    @GetMapping("/userDetails")
    public String getUserDetails(@CookieValue(value = "token", defaultValue = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXlhbiIsImV4cCI6MTY0NDk0NzMwNywiaWF0IjoxNjQ0OTQ2MTA3fQ.Qzp-3jAGF0QHj_i_kmRCGg0eobcRa3HfA-JugRrNhkELsQVfwXcexaGNVwf28Khx59Ket66jjCMPETO5693tJg") String token){
        UserModel userDetails = userService.getUserDetails(token);
        return userDetails.toString();
    }
}
