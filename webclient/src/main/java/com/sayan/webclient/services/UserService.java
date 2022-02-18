package com.sayan.webclient.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.sayan.webclient.models.*;
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

import static com.sayan.webclient.util.Constants.SERVER_BUSY;

@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Value("${service.authServer}")
    private String authServer;
    @Value("${service.url.getUserDetails}")
    private String getUserDetailsUrl;
    @Value("${service.url.updateUser}")
    private String updateUserUrl;
    @Value("${service.url.register}")
    private String registerUrl;
    @Value("${service.url.forgotPassword}")
    private String forgotPasswordUrl;
    @Value("${service.url.resetPassword}")
    private String resetPasswordUrl;
    @Value("${service.url.login}")
    private String loginUrl;
    @Value("${service.url.logout}")
    private String logoutUrl;
    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getUserDetailsFallback", threadPoolKey = "getProductThreadPool", commandKey = "getProductServiceCommand")
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

    public UserModel getUserDetailsFallback(String token) {
        return new UserModel("Unable to load", "Unable to load", "Unable to load",
                "Unable to load", "Unable to load", "Unable to load", "--");
    }

    @HystrixCommand(fallbackMethod = "updateUserFallback", threadPoolKey = "getProductThreadPool", commandKey = "getProductServiceCommand")
    public UserModel updateUser(String token, UpdateUserModel updateUserModel) {
        ResponseEntity<UserModel> responseEntity = null;
        UserModel user = UserModel.builder()
                .firstName(updateUserModel.getFirstName())
                .lastName(updateUserModel.getLastName())
                .email(updateUserModel.getEmail())
                .phone(updateUserModel.getPhone())
                .build();

        String url = authServer + updateUserUrl;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserModel> request = new HttpEntity<>(user, headers);

        try{
            responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, UserModel.class);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        if (responseEntity.getBody() == null) {
            return null;
        }
        logger.info(Objects.requireNonNull(responseEntity.getBody()).toString());
        return responseEntity.getBody();
    }

    public UserModel updateUserFallback(String token, UpdateUserModel updateUserModel){
        return new UserModel("Unable to load", "Unable to load", "Unable to load",
                "Unable to load", "Unable to load", "Unable to load", "--");
    }

    @HystrixCommand(fallbackMethod = "registerFallback", threadPoolKey = "getProductThreadPool", commandKey = "getProductServiceCommand")
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

    public boolean registerFallback(UserModel userModel){
        return false;
    }
    @HystrixCommand(fallbackMethod = "initiateForgotPasswordFallback", threadPoolKey = "getProductThreadPool", commandKey = "getProductServiceCommand")
    public Boolean initiateForgotPassword(ResetPasswordModel resetPasswordModel) {
        ResponseEntity<ForgotPasswordResponse> responseEntity = null;

        String url = authServer + forgotPasswordUrl;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ResetPasswordModel> request = new HttpEntity<>(resetPasswordModel, headers);

        try{
            responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, ForgotPasswordResponse.class);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        if (responseEntity.getBody() == null) {
            return false;
        }
        logger.info(Objects.requireNonNull(responseEntity.getBody()).toString());
        return responseEntity.getBody().getIsValid();
    }

    public Boolean initiateForgotPasswordFallback(ResetPasswordModel resetPasswordModel){
        return false;
    }

    @HystrixCommand(fallbackMethod = "resetPasswordFallback", threadPoolKey = "getProductThreadPool", commandKey = "getProductServiceCommand")
    public String resetPassword(PasswordModel passwordModel) {
        ResponseEntity<JwtResponse> responseEntity = null;

        String url = authServer + resetPasswordUrl;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PasswordModel> request = new HttpEntity<>(passwordModel, headers);

        try{
            responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, JwtResponse.class);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        if (responseEntity.getBody() == null) {
            return null;
        }
        logger.info(Objects.requireNonNull(responseEntity.getBody()).toString());
        return responseEntity.getBody().getJwtToken();
    }

    public String resetPasswordFallback(PasswordModel passwordModel){
        return SERVER_BUSY;
    }
    @HystrixCommand(fallbackMethod = "loginFallback", threadPoolKey = "getProductThreadPool", commandKey = "getProductServiceCommand")
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

    public String loginFallback(LoginModel loginModel){
        return SERVER_BUSY;
    }

    @HystrixCommand(fallbackMethod = "doLogoutFallback", threadPoolKey = "getProductThreadPool", commandKey = "getProductServiceCommand")
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

    public Boolean doLogoutFallback(String token){
        return false;
    }
}
