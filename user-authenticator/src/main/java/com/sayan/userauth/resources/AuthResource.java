package com.sayan.userauth.resources;

import com.sayan.userauth.models.JwtRequest;
import com.sayan.userauth.models.JwtResponse;
import com.sayan.userauth.models.LogoutResponse;
import com.sayan.userauth.models.MyUserDetails;
import com.sayan.userauth.services.UserService;
import com.sayan.userauth.util.JwtUtility;
import io.swagger.annotations.ApiImplicitParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jwt")
public class AuthResource {

    private final Logger logger = LoggerFactory.getLogger(UserResource.class);

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("/auth")
    public ResponseEntity<JwtResponse> authenticate(@RequestBody JwtRequest jwtRequest) throws Exception{
        System.out.println("AUTHENTICATION STARTS...");
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            e.printStackTrace();
            throw new Exception("INVALID_CREDENTIALS", e);
        } catch (Exception e){
            e.printStackTrace();
            throw new Exception("INVALID_CREDENTIALS GENERAL EXCEPTION", e);
        }

        final UserDetails userDetails
                = userService.loadUserByUsername(jwtRequest.getUsername());

        final String token =
                jwtUtility.generateToken(userDetails);

        return  new ResponseEntity<>(new JwtResponse(token), HttpStatus.OK);
    }

    @PostMapping("/invalidate")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false,
            paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<LogoutResponse> invalidate() throws Exception{
        logger.trace("LOGOUT OPERATION STARTS...");
        //add to Redis blacklist
        logger.trace("LOGOUT OPERATION ENDS...");
        return  new ResponseEntity<>(new LogoutResponse("success"), HttpStatus.OK);
    }


}
