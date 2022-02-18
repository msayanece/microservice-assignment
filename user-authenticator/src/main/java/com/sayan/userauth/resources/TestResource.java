package com.sayan.userauth.resources;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestResource {

    /**
     * Used for testing purpose
     * @return
     */
    @GetMapping
    public String test(){
        return "Service is working fine.";
    }
}
