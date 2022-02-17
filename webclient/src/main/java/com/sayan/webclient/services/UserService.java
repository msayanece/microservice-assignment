package com.sayan.webclient.services;

import com.sayan.webclient.models.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Value("${service.authServer}")
    private String authServer;
    @Value("${service.url.getUserDetails}")
    private String getUserDetailsUrl;
    @Value("${service.url.register}")
    private String registerUrl;
    @Autowired
    private RestTemplate restTemplate;

    public UserModel getUserDetails(String token) {
        String url = authServer + getUserDetailsUrl;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<String> request = new HttpEntity<String>(headers);
        Map<String, String> map = new HashMap<>();
        ResponseEntity<UserModel> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET, request, UserModel.class);
        return responseEntity.getBody();
    }

    public boolean register(UserModel userModel) {
        String url = authServer + registerUrl;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserModel> request = new HttpEntity<>(userModel, headers);

        ResponseEntity<UserModel> responseEntity =
                restTemplate.exchange(url, HttpMethod.POST, request, UserModel.class);
        if (responseEntity.getBody() == null) {
            return false;
        }
        logger.info(Objects.requireNonNull(responseEntity.getBody()).toString());
        return true;
    }
}
