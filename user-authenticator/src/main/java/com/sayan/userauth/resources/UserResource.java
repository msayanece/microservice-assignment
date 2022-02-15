package com.sayan.userauth.resources;

import com.sayan.userauth.models.MyUserDetails;
import com.sayan.userauth.entities.User;
import com.sayan.userauth.models.PasswordModel;
import com.sayan.userauth.models.UserModel;
import com.sayan.userauth.services.UserService;
import io.swagger.annotations.ApiImplicitParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserResource {

    private final Logger logger = LoggerFactory.getLogger(UserResource.class);
    @Autowired
    private UserService userService;

    @GetMapping("/")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false,
            paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<UserDetails> getUserDetails(){
        logger.trace("getUserDetails: called");
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.info("getUserDetails: userDetails: " + userDetails);
        ResponseEntity<UserDetails> response =
                new ResponseEntity<>(userService.loadUserByUsername(userDetails.getUsername()), HttpStatus.OK);
        logger.trace("getUserDetails: response: " + response);
        return response;
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody UserModel user) throws RuntimeException{
        logger.trace("createUser: called");
        if (user == null){
            logger.error("createUser: User object null");
            throw new RuntimeException("User is Required.");
        }
        ResponseEntity<User> response = new ResponseEntity<>(userService.insertNewUser(user), HttpStatus.CREATED);
        logger.trace("createUser: response: " + response);
        return response;
    }

    @PostMapping("/changePassword")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false,
            paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<User> changePassword(@RequestBody PasswordModel passwordModel) throws RuntimeException{
        logger.trace("changePassword: called");
        if (passwordModel == null){
            logger.error("changePassword: passwordModel object null");
            throw new RuntimeException("passwordModel is Required.");
        }
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseEntity<User> response =
                new ResponseEntity<>(userService.changePassword(passwordModel, userDetails.getUsername()), HttpStatus.OK);
        logger.trace("changePassword: response: " + response);
        return response;
    }
}
