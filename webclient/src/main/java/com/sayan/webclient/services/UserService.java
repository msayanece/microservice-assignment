package com.sayan.webclient.services;

import com.sayan.webclient.models.JwtResponse;
import com.sayan.webclient.models.LoginModel;
import com.sayan.webclient.models.LogoutResponse;
import com.sayan.webclient.models.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
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
    @Value("${service.url.login}")
    private String loginUrl;
    @Value("${service.url.logout}")
    private String logoutUrl;
    @Autowired
    private RestTemplate restTemplate;

    @Nullable
    public UserModel getUserDetails(String token) {
        ResponseEntity<UserModel> responseEntity = null;

        String url = authServer + getUserDetailsUrl;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<String> request = new HttpEntity<String>(headers);
        Map<String, String> map = new HashMap<>();

        try{
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, UserModel.class);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return responseEntity.getBody();
    }

    public boolean register(UserModel userModel) {
        ResponseEntity<UserModel> responseEntity = null;

        String url = authServer + registerUrl;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserModel> request = new HttpEntity<>(userModel, headers);

        try{
            responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, UserModel.class);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        if (responseEntity.getBody() == null) {
            return false;
        }
        logger.info(Objects.requireNonNull(responseEntity.getBody()).toString());
        return true;
    }

    @Nullable
    public String login(LoginModel loginModel) {
        ResponseEntity<JwtResponse> responseEntity = null;

        String url = authServer + loginUrl;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginModel> request = new HttpEntity<>(loginModel, headers);

        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, JwtResponse.class);
        }catch (HttpClientErrorException e){
            e.printStackTrace();
            return null;
        }
        if (responseEntity.getBody() == null) {
            return null;
        }
        logger.info(Objects.requireNonNull(responseEntity.getBody()).toString());
        return responseEntity.getBody().getJwtToken();
    }

    public Boolean doLogout(String token) {
        ResponseEntity<LogoutResponse> responseEntity = null;

        String url = authServer + logoutUrl;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
//        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, LogoutResponse.class);
        }catch (HttpClientErrorException e){
            e.printStackTrace();
            return false;
        }
        logger.info(Objects.requireNonNull(responseEntity.getBody()).toString());
        return responseEntity.getBody().getResult().equals("success");
    }
}
