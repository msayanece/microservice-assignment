package com.sayan.webclient.controllers;

import com.sayan.webclient.models.*;
import com.sayan.webclient.services.CookieService;
import com.sayan.webclient.services.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.sayan.webclient.util.Constants.*;

@Controller
public class WebController {

    private final Logger logger = LoggerFactory.getLogger(WebController.class);

    @Autowired
    private CookieService cookieService;
    @Autowired
    private UserService userService;

    //---------Pages------------

    /**
     * home page redirects to the login page
     * @return
     */
    @GetMapping
    public String home(){
        return "redirect:login";
    }

    /**
     * Login page resource, if session available redirects to the dashboard page
     * @return page name
     */
    @GetMapping("/login")
    public String loginPage(@CookieValue(value = ACCESS_TOKEN, defaultValue = "") String token,
                            HttpServletResponse response){
        //disable back
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");   //for http1.1
        response.setHeader(HttpHeaders.PRAGMA, "no-cache");     //for http1.0
        response.setHeader(HttpHeaders.EXPIRES, "0");           //for proxies
        if (!StringUtils.isEmpty(token)){
            return "redirect:dashboard";
        }
        return "login";
    }
    /**
     * Dashboard page resource
     * @return page name
     */
    @GetMapping("/dashboard")
    public String dashboardPage(@CookieValue(value = ACCESS_TOKEN, defaultValue = "") String token,
                                Model model, HttpServletResponse response) throws HttpClientErrorException {
        //disable back
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");   //for http1.1
        response.setHeader(HttpHeaders.PRAGMA, "no-cache");     //for http1.0
        response.setHeader(HttpHeaders.EXPIRES, "0");           //for proxies

        if(StringUtils.isEmpty(token)) return "redirect:login";

        UserModel userDetails = userService.getUserDetails(token);
        if (userDetails == null) {
            return "redirect:login";
        }
        model.addAttribute("user", userDetails);
        return "dashboard";
    }
    /**
     * Registration page resource
     * @return page name
     */
    @GetMapping("/register")
    public String register(@CookieValue(value = ACCESS_TOKEN, defaultValue = "") String token){
        if (!StringUtils.isEmpty(token)){
            return "redirect:dashboard";
        }
        return "registration";
    }
    /**
     * Reset (forgot) Password page resource, used for validating username and 
     * then redirect to set new password page
     * @return page name
     */
    @GetMapping("/resetPassword")
    public String resetPassword(){
        return "reset-password";
    }
    /**
     * New Password page resource
     * @return page name
     */
    @GetMapping("/newPassword")
    public String newPassword(){
        return "new-password";
    }

    //-------------Actions---------------

    /**
     * Login action
     * @param loginModel holds login data
     * @param response http response
     * @param redirectAttributes for redirection
     * @return model and view object with page name and model value if required
     */
    @PostMapping("/doLogin")
    public ModelAndView doLogin(LoginModel loginModel, HttpServletResponse response,
                          RedirectAttributes redirectAttributes){
        logger.info(loginModel.toString());
        String jwt = userService.login(loginModel);
        if (jwt == null){
            cookieService.addCookie(response, ACCESS_TOKEN, null, "/");
            redirectAttributes.addFlashAttribute("error", USERNAME_PASSWORD_NOT_FOUND);
            RedirectView redirectView = new RedirectView("/login", true);
            return new ModelAndView(redirectView);
        }else if (jwt.equals(SERVER_BUSY)){
            cookieService.addCookie(response, ACCESS_TOKEN, null, "/");
            redirectAttributes.addFlashAttribute("error", SERVER_BUSY);
            RedirectView redirectView = new RedirectView("/login", true);
            return new ModelAndView(redirectView);
        }else {
            cookieService.addCookie(response, ACCESS_TOKEN, jwt, "/");
            RedirectView redirectView = new RedirectView("/dashboard", true);
            return new ModelAndView(redirectView);
        }
    }

    /**
     * Forgot password action - validate username and redirect
     * @param resetPasswordModel holds username data
     * @param response http response
     * @param redirectAttributes for redirection
     * @return model and view object with page name and model value if required
     */
    @PostMapping("/doResetPassword")
    public ModelAndView doResetPassword(ResetPasswordModel resetPasswordModel, HttpServletResponse response,
                                  RedirectAttributes redirectAttributes){
        logger.info(resetPasswordModel.toString());
        if(userService.initiateForgotPassword(resetPasswordModel)){
            cookieService.addCookie(response, USER_NAME, resetPasswordModel.getUsername(), "/");
            RedirectView redirectView = new RedirectView("/newPassword", true);
            return new ModelAndView(redirectView);
        }else {
            redirectAttributes.addFlashAttribute("error", USERNAME_NOT_FOUND);
            RedirectView redirectView = new RedirectView("/resetPassword", true);
            return new ModelAndView(redirectView);
        }
    }

    /**
     * reset password action - for a valid username
     * @param username cookie
     * @param passwordModel holds password data
     * @param response http response
     * @param redirectAttributes for redirection
     * @return model and view object with page name and model value if required
     */
    @PostMapping("/addNewPassword")
    public ModelAndView addNewPassword(@CookieValue(value = USER_NAME, defaultValue = "") String username,
                                 PasswordModel passwordModel, HttpServletResponse response,
                                       RedirectAttributes redirectAttributes){
        passwordModel.setUsername(username);
        logger.info(passwordModel.toString());
        String jwt = userService.resetPassword(passwordModel);
        if (jwt == null){
            cookieService.addCookie(response, ACCESS_TOKEN, null, "/");
            redirectAttributes.addFlashAttribute("error", USERNAME_PASSWORD_NOT_FOUND);
            RedirectView redirectView = new RedirectView("/login", true);
            return new ModelAndView(redirectView);
        }else if (jwt.equals(SERVER_BUSY)){
            cookieService.addCookie(response, ACCESS_TOKEN, null, "/");
            redirectAttributes.addFlashAttribute("error", SERVER_BUSY);
            RedirectView redirectView = new RedirectView("/login", true);
            return new ModelAndView(redirectView);
        }else {
            cookieService.addCookie(response, ACCESS_TOKEN, jwt, "/");
            RedirectView redirectView = new RedirectView("/dashboard", true);
            return new ModelAndView(redirectView);
        }
    }

    /**
     * register a new user action
     * @param userModel holds user data
     * @param redirectAttributes for redirection
     * @return model and view object with page name and model value if required
     */
    @PostMapping("/doRegister")
    public ModelAndView doRegister(UserModel userModel, RedirectAttributes redirectAttributes,
                                   HttpServletResponse response){
        logger.info(userModel.toString());
        if(userService.register(userModel)){
            //disable back
            response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");   //for http1.1
            response.setHeader(HttpHeaders.PRAGMA, "no-cache");     //for http1.0
            response.setHeader(HttpHeaders.EXPIRES, "0");           //for proxies

            RedirectView redirectView = new RedirectView("/login", true);
            return new ModelAndView(redirectView);
        }else {
            redirectAttributes.addFlashAttribute("error", UNABLE_TO_REGISTER);
            RedirectView redirectView = new RedirectView("/register", true);
            return new ModelAndView(redirectView);
        }
    }

    /**
     * update profile data action
     * @param updateUserModel data
     * @param token jwt token from http cookie
     * @return UserModel object with updated data
     */
    @PostMapping("/updateProfile")
    @ResponseBody
    public UserModel updateProfile(
            @RequestBody UpdateUserModel updateUserModel,
            @CookieValue(value = ACCESS_TOKEN, defaultValue = "") String token,
            HttpServletResponse response) throws IOException {
        logger.info(updateUserModel.toString());
        if (StringUtils.isEmpty(token)){
            return null;
        }
        return userService.updateUser(token, updateUserModel);
    }

    /**
     * logout action, invalidate token, this will remove/reset the token value stored in
     * http cookie for clearing session
     * @param token the jwt token in http cookie
     * @param request http request
     * @param response http response
     * @return the redirected page
     */
    @GetMapping("/logout")
    public String logout(@CookieValue(value = ACCESS_TOKEN, defaultValue = "") String token, HttpServletRequest request,
                         HttpServletResponse response){
        if (!userService.doLogout(token)) {
            return "redirect:dashboard";
        }else {
            cookieService.addCookie(response, ACCESS_TOKEN, null, "/");
            return "redirect:login";
        }
    }
}
