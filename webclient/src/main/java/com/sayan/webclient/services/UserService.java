package com.sayan.webclient.services;

import com.sayan.webclient.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private RestTemplate restTemplate;

    public UserModel getUserDetails(String token) {
        String url = "http://user-auth-service/user/";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<String> request = new HttpEntity<String>(headers);
        Map<String, String> map = new HashMap<>();
        ResponseEntity<UserModel> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET, request, UserModel.class, map);
        return responseEntity.getBody();
    }
}
