package com.sayan.webclient.controllers;

import com.sayan.webclient.models.*;
import com.sayan.webclient.services.CookieService;
import com.sayan.webclient.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.sayan.webclient.util.Constants.ACCESS_TOKEN;

@Controller
public class WebController {

    private final Logger logger = LoggerFactory.getLogger(WebController.class);

    @Autowired
    private CookieService cookieService;
    @Autowired
    private UserService userService;

    //---------Pages------------

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }
    @GetMapping("/dashboard")
    public String dashboardPage(@CookieValue(value = ACCESS_TOKEN) String token){
        return "dashboard";
    }
    @GetMapping("/register")
    public String register(){
        return "registration";
    }
    @GetMapping("/resetPassword")
    public String resetPassword(){
        return "reset-password";
    }
    @GetMapping("/newPassword")
    public String newPassword(){
        return "new-password";
    }

    //-------------Actions---------------

    @PostMapping("/doLogin")
    public String doLogin(LoginModel loginModel, HttpServletResponse response){
        logger.info(loginModel.toString());
        String jwt = userService.login(loginModel);
        if (jwt == null){
            cookieService.addCookie(response, ACCESS_TOKEN, jwt, "/");
            return "redirect:login";
        }else {
            cookieService.addCookie(response, ACCESS_TOKEN, jwt, "/");
            return "redirect:dashboard";
        }
    }
    @PostMapping("/doResetPassword")
    public String doResetPassword(ResetPasswordModel resetPasswordModel){
        System.out.println(resetPasswordModel);
        return "redirect:newPassword";
    }
    @PostMapping("/addNewPassword")
    public String addNewPassword(AddNewPasswordModel addNewPasswordModel){
        System.out.println(addNewPasswordModel);
        return "redirect:dashboard";
    }
    @PostMapping("/doRegister")
    public String doRegister(
            UserModel userModel){
        logger.info(userModel.toString());
        if(userService.register(userModel)){
            return "redirect:login";
        }else {
            return "redirect:register";
        }
    }

    @PostMapping("/updateProfile")
    @ResponseBody
    public UserModel updateProfile(
            @RequestBody UpdateUserModel updateUserModel,
            @CookieValue(value = ACCESS_TOKEN) String token){
        System.out.println(updateUserModel);
        UserModel userModel = UserModel.builder()
                .firstName(updateUserModel.getFirstName())
                .lastName(updateUserModel.getLastName())
                .build();
        return userModel;
    }

    @GetMapping("/userDetails")
    public String getUserDetails(@CookieValue(value = ACCESS_TOKEN) String token){
        UserModel userDetails = userService.getUserDetails(token);
        return userDetails.toString();
    }

    @GetMapping("/logout")
    public String logout(@CookieValue(value = ACCESS_TOKEN, defaultValue = "") String token, HttpServletRequest request,
                         HttpServletResponse response){
//        final Cookie[] cookies = request.getCookies();
//        final String accessToken = cookieService.getCookieValueByName(cookies, ACCESS_TOKEN);
//        userService.doLogout(accessToken);
        cookieService.addCookie(response, ACCESS_TOKEN, null, "/");
        return "redirect:login";
    }
}
