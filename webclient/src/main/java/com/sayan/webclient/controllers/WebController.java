package com.sayan.webclient.controllers;

import com.sayan.webclient.models.*;
import com.sayan.webclient.services.CookieService;
import com.sayan.webclient.services.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.sayan.webclient.util.Constants.ACCESS_TOKEN;
import static com.sayan.webclient.util.Constants.USER_NAME;

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
    public String dashboardPage(@CookieValue(value = ACCESS_TOKEN, defaultValue = "") String token,
                                Model model){
        if(StringUtils.isEmpty(token)) return "redirect:login";

        UserModel userDetails = userService.getUserDetails(token);
        if (userDetails == null) {
            return "redirect:login";
        }
        model.addAttribute("user", userDetails);
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
            cookieService.addCookie(response, ACCESS_TOKEN, null, "/");
            return "redirect:login";
        }else {
            cookieService.addCookie(response, ACCESS_TOKEN, jwt, "/");
            return "redirect:dashboard";
        }
    }
    @PostMapping("/doResetPassword")
    public String doResetPassword(ResetPasswordModel resetPasswordModel, Model model,
                                  HttpServletResponse response){
        System.out.println(resetPasswordModel);
        if(userService.initiateForgotPassword(resetPasswordModel)){
            cookieService.addCookie(response, USER_NAME, resetPasswordModel.getUsername(), "/");
            return "redirect:newPassword";
        }else {
            return "redirect:resetPassword";
        }
    }
    @PostMapping("/addNewPassword")
    public String addNewPassword(@CookieValue(value = USER_NAME, defaultValue = "") String username,
                                 PasswordModel passwordModel, HttpServletResponse response){
        passwordModel.setUsername(username);
        System.out.println(passwordModel);
        String jwt = userService.resetPassword(passwordModel);
        if (jwt == null){
            cookieService.addCookie(response, ACCESS_TOKEN, null, "/");
            return "redirect:login";
        }else {
            cookieService.addCookie(response, ACCESS_TOKEN, jwt, "/");
            return "redirect:dashboard";
        }
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
            @CookieValue(value = ACCESS_TOKEN, defaultValue = "") String token){
        logger.info(updateUserModel.toString());
        return userService.updateUser(token, updateUserModel);
    }

    @GetMapping("/userDetails")
    public String getUserDetails(@CookieValue(value = ACCESS_TOKEN, defaultValue = "") String token){
        UserModel userDetails = userService.getUserDetails(token);
        return userDetails.toString();
    }

    @GetMapping("/logout")
    public String logout(@CookieValue(value = ACCESS_TOKEN, defaultValue = "") String token, HttpServletRequest request,
                         HttpServletResponse response){
        userService.doLogout(token);
        cookieService.addCookie(response, ACCESS_TOKEN, null, "/");
        return "redirect:login";
    }
}
