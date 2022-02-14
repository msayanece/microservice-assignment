package com.sayan.userauth.resources;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserResouce {

    @GetMapping("/{id}")
    public SecurityProperties.User getUserDetails(@PathVariable String username){
        return new SecurityProperties.User();
    }
}
