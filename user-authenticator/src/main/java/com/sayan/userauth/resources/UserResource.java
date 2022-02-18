package com.sayan.userauth.resources;

import com.sayan.userauth.models.*;
import com.sayan.userauth.entities.User;
import com.sayan.userauth.services.UserService;
import io.swagger.annotations.ApiImplicitParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserResource {

    private final Logger logger = LoggerFactory.getLogger(UserResource.class);
    @Autowired
    private UserService userService;
    @Autowired
    private AuthResource authResource;

    /**
     * get user details using the JWT token owner
     * @return user data
     */
    @GetMapping("/")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false,
            paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<UserModel> getUserDetails(){
        logger.trace("getUserDetails: called");
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.info("getUserDetails: userDetails: " + userDetails);
        MyUserDetails resultUserDetails = (MyUserDetails) userService.loadUserByUsername(userDetails.getUsername());
        User resultUser = resultUserDetails.getUser();
        //create UserModel Object
        UserModel user = UserModel.builder()
                .username(resultUser.getUsername())
                .firstName(resultUser.getFirstName())
                .email(resultUser.getEmail())
                .lastName(resultUser.getLastName())
                .password(resultUser.getPassword())
                .phone(resultUser.getPhone())
                .build();
        ResponseEntity<UserModel> response = new ResponseEntity<>(user, HttpStatus.OK);
        logger.trace("getUserDetails: response: " + response);
        return response;
    }

    /**
     * Register/insert a new user
     * @param user user data
     * @return Created user
     * @throws RuntimeException
     */
    @PostMapping("/create")
    public ResponseEntity<UserModel> createUser(@RequestBody UserModel user) throws RuntimeException{
        logger.trace("createUser: called");
        if (user == null){
            logger.error("createUser: User object null");
            throw new RuntimeException("User is Required.");
        }
        User resultUser = userService.insertNewUser(user);
        UserModel userModelResult = UserModel.builder()
                .username(resultUser.getUsername())
                .firstName(resultUser.getFirstName())
                .email(resultUser.getEmail())
                .lastName(resultUser.getLastName())
//                .password(resultUser.getPassword())
                .phone(resultUser.getPhone())
                .build();
        ResponseEntity<UserModel> response = new ResponseEntity<>(userModelResult, HttpStatus.CREATED);
        logger.trace("createUser: response: " + response);
        return response;
    }

    /**
     * Update a user data using the JWT token
     * @param user user data
     * @return the updated user
     * @throws RuntimeException
     */
    @PostMapping("/update")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false,
            paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<UserModel> updateUser(@RequestBody UserModel user) throws RuntimeException{
        logger.trace("updateUser: called");
        if (user == null){
            logger.error("updateUser: User object null");
            throw new RuntimeException("User is Required.");
        }
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.info("getUserDetails: userDetails: " + userDetails);
        User resultUser = userService.updateUser(user, userDetails.getUsername());
        UserModel userModelResult = UserModel.builder()
                .firstName(resultUser.getFirstName())
                .email(resultUser.getEmail())
                .lastName(resultUser.getLastName())
                .phone(resultUser.getPhone())
                .build();
        ResponseEntity<UserModel> response = new ResponseEntity<>(userModelResult, HttpStatus.OK);
        logger.trace("updateUser: response: " + response);
        return response;
    }

    /**
     * Used to validate a username for resetting a password
     * @param passwordModel holds username data
     * @return response isValid
     * @throws RuntimeException
     */
    @PostMapping("/forgotPassword")
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(@RequestBody ResetPasswordModel passwordModel) throws RuntimeException{
        logger.trace("forgotPassword: called");
        if (passwordModel == null){
            logger.error("forgotPassword: passwordModel object null");
            throw new RuntimeException("ResetPasswordModel is Required.");
        }
        Boolean isValid = userService.validateUsername(passwordModel.getUsername());
        ResponseEntity<ForgotPasswordResponse> response =
                new ResponseEntity<>(new ForgotPasswordResponse(isValid), HttpStatus.OK);
        logger.trace("forgotPassword: response: " + response);
        return response;
    }

    /**
     * reset password, after validating the user identification
     * @param passwordModel new password data
     * @return the generated JWT token
     * @throws RuntimeException
     */
    @PostMapping("/resetPassword")
    public ResponseEntity<JwtResponse> resetPassword(@RequestBody PasswordModel passwordModel) throws RuntimeException{
        logger.trace("resetPassword: called");
        ResponseEntity<JwtResponse> jwtResponse = null;
        if (passwordModel == null){
            logger.error("resetPassword: PasswordModel object null");
            throw new RuntimeException("PasswordModel is Required.");
        }
        Boolean isSuccess = userService.resetPassword(passwordModel);
        try {
            jwtResponse = authResource.authenticate(new JwtRequest(passwordModel.getUsername(), passwordModel.getNewPassword()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Server error: Unable to login");
        }
        if (jwtResponse == null) {
            throw new RuntimeException("Server error: Unable to login");
        }
        logger.trace("resetPassword: response: " + jwtResponse);
        return jwtResponse;
    }
}
