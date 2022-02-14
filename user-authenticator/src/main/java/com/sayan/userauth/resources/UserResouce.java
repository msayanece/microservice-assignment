package com.sayan.userauth.resources;

import com.sayan.userauth.models.User;
import com.sayan.userauth.services.UserService;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserResouce {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<UserDetails> getUserDetails(@PathVariable(name = "id") String username){
        return new ResponseEntity<>(userService.loadUserByUsername(username), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) throws Exception{
        if (user == null) throw new Exception("User is Required.");
        return new ResponseEntity<>(userService.insertNewUser(user), HttpStatus.CREATED);
    }
}
